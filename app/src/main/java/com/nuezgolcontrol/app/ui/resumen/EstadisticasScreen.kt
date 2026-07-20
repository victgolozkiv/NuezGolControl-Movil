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
            .background(Color.Black)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
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
                    text = "📊 Estadísticas por Producto",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${uiState.ventasTotales} transacciones registradas",
                    fontSize = 12.sp,
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
                if (uiState.productoMasVendido != null) {
                    DestacadoCard(
                        titulo = "🏆 Más Vendido",
                        producto = uiState.productoMasVendido.tipoNuez,
                        valor = "${Formatters.numero(uiState.productoMasVendido.cantidadTotal)} kg",
                        color = Color(0xFF4CAF50),
                        modifier = Modifier.weight(1f)
                    )
                }

                // Más lucrativo
                if (uiState.productoMasLucrador != null) {
                    DestacadoCard(
                        titulo = "💰 Más Lucrativo",
                        producto = uiState.productoMasLucrador.tipoNuez,
                        valor = Formatters.dinero(uiState.productoMasLucrador.ingresoTotal),
                        color = Color(0xFFFFB74D),
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

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
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .height(120.dp)
            .animateContentSize(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = color.copy(alpha = 0.2f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = titulo,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = color
            )
            Column {
                Text(
                    text = producto,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = valor,
                    fontSize = 16.sp,
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
            .animateContentSize(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1A1A1A)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
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
                                color = TealTotal.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(50.dp)
                            )
                            .padding(8.dp)
                    )
                    Spacer(modifier = Modifier.padding(horizontal = 8.dp))
                    Text(
                        text = estadistica.tipoNuez,
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
                    .background(Color(0xFF2A2A2A))
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
                        color = Color(0xFFAAAAAA)
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
                        text = "# Ventas",
                        fontSize = 11.sp,
                        color = Color(0xFFAAAAAA)
                    )
                    Text(
                        text = estadistica.ventasCount.toString(),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF81C784)
                    )
                }
                Column {
                    Text(
                        text = "Precio Promedio",
                        fontSize = 11.sp,
                        color = Color(0xFFAAAAAA)
                    )
                    Text(
                        text = Formatters.dinero(estadistica.precioPromedio),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFFB74D)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Fila 2: Ingreso Total
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Color(0xFF1B5E20).copy(alpha = 0.3f),
                        shape = RoundedCornerShape(8.dp)
                    )
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
                        text = "Ingreso Total",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF81C784)
                    )
                }
                Text(
                    text = Formatters.dinero(estadistica.ingresoTotal),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF81C784)
                )
            }
        }
    }
}
