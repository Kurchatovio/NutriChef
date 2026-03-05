package com.example.nutrichef.datos.dao

import androidx.room.*
import com.example.nutrichef.datos.entidades.RecetaEtiqueta

@Dao
interface RecetaEtiquetaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(relacion: RecetaEtiqueta): Long

    @Update
    suspend fun actualizar(relacion: RecetaEtiqueta)

    @Delete
    suspend fun eliminar(relacion: RecetaEtiqueta)

    @Query("SELECT * FROM recetaEtiqueta WHERE recetaId = :idReceta")
    suspend fun obtenerPorReceta(idReceta: Int): List<RecetaEtiqueta>

    @Query("SELECT * FROM recetaEtiqueta WHERE etiquetaId = :idEtiqueta")
    suspend fun obtenerPorEtiqueta(idEtiqueta: Int): List<RecetaEtiqueta>

    @Query("""
        SELECT * FROM recetaEtiqueta 
        WHERE recetaId = :idReceta AND etiquetaId = :idEtiqueta 
        LIMIT 1
    """)
    suspend fun obtenerRelacion(idReceta: Int, idEtiqueta: Int): RecetaEtiqueta?

    // ❗ ESTA FUNCIÓN FALTABA
    @Query("DELETE FROM recetaEtiqueta WHERE recetaId = :idReceta")
    suspend fun eliminarPorReceta(idReceta: Int)
}
