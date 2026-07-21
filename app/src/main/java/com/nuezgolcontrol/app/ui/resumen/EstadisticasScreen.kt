package com.nuezgolcontrol.app.ui.resumen

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.ShoppingCart
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nuezgolcontrol.app.ui.theme.PurplePrimary
import com.nuezgolcontrol.app.ui.theme.TealTotal
import com.nuezgolcontrol.app.util.Formatters

@Composable
fun EstadisticasScreen(viewModel: EstadisticasViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            // Encabezado
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = PurplePrimary.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .border(1.dp, PurplePrimary.copy(alpha = 0.25f), RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Text(
                    text = "📊 Rendimiento por Variedad de Nuez",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${uiState.ventasTotales} transacciones de venta registradas",
                    fontSize = 13.sp,
                    color = TealTotal,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        // Cards de Destacados
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Más vendido
                val masVendido = uiState.productoMasVendido
                if (masVendido != null) {
                    DestacadoCard(
                        titulo = "🏆 Más Vendido",
                        producto = masVendido.tipoNuez,
                        valor = "${Formatters.numero(masVendido.cantidadTotal)} kg",
                        color = Color(0xFF81C784),
                        borderColor = Color(0xFF81C784).copy(alpha = 0.35f),
                        backgroundColor = Color(0xFF1B5E20).copy(alpha = 0.2f),
                        modifier = Modifier.weight(1f)
                    )
                }

                // Más lucrativo
                val masLucrador = uiState.productoMasLucrador
                if (masLucrador != null) {
                    DestacadoCard(
                        titulo = "💰 Más Lucrativo",
                        producto = masLucrador.tipoNuez,
                        valor = Formatters.dinero(masLucrador.ingresoTotal),
                        color = Color(0xFFFFB74D),
                        borderColor = Color(0xFFFFB74D).copy(alpha = 0.35f),
                        backgroundColor = Color(0xFFE65100).copy(alpha = 0.15f),
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // Lista de productos
        items(uiState.estadisticas) { estadistica ->
            ProductoEstadisticaCard(estadistica)
        }

        item {
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun DestacadoCard(
    titulo: String,
    producto: String,
    valor: String,
    color: Color,
    borderColor: Color,
    backgroundColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .height(130.dp)
            .animateContentSize()
            .border(1.dp, borderColor, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = titulo,
                fontSize = 11.sp,
                fontWeight = FontWeight.ExtraBold,
                color = color,
                letterSpacing = 0.5.sp
            )
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    text = producto.replaceFirstChar { it.uppercase() },
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = valor,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = color
                )
            }
        }
    }
}

@Composable
fun ProductoEstadisticaCard(
    estadistica: EstadisticaProducto
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize()
            .border(1.dp, Color(0xFF222222), RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF0F0F0F)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Encabezado
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
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = null,
                        tint = TealTotal,
                        modifier = Modifier
                            .background(
                                color = TealTotal.copy(alpha = 0.12f),
                                shape = RoundedCornerShape(50.dp)
                            )
                            .padding(8.dp)
                    )
                    Spacer(modifier = Modifier.padding(horizontal = 8.dp))
                    Text(
                        text = estadistica.tipoNuez.replaceFirstChar { it.uppercase() },
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            // Dividir
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Color(0xFF222222))
            )

            // Fila 1: Cantidad y # ventas
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Cantidad Total",
                        fontSize = 11.sp,
                        color = Color(0xFF888888)
                    )
                    Text(
                        text = Formatters.kg(estadistica.cantidadTotal),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = TealTotal
                    )
                }
                Column {
                    Text(
                        text = "Ventas",
                        fontSize = 11.sp,
                        color = Color(0xFF888888)
                    )
                    Text(
                        text = "${estadistica.ventasCount} trans.",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF81C784)
                    )
                }
                Column {
                    Text(
                        text = "Precio Promedio",
                        fontSize = 11.sp,
                        color = Color(0xFF888888)
                    )
                    Text(
                        text = Formatters.dinero(estadistica.precioPromedio),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFFB74D)
                    )
                }
            }

            // Fila 2: Ingreso Total
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Color(0xFF1B5E20).copy(alpha = 0.15f),
                        shape = RoundedCornerShape(10.dp)
                    )
                    .border(1.dp, Color(0xFF81C784).copy(alpha = 0.25f), RoundedCornerShape(10.dp))
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.TrendingUp,
                        contentDescription = null,
                        tint = Color(0xFF81C784),
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(
                        text = "Ingreso Total Acumulado",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF81C784)
                    )
                }
                Text(
                    text = Formatters.dinero(estadistica.ingresoTotal),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF81C784)
                )
            }
        }
    }
}
