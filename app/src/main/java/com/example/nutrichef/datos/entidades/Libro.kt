package com.example.nutrichef.datos.entidades

import androidx.room.Entity
import androidx.room.PrimaryKey

// Colección de recetas o "librode recetas" creado por el usuario
@Entity(tableName = "libro")
data class Libro(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,   // Clave primaria
    val nombre: String,                                 // Nombre del libro (Ej:carnes, pescados, Navidadeñas, etc.)
    val descripcion: String? = null,                    // Descripción opcional
    val imagenUri: String? = null                       // Imagen opcional
)
