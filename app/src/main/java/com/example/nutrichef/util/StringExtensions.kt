package com.example.nutrichef.util

import java.text.Normalizer

/**
 * Elimina acentos, tildes y marcas diacríticas de un String.
 *
 * Ejemplos:
 *  "atún".sinAcentos()  -> "atun"
 *  "café".sinAcentos()  -> "cafe"
 *  "niño".sinAcentos()  -> "nino"
 *
 * Esto permite realizar búsquedas más naturales en español.
 */
fun String.sinAcentos(): String {
    val normalized = Normalizer.normalize(this, Normalizer.Form.NFD)
    return normalized.replace(Regex("\\p{InCombiningDiacriticalMarks}+"), "")
}
