package com.example.nutrichef.datos.entidades

import androidx.room.Entity
import androidx.room.PrimaryKey

//Unidades de medida (gramo, litro, unidad, etc.)
@Entity(tableName = "medida")
data class Medida(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,   //Clave primaria
    val nombre: String,                                 //Ej: gramos, litros, cucharada, pieza, etc.
    val tipo: String,                                   //Ej: masa, volumen y cantidad
    val equivalenteMetrico: Double                      //Conversión de medidas dentro del mismo tipo
)
