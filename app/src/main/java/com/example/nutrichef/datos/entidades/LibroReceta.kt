package com.example.nutrichef.datos.entidades

import androidx.room.Entity
import androidx.room.ForeignKey

// Relación un libro y sus recetas (muchos a muchos)
@Entity(
    tableName = "libroReceta",
    primaryKeys = ["libroId", "recetaId"],
    foreignKeys = [
        ForeignKey(
            entity = Libro::class,
            parentColumns = ["id"],
            childColumns = ["libroId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Receta::class,
            parentColumns = ["id"],
            childColumns = ["recetaId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class LibroReceta(
    val libroId: Int,      // Clave foránea hacia la tabla Libro
    val recetaId: Int      // Clave foránea hacia la tabla Receta
)
