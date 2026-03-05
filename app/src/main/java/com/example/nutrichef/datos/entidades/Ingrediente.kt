package com.example.nutrichef.datos.entidades

import androidx.room.Entity
import androidx.room.PrimaryKey

// Ingredientes utilizados en las recetas
@Entity(tableName = "ingrediente")
data class Ingrediente(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,   // Clave primaria
    val nombre: String,                                 // Ej: harina, leche, huevo, etc.
    val descripcion: String? = null,                    // Descripción opcional
    val medidaId: Int,                                  // Clave foránea hacia tabla Medida
    val proteinasPorMedida: Double? = null,             // Proteínas por unidad base (por cada 100g, etc.)
    val carbohidratosPorMedida: Double? = null,         // Carbohidratos por unidad base (por cada 100g, etc.)
    val grasasPorMedida: Double? = null,                // Grasas por unidad base (por cada 100g, etc.)
    val imagenUri: String? = null                       // Imagen opcional
    )