package com.example.nutrichef.datos.entidades

import androidx.room.Entity
import androidx.room.PrimaryKey

// Etiquetas o "tags" para asignarse a recetas (Ej: saludable, rápido, dulce, etc.)
@Entity(tableName = "etiqueta")
data class Etiqueta(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,   // Clave primaria
    val nombre: String                                  // Nombre de la etiqueta
)
