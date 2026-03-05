package com.example.nutrichef.datos.dao

import androidx.room.*
import com.example.nutrichef.datos.entidades.Ingrediente

@Dao
interface IngredienteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(ingrediente: Ingrediente): Long

    @Update
    suspend fun actualizar(ingrediente: Ingrediente)

    @Delete
    suspend fun eliminar(ingrediente: Ingrediente)

    @Query("SELECT * FROM ingrediente ORDER BY nombre ASC")
    suspend fun obtenerTodos(): List<Ingrediente>

    @Query("SELECT * FROM ingrediente WHERE id = :id LIMIT 1")
    suspend fun obtenerPorId(id: Int): Ingrediente?

    @Query("SELECT * FROM ingrediente WHERE nombre LIKE '%' || :texto || '%' ORDER BY nombre ASC")
    suspend fun buscarPorNombre(texto: String): List<Ingrediente>
}
