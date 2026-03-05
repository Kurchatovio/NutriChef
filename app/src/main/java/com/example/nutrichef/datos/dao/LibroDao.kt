package com.example.nutrichef.datos.dao

import androidx.room.Dao                                            // Importa la anotación @Dao del paquete oficial de Room
import androidx.room.Insert                                         // Importa la anotación @Insert del paquete oficial de Room
import androidx.room.Update                                         // Importa la anotación @Update del paquete oficial de Room
import androidx.room.Delete                                         // Importa la anotación @Delete del paquete oficial de Room
import androidx.room.Query                                          // Importa la anotación @Query del paquete oficial de Room
import androidx.room.OnConflictStrategy                             // Permite decidir qué hacer ante un conflicto (REPLACE, ABORT, IGNORE)
import com.example.nutrichef.datos.entidades.Libro                  // Importa la entidad "Libro" desde el paquete "entidades"

@Dao
interface LibroDao {

    // Insertar un libro nuevo.
    // Si ya existe un libro con el mismo ID ESTE se sobrescribE (REPLACE)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(libro: Libro): Long

    // Actualizar un libro existente
    @Update
    suspend fun actualizar(libro: Libro)

    // Eliminar un libro
    @Delete
    suspend fun eliminar(libro: Libro)

    // Obtener todos los libros ordenados alfabéticamente
    @Query("SELECT * FROM libro ORDER BY nombre ASC")
    suspend fun obtenerTodos(): List<Libro>

    // Obtener un libro específico por su ID
    @Query("SELECT * FROM libro WHERE id = :id LIMIT 1")
    suspend fun obtenerPorId(id: Int): Libro?

    // BuscaR libros por nombre usando coincidencia parcial
    @Query("SELECT * FROM libro WHERE nombre LIKE '%' || :texto || '%' ORDER BY nombre ASC")
    suspend fun buscarPorNombre(texto: String): List<Libro>
}
