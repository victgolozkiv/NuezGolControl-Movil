package com.nuezgolcontrol.app.ui.resumen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.nuezgolcontrol.app.data.NuezRepository
import com.nuezgolcontrol.app.data.Venta
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

data class EstadisticaProducto(
    val tipoNuez: String,
    val cantidadTotal: Double,
    val ventasCount: Int,
    val ingresoTotal: Double,
    val precioPromedio: Double
)

data class EstadisticasUiState(
    val estadisticas: List<EstadisticaProducto> = emptyList(),
    val productoMasVendido: EstadisticaProducto? = null,
    val productoMasLucrador: EstadisticaProducto? = null,
    val ventasTotales: Int = 0
)

class EstadisticasViewModel(private val repository: NuezRepository) : ViewModel() {

    val uiState: StateFlow<EstadisticasUiState> = repository.ventas
        .map { ventas ->
            val agrupadas = ventas.groupBy { it.tipoNuez }
            
            val estadisticas = agrupadas.map { (tipoNuez, listVentas) ->
                EstadisticaProducto(
                    tipoNuez = tipoNuez,
                    cantidadTotal = listVentas.sumOf { it.cantidad },
                    ventasCount = listVentas.size,
                    ingresoTotal = listVentas.sumOf { it.total },
                    precioPromedio = if (listVentas.isNotEmpty()) 
                        listVentas.map { it.precioUnitario }.average() 
                    else 0.0
                )
            }.sortedByDescending { it.ingresoTotal }
            
            val productoMasVendido = estadisticas.maxByOrNull { it.cantidadTotal }
            val productoMasLucrador = estadisticas.maxByOrNull { it.ingresoTotal }
            
            EstadisticasUiState(
                estadisticas = estadisticas,
                productoMasVendido = productoMasVendido,
                productoMasLucrador = productoMasLucrador,
                ventasTotales = ventas.size
            )
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), EstadisticasUiState())

    companion object {
        fun factory(repository: NuezRepository) = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return EstadisticasViewModel(repository) as T
            }
        }
    }
}
