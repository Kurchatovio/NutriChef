package com.example.nutrichef.datos.dao

import androidx.room.Dao                                            //Importa la anotación @Dao del paquete oficial de Room
import androidx.room.Insert                                         //Importa la anotación @Insert del paquete oficial de Room
import androidx.room.Update                                         //Importa la anotación @Update del paquete oficial de Room
import androidx.room.Delete                                         //Importa la anotación @Delete del paquete oficial de Room
import androidx.room.Query                                          //Importa la anotación @Query del paquete oficial de Room
import androidx.room.OnConflictStrategy                             //Permite decidir qué hacer ante un conflicto (REPLACE, ABORT, IGNORE)
import com.example.nutrichef.datos.entidades.Etiqueta               //Importa la entidad "Etiqueta" desde el paquete "entidades"

@Dao
interface EtiquetaDao {

    // Insertar nueva etiqueta
    // Si se inserta una etiqueta con un id ya existente, esta se reemplaza
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(etiqueta: Etiqueta): Long

    // Actualizar una etiqueta existente
    @Update
    suspend fun actualizar(etiqueta: Etiqueta)

    // Eliminar una etiqueta
    @Delete
    suspend fun eliminar(etiqueta: Etiqueta)

    // Obtener todas las etiquetas ordenadas alfabéticamente
    @Query("SELECT * FROM etiqueta ORDER BY nombre ASC")
    suspend fun obtenerTodas(): List<Etiqueta>

    // Obtener una etiqueta concreta por su ID
    @Query("SELECT * FROM etiqueta WHERE id = :id LIMIT 1")
    suspend fun obtenerPorId(id: Int): Etiqueta?

    // Buscar etiquetas por nombre usando coincidencia parcial
    @Query("SELECT * FROM etiqueta WHERE nombre LIKE '%' || :texto || '%' ORDER BY nombre ASC")
    suspend fun buscarPorNombre(texto: String): List<Etiqueta>
}
