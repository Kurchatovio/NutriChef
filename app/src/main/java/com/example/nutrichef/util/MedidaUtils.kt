package com.example.nutrichef.util

import com.example.nutrichef.datos.entidades.Medida
import com.example.nutrichef.util.obtenerNombreMedida
import com.example.nutrichef.util.obtenerAbreviraturaMedida

// Devuelve el nombre completo de la medida dado su ID
// Ej: "mililitros (ml)", "gramos (g)"
fun obtenerNombreMedida(medidaId: Int?, medidas: List<Medida>): String {
    medidaId ?: return ""
    return medidas.find { it.id == medidaId }?.nombre ?: ""
}

// Devuelve la abreviatura de la medida extraída entre paréntesis
// Ej: "gramos (g)" → "g", "mililitros (ml)" → "ml", "taza" → "taza"
fun obtenerAbreviraturaMedida(medidaId: Int?, medidas: List<Medida>): String {
    medidaId ?: return ""
    val nombreCompleto = medidas.find { it.id == medidaId }?.nombre ?: return ""
    val regex = Regex("\\(([^)]+)\\)")
    return regex.find(nombreCompleto)?.groupValues?.get(1) ?: nombreCompleto
}