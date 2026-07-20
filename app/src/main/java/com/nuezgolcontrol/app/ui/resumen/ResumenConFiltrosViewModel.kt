package com.nuezgolcontrol.app.ui.resumen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.nuezgolcontrol.app.data.NuezRepository
import com.nuezgolcontrol.app.util.DateRange
import com.nuezgolcontrol.app.util.DateRangeFilter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

data class ResumenConFiltrosUiState(
    val totalIngresos: Double = 0.0,
    val totalPagosEmpleados: Double = 0.0,
    val totalGastos: Double = 0.0,
    val totalEgresos: Double = 0.0,
    val gananciaNet: Double = 0.0,
    val filtroActual: String = "Todo el tiempo",
    val ventasCount: Int = 0,
    val pagosCount: Int = 0,
    val gastosCount: Int = 0
)

class ResumenConFiltrosViewModel(private val repository: NuezRepository) : ViewModel() {
    
    private val _selectedDateRange = MutableStateFlow(DateRangeFilter.getAllTimeRange())
    val selectedDateRange: StateFlow<DateRange> = _selectedDateRange
    
    private val _filterLabel = MutableStateFlow("Todo el tiempo")
    val filterLabel: StateFlow<String> = _filterLabel

    val uiState: StateFlow<ResumenConFiltrosUiState> = combine(
        repository.ventas,
        repository.pagosTrabajadores,
        repository.gastos,
        _selectedDateRange,
        _filterLabel
    ) { ventas, pagos, gastos, dateRange, label ->
        
        val filteredVentas = ventas.filter { 
            DateRangeFilter.isInRange(it.fecha, dateRange) 
        }
        val filteredPagos = pagos.filter { 
            DateRangeFilter.isInRange(it.fecha, dateRange) 
        }
        val filteredGastos = gastos.filter { 
            DateRangeFilter.isInRange(it.fecha, dateRange) 
        }
        
        val totalIngresos = filteredVentas.sumOf { it.total }
        val totalPagosEmpleados = filteredPagos.sumOf { pago ->
            if (pago.tipoPago == "Kilo") {
                pago.kilos * pago.precioPorKilo
            } else {
                pago.montoRaya
            }
        }
        val totalGastos = filteredGastos.sumOf { it.monto }
        val totalEgresos = totalPagosEmpleados + totalGastos
        val gananciaNet = totalIngresos - totalEgresos

        ResumenConFiltrosUiState(
            totalIngresos = totalIngresos,
            totalPagosEmpleados = totalPagosEmpleados,
            totalGastos = totalGastos,
            totalEgresos = totalEgresos,
            gananciaNet = gananciaNet,
            filtroActual = label,
            ventasCount = filteredVentas.size,
            pagosCount = filteredPagos.size,
            gastosCount = filteredGastos.size
        )
    }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), ResumenConFiltrosUiState())
    
    fun filtrarPorHoy() {
        _selectedDateRange.value = DateRangeFilter.getTodayRange()
        _filterLabel.value = "Hoy"
    }
    
    fun filtrarPorEstaaSemana() {
        _selectedDateRange.value = DateRangeFilter.getThisWeekRange()
        _filterLabel.value = "Esta semana"
    }
    
    fun filtrarPorEsteMes() {
        _selectedDateRange.value = DateRangeFilter.getThisMonthRange()
        _filterLabel.value = "Este mes"
    }
    
    fun filtrarPorUltimosTreintaDias() {
        _selectedDateRange.value = DateRangeFilter.getLastThirtyDaysRange()
        _filterLabel.value = "Últimos 30 días"
    }
    
    fun filtrarTodoElTiempo() {
        _selectedDateRange.value = DateRangeFilter.getAllTimeRange()
        _filterLabel.value = "Todo el tiempo"
    }

    companion object {
        fun factory(repository: NuezRepository) = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return ResumenConFiltrosViewModel(repository) as T
            }
        }
    }
}
