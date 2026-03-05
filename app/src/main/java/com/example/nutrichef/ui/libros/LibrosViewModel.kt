package com.example.nutrichef.ui.libros

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nutrichef.datos.entidades.Libro
import com.example.nutrichef.datos.entidades.LibroReceta
import com.example.nutrichef.datos.repositorio.NutriChefRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel encargado de gestionar los libros de recetas ("cookbooks").
 *
 * Gestiona:
 *  - CRUD de Libros
 *  - Búsqueda
 *  - Relación Libro–Receta (asignar recetas a un libro)
 */
class LibrosViewModel(
    private val repositorio: NutriChefRepository
) : ViewModel() {

    // -------------------------------------------------------------
    //                   ESTADO OBSERVABLE PARA LA UI
    // -------------------------------------------------------------

    private val _libros = MutableStateFlow<List<Libro>>(emptyList())
    val libros: StateFlow<List<Libro>> = _libros

    private val _recetasDeLibro = MutableStateFlow<List<LibroReceta>>(emptyList())
    val recetasDeLibro: StateFlow<List<LibroReceta>> = _recetasDeLibro


    // -------------------------------------------------------------
    //                          LIBROS (CRUD)
    // -------------------------------------------------------------

    /** Carga todos los libros. */
    fun cargarLibros() {
        viewModelScope.launch {
            _libros.value = repositorio.obtenerTodosLosLibros()
        }
    }

    /** Busca libros cuyo nombre coincida parcialmente. */
    fun buscarLibros(texto: String) {
        viewModelScope.launch {
            _libros.value = repositorio.buscarLibrosPorNombre(texto)
        }
    }

    /** Inserta un libro nuevo. */
    fun insertarLibro(libro: Libro) {
        viewModelScope.launch {
            repositorio.insertarLibro(libro)
            cargarLibros()
        }
    }

    /** Actualiza un libro existente. */
    fun actualizarLibro(libro: Libro) {
        viewModelScope.launch {
            repositorio.actualizarLibro(libro)
            cargarLibros()
        }
    }

    /** Elimina un libro y recarga la lista. */
    fun eliminarLibro(libro: Libro) {
        viewModelScope.launch {
            repositorio.eliminarLibro(libro)
            cargarLibros()
        }
    }


    // -------------------------------------------------------------
    //              RELACIÓN LIBRO – RECETA (tabla puente)
    // -------------------------------------------------------------

    /** Carga todas las relaciones libro–receta para un libro concreto. */
    fun cargarRecetasDeLibro(idLibro: Int) {
        viewModelScope.launch {
            _recetasDeLibro.value = repositorio.obtenerRelacionesLibroRecetaPorLibro(idLibro)
        }
    }

    /** Añade una receta a un libro. */
    fun añadirRecetaALibro(relacion: LibroReceta) {
        viewModelScope.launch {
            repositorio.insertarRelacionLibroReceta(relacion)
            cargarRecetasDeLibro(relacion.libroId)
        }
    }

    /** Elimina una receta del libro. */
    fun eliminarRecetaDeLibro(relacion: LibroReceta) {
        viewModelScope.launch {
            repositorio.eliminarRelacionLibroReceta(relacion)
            cargarRecetasDeLibro(relacion.libroId)
        }
    }
}
