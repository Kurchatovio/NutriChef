package com.example.nutrichef.ui.listaRecetas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.nutrichef.datos.repositorio.NutriChefRepository

/**
 * Factory necesario para crear RecetasViewModel pasándole el Repository.
 * Android no puede crear ViewModels con parámetros sin esta fábrica.
 */
class RecetasViewModelFactory(
    private val repositorio: NutriChefRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RecetasViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RecetasViewModel(repositorio) as T
        }
        throw IllegalArgumentException("Clase ViewModel desconocida.")
    }
}
