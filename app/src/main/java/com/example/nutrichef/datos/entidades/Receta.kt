package com.example.nutrichef.datos.entidades

import androidx.room.Entity
import androidx.room.PrimaryKey

// Recetas creadas por el usuario o precargadas
@Entity(tableName = "receta")
data class Receta(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,   // Clave primaria
    val nombre: String,                                 // Nombre de la receta
    val descripcion: String? = null,                    // Descripción opcional
    val categoria: String? = null,                      // Ej: desayuno, almuerzo, postre, etc.
    val porciones: Int? = null,                         // Número de raciones (opcional)
    val tiempoPreparacionMin: Int? = null,              // Tiempo de preparación
    val imagenUri: String? = null                       // Imagen opcional
)
