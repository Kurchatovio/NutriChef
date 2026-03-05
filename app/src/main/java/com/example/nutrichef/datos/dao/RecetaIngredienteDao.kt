package com.example.nutrichef.datos.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Update
import androidx.room.Delete
import androidx.room.Query
import androidx.room.OnConflictStrategy
import com.example.nutrichef.datos.entidades.RecetaIngrediente

@Dao
interface RecetaIngredienteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(relacion: RecetaIngrediente): Long

    @Update
    suspend fun actualizar(relacion: RecetaIngrediente)

    @Delete
    suspend fun eliminar(relacion: RecetaIngrediente)

    @Query("SELECT * FROM recetaIngrediente WHERE recetaId = :idReceta")
    suspend fun obtenerPorReceta(idReceta: Int): List<RecetaIngrediente>

    @Query("SELECT * FROM recetaIngrediente WHERE ingredienteId = :idIngrediente")
    suspend fun obtenerPorIngrediente(idIngrediente: Int): List<RecetaIngrediente>

    @Query("""
        SELECT *
        FROM recetaIngrediente
        WHERE recetaId = :idReceta AND ingredienteId = :idIngrediente
        LIMIT 1
    """)
    suspend fun obtenerRelacion(idReceta: Int, idIngrediente: Int): RecetaIngrediente?

    @Query("DELETE FROM recetaIngrediente WHERE recetaId = :idReceta")
    suspend fun eliminarPorReceta(idReceta: Int)

    // ---------------------------------------------------------
    // NUEVO → Necesario para evitar eliminar ingredientes usados
    // ---------------------------------------------------------
    @Query("SELECT COUNT(*) FROM recetaIngrediente WHERE ingredienteId = :idIngrediente")
    suspend fun contarRecetasConIngrediente(idIngrediente: Int): Int
}
