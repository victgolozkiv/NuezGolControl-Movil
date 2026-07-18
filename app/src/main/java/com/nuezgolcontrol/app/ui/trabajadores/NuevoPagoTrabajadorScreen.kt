package com.nuezgolcontrol.app.ui.trabajadores

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nuezgolcontrol.app.ui.theme.PurplePrimary
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NuevoPagoTrabajadorScreen(
    viewModel: TrabajadoresViewModel,
    onBack: () -> Unit
) {
    var tipoPago by remember { mutableStateOf("Kilo") }
    var trabajador by remember { mutableStateOf("") }
    var kilos by remember { mutableStateOf("") }
    var precioPorKilo by remember { mutableStateOf("17") }
    var montoRaya by remember { mutableStateOf("") }
    var expandedTrabajador by remember { mutableStateOf(false) }

    val snackbar = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Nombres guardados dinámicamente desde la BD
    val trabajadoresGuardados by viewModel.trabajadoresGuardados.collectAsStateWithLifecycle()
    val sugerencias = trabajadoresGuardados.map { it.nombre }
        .filter { it.contains(trabajador, ignoreCase = true) }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbar) },
        topBar = {
            TopAppBar(
                title = { Text("Nuevo Pago") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Nombre del trabajador — dropdown dinámico filtrado
            ExposedDropdownMenuBox(
                expanded = expandedTrabajador && sugerencias.isNotEmpty(),
                onExpandedChange = { expandedTrabajador = it }
            ) {
                OutlinedTextField(
                    value = trabajador,
                    onValueChange = {
                        trabajador = it
                        expandedTrabajador = true
                    },
                    label = { Text("Nombre del Trabajador") },
                    placeholder = { Text("Escribe el nombre…") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(MenuAnchorType.PrimaryEditable),
                    singleLine = true,
                    trailingIcon = {
                        if (sugerencias.isNotEmpty()) {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedTrabajador)
                        }
                    }
                )
                ExposedDropdownMenu(
                    expanded = expandedTrabajador && sugerencias.isNotEmpty(),
                    onDismissRequest = { expandedTrabajador = false }
                ) {
                    sugerencias.forEach { opcion ->
                        DropdownMenuItem(
                            text = { Text(opcion) },
                            onClick = {
                                trabajador = opcion
                                expandedTrabajador = false
                            }
                        )
                    }
                }
            }

            // Tipo de pago
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (tipoPago == "Kilo") {
                    Button(onClick = { }, modifier = Modifier.weight(1f), colors = ButtonDefaults.buttonColors(containerColor = PurplePrimary)) { Text("Por Kilo") }
                } else {
                    OutlinedButton(onClick = { tipoPago = "Kilo" }, modifier = Modifier.weight(1f)) { Text("Por Kilo") }
                }

                if (tipoPago == "Raya") {
                    Button(onClick = { }, modifier = Modifier.weight(1f), colors = ButtonDefaults.buttonColors(containerColor = PurplePrimary)) { Text("Por Raya (Día)") }
                } else {
                    OutlinedButton(onClick = { tipoPago = "Raya" }, modifier = Modifier.weight(1f)) { Text("Por Raya (Día)") }
                }
            }

            // Campos según tipo
            if (tipoPago == "Kilo") {
                OutlinedTextField(
                    value = kilos,
                    onValueChange = { kilos = it },
                    label = { Text("Kilos hechos") },
                    suffix = { Text("kg") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = precioPorKilo,
                    onValueChange = { precioPorKilo = it },
                    label = { Text("Precio a pagar por kilo (\$)") },
                    prefix = { Text("\$") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth()
                )
                // Preview del total
                val totalPreview = (kilos.toDoubleOrNull() ?: 0.0) * (precioPorKilo.toDoubleOrNull() ?: 0.0)
                if (totalPreview > 0) {
                    Text(
                        text = "Total a pagar: \$${"%.2f".format(totalPreview)}",
                        style = MaterialTheme.typography.titleMedium,
                        color = PurplePrimary
                    )
                }
            } else {
                OutlinedTextField(
                    value = montoRaya,
                    onValueChange = { montoRaya = it },
                    label = { Text("Pago de Raya (\$)") },
                    prefix = { Text("\$") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(Modifier.height(8.dp))

            Button(
                onClick = {
                    if (trabajador.isBlank()) {
                        scope.launch { snackbar.showSnackbar("Escribe el nombre del trabajador") }
                        return@Button
                    }
                    if (tipoPago == "Kilo") {
                        val k = kilos.replace(',', '.').toDoubleOrNull() ?: 0.0
                        val ppk = precioPorKilo.replace(',', '.').toDoubleOrNull() ?: 0.0
                        if (k <= 0 || ppk <= 0) {
                            scope.launch { snackbar.showSnackbar("Ingresa kilos y precio válidos") }
                            return@Button
                        }
                        viewModel.agregarPago(trabajador, tipoPago, k, ppk, 0.0)
                    } else {
                        val mr = montoRaya.replace(',', '.').toDoubleOrNull() ?: 0.0
                        if (mr <= 0) {
                            scope.launch { snackbar.showSnackbar("Ingresa un monto válido") }
                            return@Button
                        }
                        viewModel.agregarPago(trabajador, tipoPago, 0.0, 0.0, mr)
                    }
                    onBack()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PurplePrimary)
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Icon(Icons.Default.Check, contentDescription = null)
                    Text("Guardar Pago")
                }
            }
        }
    }
}
