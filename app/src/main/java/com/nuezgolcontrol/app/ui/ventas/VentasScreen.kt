package com.nuezgolcontrol.app.ui.ventas

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nuezgolcontrol.app.data.Venta
import com.nuezgolcontrol.app.ui.components.ConfirmDeleteDialog
import com.nuezgolcontrol.app.ui.components.EmptyState
import com.nuezgolcontrol.app.ui.components.TotalFooter
import com.nuezgolcontrol.app.ui.theme.PurplePrimary
import com.nuezgolcontrol.app.ui.theme.SurfaceElevated
import com.nuezgolcontrol.app.ui.theme.TealTotal
import com.nuezgolcontrol.app.util.ExcelExporter
import com.nuezgolcontrol.app.util.Formatters
import kotlinx.coroutines.launch

@Composable
fun VentasScreen(
    viewModel: VentasViewModel,
    onNuevaVenta: () -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbar = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    var pendienteEliminar by remember { mutableStateOf<Venta?>(null) }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbar) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNuevaVenta,
                containerColor = PurplePrimary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Nueva venta")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(Modifier.height(12.dp))
            Text(
                text = "Ventas Registradas",
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(Modifier.height(12.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    onClick = {
                        scope.launch {
                            val ventas = viewModel.listarParaExportar()
                            if (ventas.isEmpty()) {
                                snackbar.showSnackbar("No hay ventas para exportar")
                            } else {
                                val intent = ExcelExporter.exportarVentas(context, ventas)
                                context.startActivity(Intent.createChooser(intent, "Exportar ventas"))
                            }
                        }
                    }
                ) {
                    Icon(Icons.Default.FileDownload, contentDescription = null)
                    Spacer(Modifier.padding(4.dp))
                    Text("Excel")
                }
                Button(
                    onClick = onNuevaVenta,
                    colors = ButtonDefaults.buttonColors(containerColor = PurplePrimary)
                ) {
                    Text("Nueva Venta")
                }
            }
            Spacer(Modifier.height(16.dp))

            if (state.ventas.isEmpty()) {
                EmptyState("No hay ventas registradas. ¡Registra tu primera venta!")
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(bottom = 88.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(state.ventas, key = { it.id }) { venta ->
                        VentaCard(
                            venta = venta,
                            onEliminar = { pendienteEliminar = venta },
                            modifier = Modifier.animateItem()
                        )
                    }
                    item {
                        TotalFooter(
                            label = "Total General:",
                            value = Formatters.dinero(state.totalGeneral)
                        )
                    }
                }
            }
        }
    }

    pendienteEliminar?.let { venta ->
        ConfirmDeleteDialog(
            title = "Confirmar eliminación",
            message = "¿Estás seguro de que deseas eliminar esta venta? Esta acción no se puede deshacer.",
            onConfirm = {
                viewModel.eliminar(venta.id)
                pendienteEliminar = null
                scope.launch { snackbar.showSnackbar("Venta eliminada") }
            },
            onDismiss = { pendienteEliminar = null }
        )
    }
}

@Composable
private fun VentaCard(
    venta: Venta,
    onEliminar: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = SurfaceElevated),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = Formatters.fecha(venta.fechaMillis),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold
                )
                IconButton(onClick = onEliminar) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Eliminar",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
            Text(text = "Cliente: ${venta.cliente}")
            Text(text = "Tipo: ${venta.tipoNuez}")
            Text(text = "Cantidad: ${Formatters.kg(venta.cantidad)}")
            Text(text = "Precio: ${Formatters.dinero(venta.precioUnitario)} / kg")
            Text(
                text = "Total: ${Formatters.dinero(venta.total)}",
                color = TealTotal,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}
