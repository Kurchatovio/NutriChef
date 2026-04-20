package com.example.nutrichef.ui.navegacion

/**
 * Clase sellada que contiene TODAS las rutas de navegación de la app NutriChef.
 *
 * Notas:
 * - Usamos snake_case para las rutas → más limpio y evita confundirlas con clases.
 * - Todas las pantallas deben declararse aquí.
 * - Ninguna ruta debe repetirse.
 */
sealed class RutasPantallas(val ruta: String) {

    // ----------------------------
    // PANTALLAS PRINCIPALES
    // ----------------------------
    object Home : RutasPantallas("pantalla_home")

    object ListaRecetas : RutasPantallas("lista_recetas")

    // Crear receta NUEVA
    object CrearRecetaCompleta : RutasPantallas("crear_receta_completa")

    // Editar una receta existente (recibe ID obligatorio)
    object EditarReceta : RutasPantallas("editar_receta_completa/{idReceta}") {
        fun crearRuta(id: Int) = "editar_receta_completa/$id"
    }

    // Ver una receta en detalle
    object DetalleReceta : RutasPantallas("detalle_receta/{idReceta}") {
        fun crearRuta(idReceta: Int) = "detalle_receta/$idReceta"
    }


    // ----------------------------
    // INGREDIENTES
    // ----------------------------
    object Ingredientes : RutasPantallas("lista_ingredientes")

    // Crear ingrediente desde menú principal
    object CrearIngrediente : RutasPantallas("crear_ingrediente")

    // Crear ingrediente desde la pantalla de añadir ingrediente a receta
    object CrearIngredienteDesdeReceta : RutasPantallas("crear_ingrediente_desde_receta")

    // Añadir un ingrediente a la receta que se está creando/editando
    object AñadirIngrediente : RutasPantallas("añadir_ingrediente")

    // Pantalla Detalle Ingrediente
    object DetalleIngrediente : RutasPantallas("detalle_ingrediente/{idIngrediente}") {
        fun crearRuta(idIngrediente: Int) = "detalle_ingrediente/$idIngrediente"
    }

    // Editar un ingrediente existente
    object EditarIngrediente : RutasPantallas("editar_ingrediente/{idIngrediente}") {
        fun crearRuta(idIngrediente: Int) = "editar_ingrediente/$idIngrediente"
    }


    // ----------------------------
    // PASOS
    // ----------------------------
    object AñadirPaso : RutasPantallas("añadir_paso")


    // ----------------------------
    // ETIQUETAS
    // ----------------------------
    object Etiquetas : RutasPantallas("lista_etiquetas")


    // ----------------------------
    // LIBROS
    // ----------------------------
    object Libros : RutasPantallas("lista_libros")
}
