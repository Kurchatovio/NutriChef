package com.example.nutrichef.ui.listaRecetas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nutrichef.datos.repositorio.NutriChefRepository
import com.example.nutrichef.datos.entidades.Receta
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel responsable de gestionar la lógica de la pantalla de recetas.
 * La UI observa los datos que expone este ViewModel (StateFlow)
 * y ejecuta acciones llamando a sus funciones.
 */
class RecetasViewModel(
    private val repositorio: NutriChefRepository
) : ViewModel() {

    // ------------------- ESTADO EXPUESTO A LA UI -------------------

    // Lista de recetas observable
    private val _recetas = MutableStateFlow<List<Receta>>(emptyList())
    val recetas: StateFlow<List<Receta>> = _recetas


    // -------------------------- FUNCIONES --------------------------

    /** Carga todas las recetas **/
    fun cargarRecetas() {
        viewModelScope.launch {
            _recetas.value = repositorio.obtenerTodasLasRecetas()
        }
    }

    /** Busca recetas por nombre (coincidencia parcial) **/
    fun buscarRecetas(texto: String) {
        viewModelScope.launch {
            _recetas.value = repositorio.buscarRecetasPorNombre(texto)
        }
    }

    /** Inserta una receta nueva **/
    fun insertarReceta(receta: Receta) {
        viewModelScope.launch {
            repositorio.insertarReceta(receta)
            cargarRecetas()
        }
    }

    /** Actualiza una receta **/
    fun actualizarReceta(receta: Receta) {
        viewModelScope.launch {
            repositorio.actualizarReceta(receta)
            cargarRecetas()
        }
    }

    /** Elimina una receta **/
    fun eliminarReceta(receta: Receta) {
        viewModelScope.launch {
            repositorio.eliminarReceta(receta)
            cargarRecetas()
        }
    }
}
