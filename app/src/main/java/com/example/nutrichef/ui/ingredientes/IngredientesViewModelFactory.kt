package com.example.nutrichef.ui.ingredientes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.nutrichef.datos.repositorio.NutriChefRepository

/**
 * Factory utilizado por Android para crear un IngredientesViewModel
 * con parámetros personalizados, en este caso el Repository.
 */
class IngredientesViewModelFactory(
    private val repositorio: NutriChefRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(IngredientesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return IngredientesViewModel(repositorio) as T
        }

        throw IllegalArgumentException("Clase ViewModel desconocida: ${modelClass.name}")
    }
}
