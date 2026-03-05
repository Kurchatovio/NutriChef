package com.example.nutrichef.datos.entidades

import androidx.room.Entity
import androidx.room.ForeignKey

// Relación entre una receta y sus ingredientes (de muchos a muchos)
@Entity(
    tableName = "recetaIngrediente",
    primaryKeys = ["recetaId", "ingredienteId"], // clave compuesta

    foreignKeys = [
        ForeignKey(
            entity = Receta::class,
            parentColumns = ["id"],
            childColumns = ["recetaId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Ingrediente::class,
            parentColumns = ["id"],
            childColumns = ["ingredienteId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class RecetaIngrediente(
    val recetaId: Int,               // Clave foránea hacia la tabla Receta
    val ingredienteId: Int,          // Clave foránea hacia la tabla Ingrediente
    val cantidad: Double,            // Cantidad usada en la receta
    val medidaId: Int? = null        // Medida específica (opcional, si es diferente a la de la base del ingrediente)
)
