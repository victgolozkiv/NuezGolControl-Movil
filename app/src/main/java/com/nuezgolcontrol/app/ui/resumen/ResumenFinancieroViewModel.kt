package com.nuezgolcontrol.app.ui.resumen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.nuezgolcontrol.app.data.NuezRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

data class ResumenFinancieroUiState(
    val totalIngresos: Double = 0.0,
    val totalPagosEmpleados: Double = 0.0,
    val totalGastos: Double = 0.0,
    val totalEgresos: Double = 0.0,
    val gananciaNet: Double = 0.0
)

class ResumenFinancieroViewModel(private val repository: NuezRepository) : ViewModel() {

    val uiState: StateFlow<ResumenFinancieroUiState> = combine(
        repository.ventas,
        repository.pagosTrabajadores,
        repository.gastos
    ) { ventas, pagos, gastos ->
        val totalIngresos = ventas.sumOf { it.total }
        val totalPagosEmpleados = pagos.sumOf { pago ->
            if (pago.tipoPago == "Kilo") {
                pago.kilos * pago.precioPorKilo
            } else {
                pago.montoRaya
            }
        }
        val totalGastos = gastos.sumOf { it.monto }
        val totalEgresos = totalPagosEmpleados + totalGastos
        val gananciaNet = totalIngresos - totalEgresos

        ResumenFinancieroUiState(
            totalIngresos = totalIngresos,
            totalPagosEmpleados = totalPagosEmpleados,
            totalGastos = totalGastos,
            totalEgresos = totalEgresos,
            gananciaNet = gananciaNet
        )
    }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), ResumenFinancieroUiState())

    companion object {
        fun factory(repository: NuezRepository) = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return ResumenFinancieroViewModel(repository) as T
            }
        }
    }
}
