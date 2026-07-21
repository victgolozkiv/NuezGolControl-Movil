package com.nuezgolcontrol.app.ui.resumen

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nuezgolcontrol.app.ui.theme.Danger
import com.nuezgolcontrol.app.ui.theme.PurpleDeep
import com.nuezgolcontrol.app.ui.theme.PurplePrimary
import com.nuezgolcontrol.app.ui.theme.TealTotal
import com.nuezgolcontrol.app.ui.theme.SurfaceElevated
import com.nuezgolcontrol.app.util.Formatters
import com.nuezgolcontrol.app.util.AdvancedReportExporter

@Composable
fun ResumenFinancieroScreen(
    viewModel: ResumenFinancieroViewModel,
    filtrosViewModel: ResumenConFiltrosViewModel,
    estadisticasViewModel: EstadisticasViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    var selectedSubTab by remember { mutableIntStateOf(0) }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(top = 16.dp)
    ) {
        // Encabezado principal del Dashboard
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "🌰 NUEZGOL",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = TealTotal,
                    letterSpacing = 1.5.sp
                )
                Text(
                    text = "Panel Financiero",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
            Box(
                modifier = Modifier
                    .background(
                        color = PurplePrimary.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .border(1.dp, PurplePrimary.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "100% Offline",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = PurplePrimary
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Selector de Pestañas (Segmented Pills)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .background(Color(0xFF121212), shape = RoundedCornerShape(14.dp))
                .border(1.dp, Color(0xFF222222), shape = RoundedCornerShape(14.dp))
                .padding(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            val tabs = listOf("General", "Por Fechas", "Productos")
            tabs.forEachIndexed { index, title ->
                val isSelected = selectedSubTab == index
                val tabBgColor by animateColorAsState(
                    targetValue = if (isSelected) PurplePrimary else Color.Transparent,
                    animationSpec = tween(200),
                    label = "tabBg"
                )
                val tabTextColor by animateColorAsState(
                    targetValue = if (isSelected) Color.White else Color(0xFF888888),
                    animationSpec = tween(200),
                    label = "tabText"
                )

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .background(tabBgColor, shape = RoundedCornerShape(10.dp))
                        .clickable { selectedSubTab = index }
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = title,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = tabTextColor
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Contenedor principal con transición Crossfade
        Crossfade(
            targetState = selectedSubTab,
            animationSpec = tween(durationMillis = 300),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            label = "dashboard_fade"
        ) { subTabIndex ->
            when (subTabIndex) {
                0 -> {
                    // Vista General
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Card Ingresos Totales
                        FinancialCard(
                            title = "Ingresos Totales",
                            amount = uiState.totalIngresos,
                            icon = Icons.Default.TrendingUp,
                            backgroundColor = Color(0xFF1B5E20).copy(alpha = 0.25f),
                            borderColor = Color(0xFF81C784).copy(alpha = 0.3f),
                            textColor = Color(0xFF81C784),
                            description = "De todas las ventas"
                        )

                        // Card Egresos - dividido en dos subcategorías
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .animateContentSize()
                                .border(1.dp, Color(0xFFCE93D8).copy(alpha = 0.25f), RoundedCornerShape(16.dp)),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = PurpleDeep.copy(alpha = 0.3f)
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(20.dp),
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
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
                                                    color = Danger.copy(alpha = 0.15f),
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

                                // Divisor visual
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(1.dp)
                                        .background(Color(0xFF6F42C1).copy(alpha = 0.4f))
                                )

                                // Pagos a empleados
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(
                                            text = "Pagos a Empleados",
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = Color(0xFFFFB74D)
                                        )
                                        Text(
                                            text = "Mano de obra",
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
                                            text = "Gastos Operativos",
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = Color(0xFF64B5F6)
                                        )
                                        Text(
                                            text = "Fertilizante, agua, etc.",
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

                        // Card Ganancia Neta (Profit)
                        val isPositive = uiState.gananciaNet >= 0
                        FinancialCard(
                            title = "Ganancia Neta",
                            amount = uiState.gananciaNet,
                            icon = Icons.Default.AttachMoney,
                            backgroundColor = if (isPositive) Color(0xFF1B5E20).copy(alpha = 0.25f) else Color(0xFFB71C1C).copy(alpha = 0.25f),
                            borderColor = if (isPositive) TealTotal.copy(alpha = 0.3f) else Danger.copy(alpha = 0.3f),
                            textColor = if (isPositive) TealTotal else Danger,
                            description = if (isPositive) "✅ Balance positivo" else "⚠️ Balance negativo"
                        )

                        // Card/Widget interactivo para exportar reporte completo
                        ExportReportCard {
                            val reportUri = AdvancedReportExporter.generarReporteCompleto(
                                context = context,
                                ventas = uiState.ventas,
                                pagos = uiState.pagos,
                                gastos = uiState.gastos,
                                cosechas = uiState.cosechas,
                                titulo = "Resumen Ejecutivo de Campo"
                            )
                            if (reportUri != null) {
                                AdvancedReportExporter.compartirReporte(context, reportUri)
                            }
                        }

                        Spacer(modifier = Modifier.height(30.dp))
                    }
                }
                1 -> {
                    // Vista con Filtros de Fecha
                    ResumenConFiltrosScreen(viewModel = filtrosViewModel)
                }
                2 -> {
                    // Estadísticas de Producto
                    EstadisticasScreen(viewModel = estadisticasViewModel)
                }
            }
        }
    }
}

@Composable
fun FinancialCard(
    title: String,
    amount: Double,
    icon: ImageVector,
    backgroundColor: Color,
    borderColor: Color,
    textColor: Color,
    description: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize()
            .border(1.dp, borderColor, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
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
                    text = description,
                    fontSize = 12.sp,
                    color = textColor.copy(alpha = 0.7f)
                )
            }
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = textColor,
                modifier = Modifier
                    .background(
                        color = textColor.copy(alpha = 0.12f),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(14.dp)
            )
        }
    }
}

@Composable
fun ExportReportCard(onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .border(1.dp, PurplePrimary.copy(alpha = 0.35f), RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF0F0F0F)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .background(PurplePrimary.copy(alpha = 0.15f), RoundedCornerShape(12.dp))
                    .padding(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = null,
                    tint = PurplePrimary,
                    modifier = Modifier.size(24.dp)
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Exportar Reporte Completo",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Genera un archivo CSV con todo el desglose de ventas, pagos y gastos operativos listo para Excel.",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    lineHeight = 16.sp
                )
            }
        }
    }
}
