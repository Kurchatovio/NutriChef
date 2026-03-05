package com.example.nutrichef.datos.dao

import androidx.room.*
import com.example.nutrichef.datos.entidades.PasoReceta

@Dao
interface PasoRecetaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(paso: PasoReceta): Long

    @Update
    suspend fun actualizar(paso: PasoReceta)

    @Delete
    suspend fun eliminar(paso: PasoReceta)

    @Query("SELECT * FROM pasoReceta WHERE recetaId = :idReceta ORDER BY orden ASC")
    suspend fun obtenerPorReceta(idReceta: Int): List<PasoReceta>

    @Query("SELECT * FROM pasoReceta WHERE id = :id LIMIT 1")
    suspend fun obtenerPorId(id: Int): PasoReceta?

    @Query("DELETE FROM pasoReceta WHERE recetaId = :idReceta")
    suspend fun eliminarPorReceta(idReceta: Int)
}
