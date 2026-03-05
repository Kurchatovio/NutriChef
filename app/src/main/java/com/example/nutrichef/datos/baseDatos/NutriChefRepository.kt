package com.example.nutrichef.datos.repositorio

import androidx.room.withTransaction
import com.example.nutrichef.datos.baseDatos.BaseDatos
import com.example.nutrichef.datos.entidades.*
import com.example.nutrichef.datos.dao.*

class NutriChefRepository private constructor(
    private val baseDatos: BaseDatos
) {

    // ------------------------------------------------------------
    //                      DAOs PRINCIPALES
    // ------------------------------------------------------------

    private val recetaDao = baseDatos.recetaDao()
    private val recetaIngredienteDao = baseDatos.recetaIngredienteDao()
    private val pasoRecetaDao = baseDatos.pasoRecetaDao()
    private val recetaEtiquetaDao = baseDatos.recetaEtiquetaDao()
    private val ingredienteDao = baseDatos.ingredienteDao()
    private val medidaDao = baseDatos.medidaDao()
    private val etiquetaDao = baseDatos.etiquetaDao()
    private val libroDao = baseDatos.libroDao()
    private val libroRecetaDao = baseDatos.libroRecetaDao()

    companion object {
        @Volatile private var INSTANCIA: NutriChefRepository? = null

        fun obtenerInstancia(baseDatos: BaseDatos): NutriChefRepository {
            return INSTANCIA ?: synchronized(this) {
                INSTANCIA ?: NutriChefRepository(baseDatos).also { INSTANCIA = it }
            }
        }
    }

    // ============================================================
    //              Funciones usadas por DetalleRecetaViewModel
    // ============================================================

    suspend fun obtenerIngredientesDeReceta(idReceta: Int): List<RecetaIngrediente> =
        recetaIngredienteDao.obtenerPorReceta(idReceta)

    suspend fun obtenerIngredientePorId(id: Int): Ingrediente? =
        ingredienteDao.obtenerPorId(id)

    suspend fun obtenerMedidaPorId(id: Int): Medida? =
        medidaDao.obtenerPorId(id)

    suspend fun obtenerPasosPorReceta(idReceta: Int): List<PasoReceta> =
        pasoRecetaDao.obtenerPorReceta(idReceta)

    // ============================================================
    //                          RECETAS
    // ============================================================

    suspend fun obtenerRecetaPorId(id: Int): Receta? =
        recetaDao.obtenerPorId(id)

    suspend fun obtenerTodasLasRecetas(): List<Receta> =
        recetaDao.obtenerTodas()

    suspend fun insertarRecetaCompleta(
        receta: Receta,
        ingredientes: List<RecetaIngrediente>,
        pasos: List<PasoReceta>,
        etiquetasIds: List<Int>
    ): Long = baseDatos.withTransaction {

        val idReceta = recetaDao.insertar(receta)

        ingredientes.forEach {
            recetaIngredienteDao.insertar(it.copy(recetaId = idReceta.toInt()))
        }

        pasos.forEach {
            pasoRecetaDao.insertar(it.copy(recetaId = idReceta.toInt()))
        }

        etiquetasIds.forEach { idEtiqueta ->
            recetaEtiquetaDao.insertar(
                RecetaEtiqueta(recetaId = idReceta.toInt(), etiquetaId = idEtiqueta)
            )
        }

        idReceta
    }

    suspend fun actualizarRecetaCompleta(
        receta: Receta,
        ingredientes: List<RecetaIngrediente>,
        pasos: List<PasoReceta>,
        etiquetasIds: List<Int>
    ) {
        val idReceta = receta.id ?: error("La receta no tiene ID, no se puede editar.")

        baseDatos.withTransaction {

            recetaDao.actualizar(receta)

            recetaIngredienteDao.eliminarPorReceta(idReceta)
            ingredientes.forEach {
                recetaIngredienteDao.insertar(it.copy(recetaId = idReceta))
            }

            pasoRecetaDao.eliminarPorReceta(idReceta)
            pasos.forEach {
                pasoRecetaDao.insertar(it.copy(recetaId = idReceta))
            }

            recetaEtiquetaDao.eliminarPorReceta(idReceta)
            etiquetasIds.forEach { idEtiqueta ->
                recetaEtiquetaDao.insertar(
                    RecetaEtiqueta(recetaId = idReceta, etiquetaId = idEtiqueta)
                )
            }
        }
    }

    suspend fun borrarRecetaCompleta(idReceta: Int) {
        baseDatos.withTransaction {
            recetaIngredienteDao.eliminarPorReceta(idReceta)
            pasoRecetaDao.eliminarPorReceta(idReceta)
            recetaEtiquetaDao.eliminarPorReceta(idReceta)

            recetaDao.obtenerPorId(idReceta)?.let { recetaDao.eliminar(it) }
        }
    }

    // ============================================================
    //                        INGREDIENTES
    // ============================================================

    suspend fun obtenerTodosLosIngredientes(): List<Ingrediente> =
        ingredienteDao.obtenerTodos()

    suspend fun buscarIngredientesPorNombre(texto: String): List<Ingrediente> =
        ingredienteDao.buscarPorNombre(texto)

    suspend fun insertarIngrediente(ingrediente: Ingrediente): Long =
        ingredienteDao.insertar(ingrediente)

    suspend fun actualizarIngrediente(ingrediente: Ingrediente) =
        ingredienteDao.actualizar(ingrediente)

    suspend fun eliminarIngrediente(ingrediente: Ingrediente) =
        ingredienteDao.eliminar(ingrediente)

    // "Cuenta" la cantidad de veces que un ingrediente aparece en recetas."
    suspend fun contarRecetasQueUsanIngrediente(idIngrediente: Int): Int {
        return recetaIngredienteDao.contarRecetasConIngrediente(idIngrediente)
    }

    // Determina si un ingrediente está en uso
    suspend fun ingredienteEstaEnUso(idIngrediente: Int): Boolean {
        return contarRecetasQueUsanIngrediente(idIngrediente) > 0
    }

    // Elimina un ingrediente si no está en uso en ninguna receta.
    suspend fun eliminarIngredienteSiNoEstaEnUso(ingrediente: Ingrediente): Boolean {
        val usos = contarRecetasQueUsanIngrediente(ingrediente.id)
        return if (usos == 0) {
            ingredienteDao.eliminar(ingrediente)
            true
        } else {
            false
        }
    }

    // ============================================================
    //                           MEDIDAS
    // ============================================================

    suspend fun obtenerTodasLasMedidas(): List<Medida> =
        medidaDao.obtenerTodas()

    // ============================================================
    //                           ETIQUETAS
    // ============================================================

    suspend fun obtenerTodasLasEtiquetas(): List<Etiqueta> =
        etiquetaDao.obtenerTodas()

    suspend fun buscarEtiquetasPorNombre(texto: String): List<Etiqueta> =
        etiquetaDao.buscarPorNombre(texto)

    suspend fun insertarEtiqueta(etiqueta: Etiqueta): Long =
        etiquetaDao.insertar(etiqueta)

    suspend fun actualizarEtiqueta(etiqueta: Etiqueta) =
        etiquetaDao.actualizar(etiqueta)

    suspend fun eliminarEtiqueta(etiqueta: Etiqueta) =
        etiquetaDao.eliminar(etiqueta)

    // ============================================================
    //                             LIBROS
    // ============================================================

    suspend fun obtenerTodosLosLibros(): List<Libro> =
        libroDao.obtenerTodos()

    suspend fun buscarLibrosPorNombre(texto: String): List<Libro> =
        libroDao.buscarPorNombre(texto)

    suspend fun insertarLibro(libro: Libro): Long =
        libroDao.insertar(libro)

    suspend fun actualizarLibro(libro: Libro) =
        libroDao.actualizar(libro)

    suspend fun eliminarLibro(libro: Libro) =
        libroDao.eliminar(libro)

    // ============================================================
    //                   LIBRO – RECETA (TABLA PUENTE)
    // ============================================================

    suspend fun obtenerRelacionesLibroRecetaPorLibro(idLibro: Int): List<LibroReceta> =
        libroRecetaDao.obtenerPorLibro(idLibro)

    suspend fun insertarRelacionLibroReceta(relacion: LibroReceta): Long =
        libroRecetaDao.insertar(relacion)

    suspend fun eliminarRelacionLibroReceta(relacion: LibroReceta) =
        libroRecetaDao.eliminar(relacion)

    // ============================================================
//              CRUD SIMPLE DE RECETAS (para ListaRecetas)
// ============================================================

    suspend fun buscarRecetasPorNombre(texto: String): List<Receta> {
        return recetaDao.buscarPorNombre(texto)
    }

    suspend fun insertarReceta(receta: Receta): Long {
        return recetaDao.insertar(receta)
    }

    suspend fun actualizarReceta(receta: Receta) {
        recetaDao.actualizar(receta)
    }

    suspend fun eliminarReceta(receta: Receta) {
        recetaDao.eliminar(receta)
    }

    // ============================================================
//                        PASOS (CRUD BASICO)
// ============================================================

    suspend fun insertarPaso(paso: PasoReceta): Long {
        return pasoRecetaDao.insertar(paso)
    }

    suspend fun actualizarPaso(paso: PasoReceta) {
        pasoRecetaDao.actualizar(paso)
    }

    suspend fun eliminarPaso(paso: PasoReceta) {
        pasoRecetaDao.eliminar(paso)
    }


}
