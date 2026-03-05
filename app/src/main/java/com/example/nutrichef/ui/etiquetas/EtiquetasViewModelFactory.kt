package com.example.nutrichef.ui.etiquetas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.nutrichef.datos.repositorio.NutriChefRepository

/**
 * Factory para crear EtiquetasViewModel con el repositorio.
 */
class EtiquetasViewModelFactory(
    private val repositorio: NutriChefRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(EtiquetasViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return EtiquetasViewModel(repositorio) as T
        }

        throw IllegalArgumentException("Clase ViewModel desconocida.")
    }
}
