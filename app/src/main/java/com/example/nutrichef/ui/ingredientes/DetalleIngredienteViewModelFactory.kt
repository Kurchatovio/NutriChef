package com.example.nutrichef.ui.ingredientes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.nutrichef.datos.repositorio.NutriChefRepository
import com.example.nutrichef.ui.ingredientes.DetalleIngredienteViewModel

class DetalleIngredienteViewModelFactory(
    private val repositorio: NutriChefRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetalleIngredienteViewModel::class.java)) {
            return DetalleIngredienteViewModel(repositorio) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
