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
    val unidad: String,
    // Macros opcionales del ingrediente
    val proteinasPorMedida: Double?,
    val carbohidratosPorMedida: Double?,
    val grasasPorMedida: Double?
)

data class ResumenNutricional(
    val proteinas: Double,
    val carbohidratos: Double,
    val grasas: Double,
    val completo: Boolean // false si algún ingrediente no tiene macros
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

    private val _resumenNutricional = MutableStateFlow<ResumenNutricional?>(null)
    val resumenNutricional: StateFlow<ResumenNutricional?> = _resumenNutricional

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
                    unidad = medida?.nombre ?: "",
                    proteinasPorMedida = ingrediente?.proteinasPorMedida,
                    carbohidratosPorMedida = ingrediente?.carbohidratosPorMedida,
                    grasasPorMedida = ingrediente?.grasasPorMedida
                )
            }

            _ingredientes.value = lista

            // Calcular resumen nutricional
            val completo = lista.all {
                it.proteinasPorMedida != null &&
                        it.carbohidratosPorMedida != null &&
                        it.grasasPorMedida != null
            }

            _resumenNutricional.value = ResumenNutricional(
                proteinas = lista.sumOf { (it.proteinasPorMedida ?: 0.0) * it.cantidad / 100.0 },
                carbohidratos = lista.sumOf { (it.carbohidratosPorMedida ?: 0.0) * it.cantidad / 100.0 },
                grasas = lista.sumOf { (it.grasasPorMedida ?: 0.0) * it.cantidad / 100.0 },
                completo = completo
            )
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
