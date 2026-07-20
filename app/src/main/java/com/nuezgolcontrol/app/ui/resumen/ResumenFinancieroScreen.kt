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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.TrendingUp
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
import com.nuezgolcontrol.app.ui.theme.PurpleDeep
import com.nuezgolcontrol.app.ui.theme.PurplePrimary
import com.nuezgolcontrol.app.ui.theme.TealTotal
import com.nuezgolcontrol.app.util.Formatters

@Composable
fun ResumenFinancieroScreen(viewModel: ResumenFinancieroViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Título
        Text(
            text = "📊 Resumen Financiero",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Card Ingresos Totales
        FinancialCard(
            title = "Ingresos Totales",
            amount = uiState.totalIngresos,
            icon = Icons.Default.TrendingUp,
            backgroundColor = Color(0xFF1B5E20),
            textColor = Color(0xFF81C784),
            description = "De todas las ventas"
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Card Egresos - dividido en dos subcategorías
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF4A148C)
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
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

                // Divisor visual
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

        Spacer(modifier = Modifier.height(12.dp))

        // Card Ganancia Neta (Profit)
        FinancialCard(
            title = "Ganancia Neta",
            amount = uiState.gananciaNet,
            icon = Icons.Default.AttachMoney,
            backgroundColor = if (uiState.gananciaNet >= 0) Color(0xFF1B5E20) else Color(0xFF8B0000),
            textColor = if (uiState.gananciaNet >= 0) TealTotal else Danger,
            description = if (uiState.gananciaNet >= 0) "✅ Ganancia" else "⚠️ Pérdida"
        )

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun FinancialCard(
    title: String,
    amount: Double,
    icon: ImageVector,
    backgroundColor: Color,
    textColor: Color,
    description: String
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
                        color = textColor.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(16.dp)
            )
        }
    }
}
