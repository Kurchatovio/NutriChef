package com.example.nutrichef.datos.baseDatos

import androidx.room.Database           // Importa la anotación @Database del paquete oficial de Room
import androidx.room.Room               // Builder que crea la instancia real de RoomDatabase
import androidx.room.RoomDatabase       // Clase abstracta de la que extienden las BD de Room
import android.content.Context          // Permite inicializar la BD con applicationContext

// Importa todas las entidades del modelo de datos
import com.example.nutrichef.datos.entidades.Medida
import com.example.nutrichef.datos.entidades.Ingrediente
import com.example.nutrichef.datos.entidades.Receta
import com.example.nutrichef.datos.entidades.RecetaIngrediente
import com.example.nutrichef.datos.entidades.PasoReceta
import com.example.nutrichef.datos.entidades.Etiqueta
import com.example.nutrichef.datos.entidades.RecetaEtiqueta
import com.example.nutrichef.datos.entidades.Libro
import com.example.nutrichef.datos.entidades.LibroReceta

// Importa todos los DAOs
import com.example.nutrichef.datos.dao.MedidaDao
import com.example.nutrichef.datos.dao.IngredienteDao
import com.example.nutrichef.datos.dao.RecetaDao
import com.example.nutrichef.datos.dao.RecetaIngredienteDao
import com.example.nutrichef.datos.dao.PasoRecetaDao
import com.example.nutrichef.datos.dao.EtiquetaDao
import com.example.nutrichef.datos.dao.RecetaEtiquetaDao
import com.example.nutrichef.datos.dao.LibroDao
import com.example.nutrichef.datos.dao.LibroRecetaDao

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [                            // Todas las tablas de la Base de Datos
        Medida::class,
        Ingrediente::class,
        Receta::class,
        RecetaIngrediente::class,
        PasoReceta::class,
        Etiqueta::class,
        RecetaEtiqueta::class,
        Libro::class,
        LibroReceta::class
    ],
    version = 1,
    exportSchema = true                     // Room generará archivos JSON con el historial de los cambios de la base de datos
)
abstract class BaseDatos : RoomDatabase() {     // Clase abstracta que extiende RoomDatabase y que Room implementará automáticamente

    // Referencias a cada DAO de la aplicación
    abstract fun medidaDao(): MedidaDao
    abstract fun ingredienteDao(): IngredienteDao
    abstract fun recetaDao(): RecetaDao
    abstract fun recetaIngredienteDao(): RecetaIngredienteDao
    abstract fun pasoRecetaDao(): PasoRecetaDao
    abstract fun etiquetaDao(): EtiquetaDao
    abstract fun recetaEtiquetaDao(): RecetaEtiquetaDao
    abstract fun libroDao(): LibroDao
    abstract fun libroRecetaDao(): LibroRecetaDao

    companion object {                  // Mantiene una única instancia de la base de datos (patrón Singleton)

        @Volatile                                               // Asegura que el valor sea siempre visible para todos los hilos (evita lecturas desactualizadas)
        private var INSTANCIA: BaseDatos? = null

        // Obtiene la instancia existente o crea una nueva si no existe
        fun obtenerInstancia(contexto: Context): BaseDatos {
            return INSTANCIA ?: synchronized(this) {

                val instanciaNueva = Room.databaseBuilder(      // Constructor de la BD
                    contexto.applicationContext,                // El contexto global de la app
                    BaseDatos::class.java,                      // Clase de la base de datos que Room debe implementar
                    "nutrichef_db"                              // Nombre del archivo físico SQLite en el dispositivo
                )
                    .fallbackToDestructiveMigration()           // Si la versión cambia sin migración, borra y recrea la BD
                    .build()

                INSTANCIA = instanciaNueva

                // Lanzar el seeder en un hilo de fondo (solo la 1ª vez que se crea la instancia en este proceso)
                CoroutineScope(Dispatchers.IO).launch {
                    DataSeeder.seedIfEmpty(instanciaNueva)
                }

                instanciaNueva
            }
        }
    }
}
