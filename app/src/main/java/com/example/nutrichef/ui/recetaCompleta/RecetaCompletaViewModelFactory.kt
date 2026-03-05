package com.example.nutrichef.ui.recetaCompleta

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.nutrichef.datos.repositorio.NutriChefRepository

/**
 * Factory para crear RecetaCompletaViewModel con el repositorio.
 */
class RecetaCompletaViewModelFactory(
    private val repositorio: NutriChefRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(RecetaCompletaViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RecetaCompletaViewModel(repositorio) as T
        }

        throw IllegalArgumentException("Clase ViewModel desconocida.")
    }
}