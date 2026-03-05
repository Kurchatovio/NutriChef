package com.example.nutrichef.datos.dao

import androidx.room.*
import com.example.nutrichef.datos.entidades.Medida

@Dao
interface MedidaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(medida: Medida): Long

    @Update
    suspend fun actualizar(medida: Medida)

    @Delete
    suspend fun eliminar(medida: Medida)

    @Query("SELECT * FROM medida ORDER BY nombre ASC")
    suspend fun obtenerTodas(): List<Medida>

    @Query("SELECT * FROM medida WHERE id = :id LIMIT 1")
    suspend fun obtenerPorId(id: Int): Medida?
}
