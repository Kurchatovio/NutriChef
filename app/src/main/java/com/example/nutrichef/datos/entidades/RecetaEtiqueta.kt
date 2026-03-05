package com.example.nutrichef.datos.entidades

import androidx.room.Entity
import androidx.room.ForeignKey

// Relación entre una receta y sus etiquetas (muchos a muchos)
@Entity(
    tableName = "recetaEtiqueta",
    primaryKeys = ["recetaId", "etiquetaId"],
    foreignKeys = [
        ForeignKey(
            entity = Receta::class,
            parentColumns = ["id"],
            childColumns = ["recetaId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Etiqueta::class,
            parentColumns = ["id"],
            childColumns = ["etiquetaId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class RecetaEtiqueta(
    val recetaId: Int,       // Clave foránea hacia la tabla Receta
    val etiquetaId: Int      // Clave foránea hacia la tabla Etiqueta
)
