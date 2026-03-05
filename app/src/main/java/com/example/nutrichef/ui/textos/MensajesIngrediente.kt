package com.example.nutrichef.ui.textos

object MensajesIngrediente {
    const val CONFIRMAR_ELIMINAR_TITULO =
        "Eliminar ingrediente"

    const val CONFIRMAR_ELIMINAR_TEXTO =
        "Esta acción eliminará permanentemente el ingrediente. ¿Desea continuar?"

    const val BLOQUEO_TITULO =
        "No es posible eliminar este ingrediente"

    val BLOQUEO_TEXTO = """
        El ingrediente está siendo utilizado en una o más recetas.

        Para eliminarlo, primero debes quitarlo o sustituirlo en todas las recetas donde aparece.

        Como alternativa, puedes crear distintas versiones del ingrediente.
        Por ejemplo:
        “Huevo pequeño”, “Huevo mediano”, “Huevo grande”.
    """.trimIndent()
}
