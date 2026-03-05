package com.example.nutrichef.datos.dao

import androidx.room.Dao                                            // Importa la anotación @Dao del paquete oficial de Room
import androidx.room.Insert                                         // Importa la anotación @Insert del paquete oficial de Room
import androidx.room.Update                                         // Importa la anotación @Update del paquete oficial de Room
import androidx.room.Delete                                         // Importa la anotación @Delete del paquete oficial de Room
import androidx.room.Query                                          // Importa la anotación @Query del paquete oficial de Room
import androidx.room.OnConflictStrategy                             // Permite decidir qué hacer ante un conflicto (REPLACE, ABORT, IGNORE)
import com.example.nutrichef.datos.entidades.LibroReceta            // Importa la entidad "LibroReceta" desde el paquete "entidades"

@Dao
interface LibroRecetaDao {

    // Insertar una relación entre libro y receta.
    // Si ya existe la combinación (libroId, recetaId), REPLACE la actualiza en lugar de producir error.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(relacion: LibroReceta): Long

    // Actualizar una relación existente
    @Update
    suspend fun actualizar(relacion: LibroReceta)

    // Eliminar una relación libro-receta
    @Delete
    suspend fun eliminar(relacion: LibroReceta)

    // Obtener todas las recetas asociadas a un libro concreto
    @Query("SELECT * FROM libroReceta WHERE libroId = :idLibro")
    suspend fun obtenerPorLibro(idLibro: Int): List<LibroReceta>

    // Obtener todos los libros en los que aparece una receta concreta
    @Query("SELECT * FROM libroReceta WHERE recetaId = :idReceta")
    suspend fun obtenerPorReceta(idReceta: Int): List<LibroReceta>

    // Obtener una relación específica libro-receta
    @Query("""
        SELECT * 
        FROM libroReceta 
        WHERE libroId = :idLibro AND recetaId = :idReceta
        LIMIT 1
    """)
    suspend fun obtenerRelacion(idLibro: Int, idReceta: Int): LibroReceta?
}
