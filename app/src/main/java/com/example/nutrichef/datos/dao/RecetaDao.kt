package com.example.nutrichef.datos.dao

import androidx.room.Dao                                //Importa la anotación @Dao del paquete oficial de Room
import androidx.room.Insert                             //Importa la anotación @Insert del paquete oficial de Room
import androidx.room.Update                             //Importa la anotación @Update del paquete oficial de Room
import androidx.room.Delete                             //Importa la anotación @Delete del paquete oficial de Room
import androidx.room.Query                              //Importa la anotación @Query del paquete oficial de Room
import androidx.room.OnConflictStrategy
import com.example.nutrichef.datos.entidades.Receta     //Importa la entidad "Receta" desde el paquete "entidades"

@Dao
interface RecetaDao {

    // Insertar una receta
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(receta: Receta): Long

    // Actualizar una receta existente
    @Update
    suspend fun actualizar(receta: Receta)

    // Eliminar una receta
    @Delete
    suspend fun eliminar(receta: Receta)

    // Obtener todas las recetas ordenadas por nombre
    @Query("SELECT * FROM receta ORDER BY nombre ASC")
    suspend fun obtenerTodas(): List<Receta>

    // Obtener una receta por ID
    @Query("SELECT * FROM receta WHERE id = :id LIMIT 1")
    suspend fun obtenerPorId(id: Int): Receta?

    // Buscar recetas por nombre
    @Query("SELECT * FROM receta WHERE nombre LIKE '%' || :texto || '%' ORDER BY nombre ASC")
    suspend fun buscarPorNombre(texto: String): List<Receta>
}
