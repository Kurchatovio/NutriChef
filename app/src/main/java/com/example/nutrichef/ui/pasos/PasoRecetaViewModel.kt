package com.example.nutrichef.ui.pasos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nutrichef.datos.entidades.PasoReceta
import com.example.nutrichef.datos.repositorio.NutriChefRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel encargado de la gestión de los pasos de receta.
 *
 * Permite cargar, añadir, actualizar y eliminar pasos, siempre ordenados,
 * usando el repositorio para comunicarse con la base de datos.
 */
class PasoRecetaViewModel(
    private val repositorio: NutriChefRepository
) : ViewModel() {

    // -------------------------------------------------------------
    //                   ESTADO OBSERVABLE PARA LA UI
    // -------------------------------------------------------------
    private val _pasos = MutableStateFlow<List<PasoReceta>>(emptyList())
    val pasos: StateFlow<List<PasoReceta>> = _pasos


    // -------------------------------------------------------------
    //                        FUNCIONES CRUD
    // -------------------------------------------------------------

    /** Carga los pasos asociados a una receta, ordenados por 'orden'. */
    fun cargarPasosDeReceta(idReceta: Int) {
        viewModelScope.launch {
            _pasos.value = repositorio.obtenerPasosPorReceta(idReceta)
        }
    }

    /** Inserta un paso nuevo. */
    fun insertarPaso(paso: PasoReceta) {
        viewModelScope.launch {
            repositorio.insertarPaso(paso)
            cargarPasosDeReceta(paso.recetaId)
        }
    }

    /** Actualiza un paso existente. */
    fun actualizarPaso(paso: PasoReceta) {
        viewModelScope.launch {
            repositorio.actualizarPaso(paso)
            cargarPasosDeReceta(paso.recetaId)
        }
    }

    /** Elimina un paso y recarga la lista. */
    fun eliminarPaso(paso: PasoReceta) {
        viewModelScope.launch {
            repositorio.eliminarPaso(paso)
            cargarPasosDeReceta(paso.recetaId)
        }
    }
}
