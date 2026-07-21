package com.nuezgolcontrol.app.ui.resumen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.nuezgolcontrol.app.data.NuezRepository
import com.nuezgolcontrol.app.data.Venta
import com.nuezgolcontrol.app.data.PagoTrabajador
import com.nuezgolcontrol.app.data.Gasto
import com.nuezgolcontrol.app.data.Cosecha
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

data class ResumenFinancieroUiState(
    val totalIngresos: Double = 0.0,
    val totalPagosEmpleados: Double = 0.0,
    val totalGastos: Double = 0.0,
    val totalEgresos: Double = 0.0,
    val gananciaNet: Double = 0.0,
    val ventas: List<Venta> = emptyList(),
    val pagos: List<PagoTrabajador> = emptyList(),
    val gastos: List<Gasto> = emptyList(),
    val cosechas: List<Cosecha> = emptyList()
)

class ResumenFinancieroViewModel(private val repository: NuezRepository) : ViewModel() {

    val uiState: StateFlow<ResumenFinancieroUiState> = combine(
        repository.ventas,
        repository.pagosTrabajadores,
        repository.gastos,
        repository.cosechas
    ) { ventas, pagos, gastos, cosechas ->
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
            gananciaNet = gananciaNet,
            ventas = ventas,
            pagos = pagos,
            gastos = gastos,
            cosechas = cosechas
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
