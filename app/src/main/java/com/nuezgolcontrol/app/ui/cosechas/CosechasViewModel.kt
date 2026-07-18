package com.nuezgolcontrol.app.ui.cosechas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.nuezgolcontrol.app.data.Cosecha
import com.nuezgolcontrol.app.data.NuezRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class CosechasUiState(
    val cosechas: List<Cosecha> = emptyList(),
    val totalKg: Double = 0.0
)

class CosechasViewModel(private val repository: NuezRepository) : ViewModel() {

    val uiState: StateFlow<CosechasUiState> = repository.cosechas
        .map { list ->
            CosechasUiState(
                cosechas = list,
                totalKg = list.sumOf { it.cantidadKg }
            )
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), CosechasUiState())

    fun eliminar(id: Long) {
        viewModelScope.launch { repository.eliminarCosecha(id) }
    }

    fun agregar(
        tipoNuez: String,
        cantidadKg: Double,
        onDone: () -> Unit,
        onError: (String) -> Unit
    ) {
        if (tipoNuez.isBlank()) {
            onError("Debes especificar el tipo de nuez")
            return
        }
        if (cantidadKg <= 0) {
            onError("La cantidad debe ser mayor que cero")
            return
        }
        viewModelScope.launch {
            repository.agregarCosecha(tipoNuez, cantidadKg)
            onDone()
        }
    }

    suspend fun listarParaExportar(): List<Cosecha> = repository.obtenerCosechas()

    companion object {
        fun factory(repository: NuezRepository) = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return CosechasViewModel(repository) as T
            }
        }
    }
}
