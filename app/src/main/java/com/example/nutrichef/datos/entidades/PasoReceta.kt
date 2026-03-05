package com.example.nutrichef.datos.entidades

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

// Relación entre una receta y sus pasos de preparación en una receta (de uno a muchos)
@Entity(
    tableName = "pasoReceta",
    foreignKeys = [
        ForeignKey(
            entity = Receta::class,
            parentColumns = ["id"],
            childColumns = ["recetaId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class PasoReceta(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,   // Clave primaria propia
    val recetaId: Int,                                  // Clave foránea hacia la tabla Receta
    val orden: Int,                                     // Número de orden del paso (1, 2, 3, etc.)
    val descripcion: String,                            // Contenido de dicho paso
    val tiempoMin: Int? = null                          // Tiempo opcional (útil para cálculo automático total)
)
