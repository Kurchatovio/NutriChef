package com.example.nutrichef.ui.etiquetas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nutrichef.datos.entidades.Etiqueta
import com.example.nutrichef.datos.repositorio.NutriChefRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel encargado de gestionar las etiquetas.
 *
 * Proporciona estado observable para la interfaz y funciones CRUD
 * comunicándose exclusivamente con el repositorio.
 */
class EtiquetasViewModel(
    private val repositorio: NutriChefRepository
) : ViewModel() {

    // -------------------------------------------------------------
    //                   ESTADO OBSERVABLE PARA LA UI
    // -------------------------------------------------------------
    private val _etiquetas = MutableStateFlow<List<Etiqueta>>(emptyList())
    val etiquetas: StateFlow<List<Etiqueta>> = _etiquetas


    // -------------------------------------------------------------
    //                           FUNCIONES CRUD
    // -------------------------------------------------------------

    /** Carga todas las etiquetas. */
    fun cargarEtiquetas() {
        viewModelScope.launch {
            _etiquetas.value = repositorio.obtenerTodasLasEtiquetas()
        }
    }

    /** Busca etiquetas por nombre. */
    fun buscarEtiquetas(texto: String) {
        viewModelScope.launch {
            _etiquetas.value = repositorio.buscarEtiquetasPorNombre(texto)
        }
    }

    /** Inserta una etiqueta nueva. */
    fun insertarEtiqueta(etiqueta: Etiqueta) {
        viewModelScope.launch {
            repositorio.insertarEtiqueta(etiqueta)
            cargarEtiquetas()
        }
    }

    /** Actualiza una etiqueta existente. */
    fun actualizarEtiqueta(etiqueta: Etiqueta) {
        viewModelScope.launch {
            repositorio.actualizarEtiqueta(etiqueta)
            cargarEtiquetas()
        }
    }

    /** Elimina una etiqueta. */
    fun eliminarEtiqueta(etiqueta: Etiqueta) {
        viewModelScope.launch {
            repositorio.eliminarEtiqueta(etiqueta)
            cargarEtiquetas()
        }
    }
}
