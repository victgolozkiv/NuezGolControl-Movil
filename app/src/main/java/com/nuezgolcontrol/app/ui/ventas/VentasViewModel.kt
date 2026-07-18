package com.nuezgolcontrol.app.ui.ventas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.nuezgolcontrol.app.data.NuezRepository
import com.nuezgolcontrol.app.data.Venta
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class VentasUiState(
    val ventas: List<Venta> = emptyList(),
    val totalGeneral: Double = 0.0
)

class VentasViewModel(private val repository: NuezRepository) : ViewModel() {

    val uiState: StateFlow<VentasUiState> = repository.ventas
        .map { list ->
            VentasUiState(
                ventas = list,
                totalGeneral = list.sumOf { it.total }
            )
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), VentasUiState())

    fun eliminar(id: Long) {
        viewModelScope.launch { repository.eliminarVenta(id) }
    }

    fun agregar(
        cliente: String,
        tipoNuez: String,
        cantidad: Double,
        precio: Double,
        onDone: () -> Unit,
        onError: (String) -> Unit
    ) {
        if (cliente.isBlank()) {
            onError("El cliente es obligatorio")
            return
        }
        if (tipoNuez.isBlank()) {
            onError("Selecciona el tipo de nuez")
            return
        }
        if (cantidad <= 0 || precio <= 0) {
            onError("Cantidad y precio deben ser mayores que cero")
            return
        }
        viewModelScope.launch {
            repository.agregarVenta(cliente, tipoNuez, cantidad, precio)
            onDone()
        }
    }

    suspend fun listarParaExportar(): List<Venta> = repository.obtenerVentas()

    companion object {
        fun factory(repository: NuezRepository) = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return VentasViewModel(repository) as T
            }
        }
    }
}
