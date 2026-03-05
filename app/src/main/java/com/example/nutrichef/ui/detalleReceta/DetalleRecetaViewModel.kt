package com.example.nutrichef.ui.detalleReceta

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nutrichef.datos.entidades.PasoReceta
import com.example.nutrichef.datos.entidades.Receta
import com.example.nutrichef.datos.repositorio.NutriChefRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class IngredienteEnRecetaUi(
    val nombre: String,
    val cantidad: Double,
    val unidad: String
)

class DetalleRecetaViewModel(
    private val repositorio: NutriChefRepository
) : ViewModel() {

    private val _receta = MutableStateFlow<Receta?>(null)
    val receta: StateFlow<Receta?> = _receta

    private val _ingredientes = MutableStateFlow<List<IngredienteEnRecetaUi>>(emptyList())
    val ingredientes: StateFlow<List<IngredienteEnRecetaUi>> = _ingredientes

    private val _pasos = MutableStateFlow<List<PasoReceta>>(emptyList())
    val pasos: StateFlow<List<PasoReceta>> = _pasos

    private val _eliminacionOk = MutableStateFlow<Boolean?>(null)
    val eliminacionOk: StateFlow<Boolean?> = _eliminacionOk

    // ---------------------------------------------------------
    // RECETA
    // ---------------------------------------------------------
    fun cargarReceta(id: Int) {
        viewModelScope.launch {
            _receta.value = repositorio.obtenerRecetaPorId(id)
        }
    }

    // ---------------------------------------------------------
    // INGREDIENTES
    // ---------------------------------------------------------
    fun cargarIngredientes(idReceta: Int) {
        viewModelScope.launch {

            val relaciones = repositorio.obtenerIngredientesDeReceta(idReceta)

            val lista = relaciones.map { ri ->

                val ingrediente = repositorio.obtenerIngredientePorId(ri.ingredienteId)
                val medidaId = ri.medidaId ?: ingrediente?.medidaId
                val medida = medidaId?.let { repositorio.obtenerMedidaPorId(it) }

                IngredienteEnRecetaUi(
                    nombre = ingrediente?.nombre ?: "Ingrediente ${ri.ingredienteId}",
                    cantidad = ri.cantidad,
                    unidad = medida?.nombre ?: ""
                )
            }

            _ingredientes.value = lista
        }
    }

    // ---------------------------------------------------------
    // PASOS
    // ---------------------------------------------------------
    fun cargarPasos(idReceta: Int) {
        viewModelScope.launch {
            _pasos.value = repositorio.obtenerPasosPorReceta(idReceta)
        }
    }

    // ---------------------------------------------------------
    // ELIMINAR
    // ---------------------------------------------------------
    fun borrarRecetaCompleta(idReceta: Int) {
        viewModelScope.launch {
            try {
                repositorio.borrarRecetaCompleta(idReceta)
                _eliminacionOk.value = true
            } catch (e: Exception) {
                e.printStackTrace()
                _eliminacionOk.value = false
            }
        }
    }

    fun resetEliminarEstado() {
        _eliminacionOk.value = null
    }
}
