@file:OptIn(androidx.compose.foundation.layout.ExperimentalLayoutApi::class)

package com.nuezgolcontrol.app.ui.trabajadores

import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.InputChip
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nuezgolcontrol.app.data.Gasto
import com.nuezgolcontrol.app.data.PagoTrabajador
import com.nuezgolcontrol.app.ui.components.ConfirmDeleteDialog
import com.nuezgolcontrol.app.ui.components.EmptyState
import com.nuezgolcontrol.app.ui.theme.PurplePrimary
import com.nuezgolcontrol.app.ui.theme.SurfaceElevated
import com.nuezgolcontrol.app.ui.theme.TealTotal
import com.nuezgolcontrol.app.util.ExcelExporter
import com.nuezgolcontrol.app.util.Formatters
import kotlinx.coroutines.launch

@Composable
fun TrabajadoresScreen(
    viewModel: TrabajadoresViewModel,
    onNuevoPago: () -> Unit
) {
    val pagos by viewModel.pagos.collectAsStateWithLifecycle()
    val gastos by viewModel.gastos.collectAsStateWithLifecycle()
    val trabajadoresGuardados by viewModel.trabajadoresGuardados.collectAsStateWithLifecycle()

    val totalPagos = pagos.sumOf { it.total }
    val totalGastos = gastos.sumOf { it.monto }
    val totalEgresos = totalPagos + totalGastos

    val context = LocalContext.current
    val snackbar = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Estados de UI
    var pendienteEliminarPago by remember { mutableStateOf<PagoTrabajador?>(null) }
    var pendienteEliminarGasto by remember { mutableStateOf<Gasto?>(null) }
    var mostrarResumen by remember { mutableStateOf(true) }
    var mostrarPagos by remember { mutableStateOf(true) }
    var mostrarGastos by remember { mutableStateOf(true) }
    var mostrarTrabajadores by remember { mutableStateOf(false) }

    // Estado del formulario de gasto rápido
    var conceptoGasto by remember { mutableStateOf("") }
    var montoGasto by remember { mutableStateOf("") }
    var mostrarFormGasto by remember { mutableStateOf(false) }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbar) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNuevoPago,
                containerColor = PurplePrimary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Nuevo pago")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(padding)
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(bottom = 100.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item { Spacer(Modifier.height(4.dp)) }

            // ── ENCABEZADO ──────────────────────────────────────────────
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Pagos y Gastos", style = MaterialTheme.typography.headlineMedium)
                    if (pagos.isNotEmpty()) {
                        IconButton(onClick = {
                            val intent = ExcelExporter.exportarPagosTrabajadores(context, pagos)
                            context.startActivity(Intent.createChooser(intent, "Exportar pagos"))
                        }) {
                            Icon(Icons.Default.FileDownload, contentDescription = "Exportar CSV", tint = PurplePrimary)
                        }
                    }
                }
            }

            // ── RESUMEN GENERAL ──────────────────────────────────────────
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = SurfaceElevated),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Resumen General", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                            IconButton(onClick = { mostrarResumen = !mostrarResumen }) {
                                Icon(
                                    imageVector = if (mostrarResumen) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                    contentDescription = null, tint = PurplePrimary
                                )
                            }
                        }
                        AnimatedVisibility(visible = mostrarResumen) {
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

                                // Resumen por trabajador
                                if (pagos.isNotEmpty()) {
                                    Text("Pagos por trabajador:", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    pagos.groupBy { it.trabajadorNombre }
                                        .map { (nombre, lista) -> nombre to lista.sumOf { it.total } }
                                        .sortedByDescending { it.second }
                                        .forEach { (nombre, total) ->
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween
                                            ) {
                                                Text(nombre, style = MaterialTheme.typography.bodyMedium)
                                                Text(Formatters.dinero(total), style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold, color = TealTotal)
                                            }
                                        }
                                    HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
                                }

                                ResumenRow("Total pagos trabajadores:", Formatters.dinero(totalPagos), isSubtotal = true)
                                ResumenRow("Total gastos / descuentos:", Formatters.dinero(totalGastos), isSubtotal = true)
                                HorizontalDivider()
                                ResumenRow("TOTAL EGRESOS:", Formatters.dinero(totalEgresos), isTotal = true)
                            }
                        }
                    }
                }
            }

            // ── LISTA DE PAGOS ───────────────────────────────────────────
            item {
                SectionHeader(
                    title = "Pagos a Trabajadores (${pagos.size})",
                    expanded = mostrarPagos,
                    onToggle = { mostrarPagos = !mostrarPagos }
                )
            }
            if (mostrarPagos) {
                if (pagos.isEmpty()) {
                    item { EmptyState("No hay pagos. Toca + para agregar.") }
                } else {
                    items(pagos, key = { it.id }) { pago ->
                        PagoTrabajadorCard(
                            pago = pago,
                            onEliminar = { pendienteEliminarPago = pago },
                            modifier = Modifier.animateItem()
                        )
                    }
                }
            }

            // ── GASTOS / DESCUENTOS ──────────────────────────────────────
            item {
                SectionHeader(
                    title = "Gastos / Descuentos (${gastos.size})",
                    expanded = mostrarGastos,
                    onToggle = { mostrarGastos = !mostrarGastos }
                )
            }
            if (mostrarGastos) {
                // Formulario rápido de gasto
                item {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = SurfaceElevated),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            TextButton(onClick = { mostrarFormGasto = !mostrarFormGasto }) {
                                Icon(Icons.Default.Add, contentDescription = null, tint = PurplePrimary)
                                Spacer(Modifier.width(4.dp))
                                Text("Agregar gasto / descuento", color = PurplePrimary)
                            }
                            AnimatedVisibility(visible = mostrarFormGasto) {
                                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                    OutlinedTextField(
                                        value = conceptoGasto,
                                        onValueChange = { conceptoGasto = it },
                                        label = { Text("Concepto") },
                                        placeholder = { Text("Ej: Fertilizante, Pago agua…") },
                                        singleLine = true,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                    OutlinedTextField(
                                        value = montoGasto,
                                        onValueChange = { montoGasto = it },
                                        label = { Text("Monto (\$)") },
                                        prefix = { Text("\$") },
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                        singleLine = true,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                    Button(
                                        onClick = {
                                            val m = montoGasto.replace(',', '.').toDoubleOrNull()
                                            if (conceptoGasto.isBlank() || m == null || m <= 0) {
                                                scope.launch { snackbar.showSnackbar("Ingresa concepto y monto válido") }
                                                return@Button
                                            }
                                            viewModel.agregarGasto(conceptoGasto, m)
                                            conceptoGasto = ""
                                            montoGasto = ""
                                            mostrarFormGasto = false
                                        },
                                        modifier = Modifier.fillMaxWidth(),
                                        colors = ButtonDefaults.buttonColors(containerColor = PurplePrimary)
                                    ) {
                                        Text("Guardar Gasto")
                                    }
                                }
                            }
                        }
                    }
                }

                if (gastos.isEmpty()) {
                    item { EmptyState("Sin gastos registrados.") }
                } else {
                    items(gastos, key = { it.id }) { gasto ->
                        GastoCard(
                            gasto = gasto,
                            onEliminar = { pendienteEliminarGasto = gasto },
                            modifier = Modifier.animateItem()
                        )
                    }
                }
            }

            // ── TRABAJADORES GUARDADOS ───────────────────────────────────
            if (trabajadoresGuardados.isNotEmpty()) {
                item {
                    SectionHeader(
                        title = "Trabajadores guardados (${trabajadoresGuardados.size})",
                        expanded = mostrarTrabajadores,
                        onToggle = { mostrarTrabajadores = !mostrarTrabajadores }
                    )
                }
                if (mostrarTrabajadores) {
                    item {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = SurfaceElevated),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    "Aparecen como sugerencias al registrar pagos. Toca × para borrar.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(Modifier.height(8.dp))
                                androidx.compose.foundation.layout.FlowRow(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    trabajadoresGuardados.forEach { t ->
                                        InputChip(
                                            selected = false,
                                            onClick = {},
                                            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                                            label = { Text(t.nombre) },
                                            trailingIcon = {
                                                IconButton(onClick = { viewModel.eliminarTrabajador(t.nombre) }) {
                                                    Icon(Icons.Default.Delete, contentDescription = "Borrar", tint = MaterialTheme.colorScheme.error)
                                                }
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Diálogos de confirmación
    pendienteEliminarPago?.let { pago ->
        ConfirmDeleteDialog(
            title = "Eliminar pago",
            message = "¿Eliminar el pago de ${pago.trabajadorNombre} (${Formatters.dinero(pago.total)})?",
            onConfirm = {
                viewModel.eliminarPago(pago.id)
                pendienteEliminarPago = null
                scope.launch { snackbar.showSnackbar("Pago eliminado") }
            },
            onDismiss = { pendienteEliminarPago = null }
        )
    }

    pendienteEliminarGasto?.let { gasto ->
        ConfirmDeleteDialog(
            title = "Eliminar gasto",
            message = "¿Eliminar \"${gasto.concepto}\" (${Formatters.dinero(gasto.monto)})?",
            onConfirm = {
                viewModel.eliminarGasto(gasto.id)
                pendienteEliminarGasto = null
                scope.launch { snackbar.showSnackbar("Gasto eliminado") }
            },
            onDismiss = { pendienteEliminarGasto = null }
        )
    }
}

// ── Componentes privados ──────────────────────────────────────────────────────

@Composable
private fun SectionHeader(title: String, expanded: Boolean, onToggle: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
        IconButton(onClick = onToggle) {
            Icon(
                imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                contentDescription = null, tint = PurplePrimary
            )
        }
    }
}

@Composable
private fun ResumenRow(label: String, value: String, isSubtotal: Boolean = false, isTotal: Boolean = false) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = if (isTotal) MaterialTheme.typography.titleMedium else MaterialTheme.typography.bodyMedium,
            fontWeight = if (isTotal || isSubtotal) FontWeight.Bold else FontWeight.Normal
        )
        Text(
            text = value,
            style = if (isTotal) MaterialTheme.typography.titleMedium else MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = if (isTotal) MaterialTheme.colorScheme.error else TealTotal
        )
    }
}

@Composable
private fun PagoTrabajadorCard(
    pago: PagoTrabajador,
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
                Text(Formatters.fecha(pago.fecha), style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
                IconButton(onClick = onEliminar) {
                    Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = MaterialTheme.colorScheme.error)
                }
            }
            Text("Trabajador: ${pago.trabajadorNombre}", fontWeight = FontWeight.Medium)
            if (pago.tipoPago == "Raya") {
                Text("Modalidad: Por Raya (Diario)")
            } else {
                Text("Modalidad: Por Kilo")
                Text("Kilos: ${Formatters.kg(pago.kilos)}  ·  ${Formatters.dinero(pago.precioPorKilo)}/kg")
            }
            Text(
                text = "Total: ${Formatters.dinero(pago.total)}",
                color = TealTotal, fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

@Composable
private fun GastoCard(
    gasto: Gasto,
    onEliminar: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = SurfaceElevated),
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(gasto.concepto, fontWeight = FontWeight.Medium)
                Text(Formatters.fecha(gasto.fecha), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Text(
                text = "- ${Formatters.dinero(gasto.monto)}",
                color = MaterialTheme.colorScheme.error,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleSmall
            )
            IconButton(onClick = onEliminar) {
                Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = MaterialTheme.colorScheme.error)
            }
        }
    }
}
