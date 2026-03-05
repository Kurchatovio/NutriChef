package com.example.nutrichef.ui.libros

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.nutrichef.datos.repositorio.NutriChefRepository

/**
 * Factory para crear LibrosViewModel con su repositorio.
 */
class LibrosViewModelFactory(
    private val repositorio: NutriChefRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LibrosViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LibrosViewModel(repositorio) as T
        }
        throw IllegalArgumentException("Clase ViewModel desconocida.")
    }
}
