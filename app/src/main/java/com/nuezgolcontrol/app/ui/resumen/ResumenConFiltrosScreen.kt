package com.nuezgolcontrol.app.ui.resumen

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nuezgolcontrol.app.ui.theme.Danger
import com.nuezgolcontrol.app.ui.theme.PurplePrimary
import com.nuezgolcontrol.app.ui.theme.TealTotal
import com.nuezgolcontrol.app.util.Formatters

data class FilterOption(
    val label: String,
    val action: () -> Unit
)

@Composable
fun ResumenConFiltrosScreen(viewModel: ResumenConFiltrosViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    
    val filters = listOf(
        FilterOption("Hoy") { viewModel.filtrarPorHoy() },
        FilterOption("Esta semana") { viewModel.filtrarPorEstaaSemana() },
        FilterOption("Este mes") { viewModel.filtrarPorEsteMes() },
        FilterOption("Últimos 30 días") { viewModel.filtrarPorUltimosTreintaDias() },
        FilterOption("Todo el tiempo") { viewModel.filtrarTodoElTiempo() }
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Encabezado
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = PurplePrimary.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(16.dp)
        ) {
            Text(
                text = "📊 Resumen Financiero Detallado",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Período: ${uiState.filtroActual}",
                fontSize = 12.sp,
                color = TealTotal,
                fontWeight = FontWeight.SemiBold
            )
        }

        // Filtros horizontales
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(filters) { filter ->
                AssistChip(
                    onClick = filter.action,
                    label = { Text(filter.label, fontSize = 12.sp) },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = if (filter.label == uiState.filtroActual) PurplePrimary else Color(0xFF2A2A2A),
                        labelColor = if (filter.label == uiState.filtroActual) Color.White else MaterialTheme.colorScheme.onBackground
                    ),
                    modifier = Modifier.height(32.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Cards de Ingresos
        FinancialCardConDetalles(
            title = "💰 Ingresos Totales",
            amount = uiState.totalIngresos,
            icon = Icons.Default.TrendingUp,
            backgroundColor = Color(0xFF1B5E20),
            textColor = Color(0xFF81C784),
            details = "${uiState.ventasCount} venta${if (uiState.ventasCount != 1) "s" else ""}"
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Card de Egresos con detalles
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF4A148C)),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Total Egresos
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = Icons.Default.TrendingDown,
                            contentDescription = null,
                            tint = Danger,
                            modifier = Modifier
                                .padding(end = 12.dp)
                                .background(
                                    color = Danger.copy(alpha = 0.2f),
                                    shape = RoundedCornerShape(50.dp)
                                )
                                .padding(8.dp)
                        )
                        Column {
                            Text(
                                text = "Total Egresos",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFFFF6B6B)
                            )
                            Text(
                                text = "Pagos + Gastos",
                                fontSize = 12.sp,
                                color = Color(0xFFEF5350)
                            )
                        }
                    }
                    Text(
                        text = Formatters.dinero(uiState.totalEgresos),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFF6B6B)
                    )
                }

                // Divisor
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(Color(0xFF6F42C1))
                )

                // Pagos a empleados
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "👥 Pagos a Empleados",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFFFFB74D)
                        )
                        Text(
                            text = "${uiState.pagosCount} pago${if (uiState.pagosCount != 1) "s" else ""}",
                            fontSize = 11.sp,
                            color = Color(0xFFFF9800)
                        )
                    }
                    Text(
                        text = Formatters.dinero(uiState.totalPagosEmpleados),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFFB74D)
                    )
                }

                // Gastos operativos
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "🔧 Gastos Operativos",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF64B5F6)
                        )
                        Text(
                            text = "${uiState.gastosCount} gasto${if (uiState.gastosCount != 1) "s" else ""}",
                            fontSize = 11.sp,
                            color = Color(0xFF42A5F5)
                        )
                    }
                    Text(
                        text = Formatters.dinero(uiState.totalGastos),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF64B5F6)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Card Ganancia Neta con indicador
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (uiState.gananciaNet >= 0) Color(0xFF1B5E20) else Color(0xFF8B0000)
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (uiState.gananciaNet >= 0) "✅ Ganancia Neta" else "❌ Pérdida Neta",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (uiState.gananciaNet >= 0) Color(0xFF81C784) else Color(0xFFFF6B6B)
                        )
                        Text(
                            text = "Ingresos - Egresos",
                            fontSize = 12.sp,
                            color = if (uiState.gananciaNet >= 0) Color(0xFF81C784).copy(alpha = 0.7f) else Color(0xFFFF6B6B).copy(alpha = 0.7f)
                        )
                    }
                    Text(
                        text = Formatters.dinero(uiState.gananciaNet),
                        fontSize = 28.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = if (uiState.gananciaNet >= 0) Color(0xFF81C784) else Color(0xFFFF6B6B)
                    )
                }
                
                // Barra de progreso visual
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                        .background(
                            color = if (uiState.gananciaNet >= 0) Color(0xFF81C784).copy(alpha = 0.3f) else Color(0xFFFF6B6B).copy(alpha = 0.3f),
                            shape = RoundedCornerShape(2.dp)
                        )
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun FinancialCardConDetalles(
    title: String,
    amount: Double,
    icon: ImageVector,
    backgroundColor: Color,
    textColor: Color,
    details: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = textColor
                )
                Text(
                    text = Formatters.dinero(amount),
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = textColor
                )
                Text(
                    text = details,
                    fontSize = 11.sp,
                    color = textColor.copy(alpha = 0.7f)
                )
            }
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = textColor,
                modifier = Modifier
                    .background(
                        color = textColor.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(16.dp)
            )
        }
    }
}
