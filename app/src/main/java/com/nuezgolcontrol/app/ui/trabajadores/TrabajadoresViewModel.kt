package com.nuezgolcontrol.app.ui.trabajadores

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.nuezgolcontrol.app.data.Gasto
import com.nuezgolcontrol.app.data.NuezRepository
import com.nuezgolcontrol.app.data.PagoTrabajador
import com.nuezgolcontrol.app.data.Trabajador
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TrabajadoresViewModel(private val repository: NuezRepository) : ViewModel() {

    val pagos: StateFlow<List<PagoTrabajador>> = repository.pagosTrabajadores
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val trabajadoresGuardados: StateFlow<List<Trabajador>> = repository.trabajadores
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val gastos: StateFlow<List<Gasto>> = repository.gastos
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // --- Pagos ---
    fun agregarPago(
        trabajadorNombre: String,
        tipoPago: String,
        kilos: Double,
        precioPorKilo: Double,
        montoRaya: Double
    ) {
        viewModelScope.launch {
            repository.agregarPagoTrabajador(trabajadorNombre, tipoPago, kilos, precioPorKilo, montoRaya)
            // El nombre queda guardado automáticamente en el repository
        }
    }

    fun eliminarPago(id: Long) {
        viewModelScope.launch { repository.eliminarPagoTrabajador(id) }
    }

    // --- Trabajadores (nombres guardados) ---
    fun eliminarTrabajador(nombre: String) {
        viewModelScope.launch { repository.eliminarTrabajador(nombre) }
    }

    // --- Gastos ---
    fun agregarGasto(concepto: String, monto: Double) {
        viewModelScope.launch { repository.agregarGasto(concepto, monto) }
    }

    fun eliminarGasto(id: Long) {
        viewModelScope.launch { repository.eliminarGasto(id) }
    }

    companion object {
        fun factory(repository: NuezRepository): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T =
                    TrabajadoresViewModel(repository) as T
            }
    }
}
