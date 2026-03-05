package com.example.nutrichef.ui.pasos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.nutrichef.datos.repositorio.NutriChefRepository

/**
 * Factory para crear PasoRecetaViewModel con el repositorio.
 */
class PasoRecetaViewModelFactory(
    private val repositorio: NutriChefRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(PasoRecetaViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PasoRecetaViewModel(repositorio) as T
        }

        throw IllegalArgumentException("Clase ViewModel desconocida.")
    }
}
