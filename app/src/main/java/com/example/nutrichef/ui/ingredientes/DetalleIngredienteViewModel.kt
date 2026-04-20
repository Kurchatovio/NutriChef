package com.example.nutrichef.ui.ingredientes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nutrichef.datos.entidades.Ingrediente
import com.example.nutrichef.datos.repositorio.NutriChefRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DetalleIngredienteViewModel(
    private val repositorio: NutriChefRepository
) : ViewModel() {

    private val _ingrediente = MutableStateFlow<Ingrediente?>(null)
    val ingrediente: StateFlow<Ingrediente?> = _ingrediente

    private val _nombreMedida = MutableStateFlow("")
    val nombreMedida: StateFlow<String> = _nombreMedida

    fun cargarIngrediente(idIngrediente: Int) {
        viewModelScope.launch {
            val ing = repositorio.obtenerIngredientePorId(idIngrediente)
            _ingrediente.value = ing
            // Cargamos también el nombre de la medida
            ing?.let {
                val medida = repositorio.obtenerMedidaPorId(it.medidaId)
                _nombreMedida.value = medida?.nombre ?: ""
            }
        }
    }

    /**
     * Intenta eliminar el ingrediente.
     *
     * onResultado(exito, estabaEnUso)
     */
    fun eliminarIngrediente(onResultado: (Boolean, Boolean) -> Unit) {
        val ing = ingrediente.value
        if (ing == null) {
            onResultado(false, false)
            return
        }

        viewModelScope.launch {
            try {
                // 1) Consultar si está en uso
                val estaEnUso = repositorio.ingredienteEstaEnUso(ing.id)

                if (estaEnUso) {
                    onResultado(false, true)
                    return@launch
                }

                // 2) Intentar eliminar
                val eliminado = repositorio.eliminarIngredienteSiNoEstaEnUso(ing)

                if (eliminado) {
                    onResultado(true, false)
                } else {
                    onResultado(false, false)
                }

            } catch (e: Exception) {
                onResultado(false, false)
            }
        }
    }
}
