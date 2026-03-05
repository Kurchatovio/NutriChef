package com.example.nutrichef.datos.baseDatos

import com.example.nutrichef.datos.entidades.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Clase encargada de precargar datos iniciales en la BD:
 *  - Medidas
 *  - Etiquetas
 *  - Ingredientes
 *  - Recetas + ingredientes + pasos + etiquetas (receta completa)
 *
 * Solo se ejecuta si NO hay recetas todavía (BD "vacía").
 */
object DataSeeder {

    suspend fun seedIfEmpty(db: BaseDatos) {
        withContext(Dispatchers.IO) {

            val recetaDao = db.recetaDao()

            // Si ya hay recetas, asumimos que la BD está en uso y NO sembramos nada.
            if (recetaDao.obtenerTodas().isNotEmpty()) return@withContext

            val medidaDao = db.medidaDao()
            val ingredienteDao = db.ingredienteDao()
            val etiquetaDao = db.etiquetaDao()
            val recetaIngredienteDao = db.recetaIngredienteDao()
            val pasoRecetaDao = db.pasoRecetaDao()
            val recetaEtiquetaDao = db.recetaEtiquetaDao()

            // =========================================================
            // 1) MEDIDAS
            // =========================================================
            val idGramo = medidaDao.insertar(
                Medida(
                    nombre = "gramos (g)",
                    tipo = "masa",
                    equivalenteMetrico = 1.0      // 1 g = 1 unidad base
                )
            ).toInt()

            val idKilo = medidaDao.insertar(
                Medida(
                    nombre = "kilogramos (kg)",
                    tipo = "masa",
                    equivalenteMetrico = 1000.0   // 1 kg = 1000 g
                )
            ).toInt()

            val idMl = medidaDao.insertar(
                Medida(
                    nombre = "mililitros (ml)",
                    tipo = "volumen",
                    equivalenteMetrico = 1.0      // 1 ml = 1 unidad base
                )
            ).toInt()

            val idLitro = medidaDao.insertar(
                Medida(
                    nombre = "litros (l)",
                    tipo = "volumen",
                    equivalenteMetrico = 1000.0   // 1 l = 1000 ml
                )
            ).toInt()

            val idCucharada = medidaDao.insertar(
                Medida(
                    nombre = "cucharada (tbsp)",
                    tipo = "volumen",
                    equivalenteMetrico = 15.0     // ~15 ml
                )
            ).toInt()

            val idCucharadita = medidaDao.insertar(
                Medida(
                    nombre = "cucharadita (tsp)",
                    tipo = "volumen",
                    equivalenteMetrico = 5.0      // ~5 ml
                )
            ).toInt()

            val idUnidad = medidaDao.insertar(
                Medida(
                    nombre = "unidad (ud)",
                    tipo = "cantidad",
                    equivalenteMetrico = 1.0      // 1 ud = 1
                )
            ).toInt()

            val idTaza = medidaDao.insertar(
                Medida(
                    nombre = "taza",
                    tipo = "volumen",
                    equivalenteMetrico = 250.0    // ~250 ml
                )
            ).toInt()

            // =========================================================
            // 2) ETIQUETAS
            // =========================================================
            val idEtiquetaRapido = etiquetaDao.insertar(Etiqueta(nombre = "Rápido")).toInt()
            val idEtiquetaFacil = etiquetaDao.insertar(Etiqueta(nombre = "Fácil")).toInt()
            val idEtiquetaDesayuno = etiquetaDao.insertar(Etiqueta(nombre = "Desayuno")).toInt()
            val idEtiquetaComida = etiquetaDao.insertar(Etiqueta(nombre = "Comida")).toInt()
            val idEtiquetaCena = etiquetaDao.insertar(Etiqueta(nombre = "Cena")).toInt()
            val idEtiquetaSnack = etiquetaDao.insertar(Etiqueta(nombre = "Snack")).toInt()
            val idEtiquetaVegetariano = etiquetaDao.insertar(Etiqueta(nombre = "Vegetariano")).toInt()
            val idEtiquetaVegano = etiquetaDao.insertar(Etiqueta(nombre = "Vegano")).toInt()
            val idEtiquetaSinGluten = etiquetaDao.insertar(Etiqueta(nombre = "Sin gluten")).toInt()
            val idEtiquetaAltoProte = etiquetaDao.insertar(Etiqueta(nombre = "Alto en proteína")).toInt()
            val idEtiquetaBajoGrasa = etiquetaDao.insertar(Etiqueta(nombre = "Bajo en grasa")).toInt()
            val idEtiquetaSaludable = etiquetaDao.insertar(Etiqueta(nombre = "Saludable")).toInt()

            // =========================================================
            // 3) INGREDIENTES BÁSICOS
            //    (puedes ampliar/modificar esta lista cuando quieras)
            // =========================================================
            suspend fun nuevoIngrediente(
                nombre: String,
                medidaId: Int,
                descripcion: String? = null,
                proteinas: Double? = null,
                carbohidratos: Double? = null,
                grasas: Double? = null,
                imagenUri: String? = null
            ): Int {
                return ingredienteDao.insertar(
                    Ingrediente(
                        nombre = nombre,
                        descripcion = descripcion,
                        medidaId = medidaId,
                        proteinasPorMedida = proteinas,
                        carbohidratosPorMedida = carbohidratos,
                        grasasPorMedida = grasas,
                        imagenUri = imagenUri
                    )
                ).toInt()
            }


            val idPatata = nuevoIngrediente("Patata", idGramo)
            val idHuevo = nuevoIngrediente("Huevo", idUnidad)
            val idCebolla = nuevoIngrediente("Cebolla", idGramo)
            val idAceiteOliva = nuevoIngrediente("Aceite de oliva", idMl)
            val idSal = nuevoIngrediente("Sal", idGramo)
            val idPasta = nuevoIngrediente("Pasta seca", idGramo)
            val idTomateTrito = nuevoIngrediente("Tomate triturado", idMl)
            val idAjo = nuevoIngrediente("Ajo", idGramo)
            val idPollo = nuevoIngrediente("Pechuga de pollo", idGramo)
            val idLechuga = nuevoIngrediente("Lechuga", idGramo)
            val idTomate = nuevoIngrediente("Tomate", idGramo)
            val idPepino = nuevoIngrediente("Pepino", idGramo)
            val idZanahoria = nuevoIngrediente("Zanahoria", idGramo)
            val idVinagre = nuevoIngrediente("Vinagre", idMl)
            val idAvena = nuevoIngrediente("Copos de avena", idGramo)
            val idLeche = nuevoIngrediente("Leche", idMl)
            val idPlatano = nuevoIngrediente("Plátano", idUnidad)
            val idCanela = nuevoIngrediente("Canela en polvo", idGramo)

            // =========================================================
            // 4) RECETAS + INGREDIENTES + PASOS + ETIQUETAS
            // =========================================================

            // ---------- 1) Tortilla de patata clásica ----------
            val idRecetaTortilla = recetaDao.insertar(
                Receta(
                    nombre = "Tortilla de patata clásica",
                    descripcion = "Tortilla jugosa de patata, huevo y cebolla.",
                    categoria = "Cena",
                    porciones = 4,
                    tiempoPreparacionMin = 40,
                    imagenUri = null
                )
            ).toInt()

            // Ingredientes de la tortilla
            recetaIngredienteDao.insertar(
                RecetaIngrediente(
                    recetaId = idRecetaTortilla,
                    ingredienteId = idPatata,
                    cantidad = 800.0,           // g
                    medidaId = idGramo
                )
            )
            recetaIngredienteDao.insertar(
                RecetaIngrediente(
                    recetaId = idRecetaTortilla,
                    ingredienteId = idHuevo,
                    cantidad = 6.0,             // uds
                    medidaId = idUnidad
                )
            )
            recetaIngredienteDao.insertar(
                RecetaIngrediente(
                    recetaId = idRecetaTortilla,
                    ingredienteId = idCebolla,
                    cantidad = 150.0,           // g
                    medidaId = idGramo
                )
            )
            recetaIngredienteDao.insertar(
                RecetaIngrediente(
                    recetaId = idRecetaTortilla,
                    ingredienteId = idAceiteOliva,
                    cantidad = 50.0,            // ml
                    medidaId = idMl
                )
            )
            recetaIngredienteDao.insertar(
                RecetaIngrediente(
                    recetaId = idRecetaTortilla,
                    ingredienteId = idSal,
                    cantidad = 5.0,             // g
                    medidaId = idGramo
                )
            )

            // Pasos de la tortilla
            pasoRecetaDao.insertar(
                PasoReceta(
                    recetaId = idRecetaTortilla,
                    orden = 1,
                    descripcion = "Pelar y cortar las patatas y la cebolla en láminas finas.",
                    tiempoMin = 10
                )
            )
            pasoRecetaDao.insertar(
                PasoReceta(
                    recetaId = idRecetaTortilla,
                    orden = 2,
                    descripcion = "Freír patata y cebolla en aceite hasta que estén tiernas.",
                    tiempoMin = 15
                )
            )
            pasoRecetaDao.insertar(
                PasoReceta(
                    recetaId = idRecetaTortilla,
                    orden = 3,
                    descripcion = "Batir los huevos, mezclar con patata y cebolla, sazonar.",
                    tiempoMin = 5
                )
            )
            pasoRecetaDao.insertar(
                PasoReceta(
                    recetaId = idRecetaTortilla,
                    orden = 4,
                    descripcion = "Cuajar la mezcla en la sartén por ambos lados.",
                    tiempoMin = 10
                )
            )

            // Etiquetas de la tortilla
            listOf(
                idEtiquetaComida,
                idEtiquetaCena,
                idEtiquetaFacil,
                idEtiquetaVegetariano
            ).forEach { etId ->
                recetaEtiquetaDao.insertar(
                    RecetaEtiqueta(
                        recetaId = idRecetaTortilla,
                        etiquetaId = etId
                    )
                )
            }

            // ---------- 2) Pasta con salsa de tomate ----------
            val idRecetaPasta = recetaDao.insertar(
                Receta(
                    nombre = "Pasta con salsa de tomate",
                    descripcion = "Pasta corta con salsa de tomate casera sencilla.",
                    categoria = "Comida",
                    porciones = 2,
                    tiempoPreparacionMin = 25,
                    imagenUri = null
                )
            ).toInt()

            recetaIngredienteDao.insertar(
                RecetaIngrediente(
                    recetaId = idRecetaPasta,
                    ingredienteId = idPasta,
                    cantidad = 200.0,
                    medidaId = idGramo
                )
            )
            recetaIngredienteDao.insertar(
                RecetaIngrediente(
                    recetaId = idRecetaPasta,
                    ingredienteId = idTomateTrito,
                    cantidad = 200.0,
                    medidaId = idMl
                )
            )
            recetaIngredienteDao.insertar(
                RecetaIngrediente(
                    recetaId = idRecetaPasta,
                    ingredienteId = idAceiteOliva,
                    cantidad = 20.0,
                    medidaId = idMl
                )
            )
            recetaIngredienteDao.insertar(
                RecetaIngrediente(
                    recetaId = idRecetaPasta,
                    ingredienteId = idAjo,
                    cantidad = 5.0,
                    medidaId = idGramo
                )
            )
            recetaIngredienteDao.insertar(
                RecetaIngrediente(
                    recetaId = idRecetaPasta,
                    ingredienteId = idSal,
                    cantidad = 5.0,
                    medidaId = idGramo
                )
            )

            pasoRecetaDao.insertar(
                PasoReceta(
                    recetaId = idRecetaPasta,
                    orden = 1,
                    descripcion = "Cocer la pasta en abundante agua con sal.",
                    tiempoMin = 10
                )
            )
            pasoRecetaDao.insertar(
                PasoReceta(
                    recetaId = idRecetaPasta,
                    orden = 2,
                    descripcion = "Sofreír el ajo y añadir el tomate triturado.",
                    tiempoMin = 10
                )
            )
            pasoRecetaDao.insertar(
                PasoReceta(
                    recetaId = idRecetaPasta,
                    orden = 3,
                    descripcion = "Mezclar la pasta escurrida con la salsa y servir.",
                    tiempoMin = 5
                )
            )

            listOf(
                idEtiquetaComida,
                idEtiquetaRapido,
                idEtiquetaFacil,
                idEtiquetaVegetariano
            ).forEach { etId ->
                recetaEtiquetaDao.insertar(
                    RecetaEtiqueta(
                        recetaId = idRecetaPasta,
                        etiquetaId = etId
                    )
                )
            }

            // ---------- 3) Pollo al horno con patatas ----------
            val idRecetaPollo = recetaDao.insertar(
                Receta(
                    nombre = "Pollo al horno con patatas",
                    descripcion = "Pechuga de pollo asada con patatas al horno.",
                    categoria = "Cena",
                    porciones = 3,
                    tiempoPreparacionMin = 50,
                    imagenUri = null
                )
            ).toInt()

            recetaIngredienteDao.insertar(
                RecetaIngrediente(
                    recetaId = idRecetaPollo,
                    ingredienteId = idPollo,
                    cantidad = 600.0,
                    medidaId = idGramo
                )
            )
            recetaIngredienteDao.insertar(
                RecetaIngrediente(
                    recetaId = idRecetaPollo,
                    ingredienteId = idPatata,
                    cantidad = 500.0,
                    medidaId = idGramo
                )
            )
            recetaIngredienteDao.insertar(
                RecetaIngrediente(
                    recetaId = idRecetaPollo,
                    ingredienteId = idAceiteOliva,
                    cantidad = 30.0,
                    medidaId = idMl
                )
            )
            recetaIngredienteDao.insertar(
                RecetaIngrediente(
                    recetaId = idRecetaPollo,
                    ingredienteId = idSal,
                    cantidad = 5.0,
                    medidaId = idGramo
                )
            )

            pasoRecetaDao.insertar(
                PasoReceta(
                    recetaId = idRecetaPollo,
                    orden = 1,
                    descripcion = "Precalentar el horno y preparar las patatas en rodajas.",
                    tiempoMin = 10
                )
            )
            pasoRecetaDao.insertar(
                PasoReceta(
                    recetaId = idRecetaPollo,
                    orden = 2,
                    descripcion = "Colocar el pollo y las patatas en una bandeja con aceite y sal.",
                    tiempoMin = 5
                )
            )
            pasoRecetaDao.insertar(
                PasoReceta(
                    recetaId = idRecetaPollo,
                    orden = 3,
                    descripcion = "Hornear hasta que el pollo esté hecho y las patatas doradas.",
                    tiempoMin = 35
                )
            )

            listOf(
                idEtiquetaComida,
                idEtiquetaCena,
                idEtiquetaAltoProte
            ).forEach { etId ->
                recetaEtiquetaDao.insertar(
                    RecetaEtiqueta(
                        recetaId = idRecetaPollo,
                        etiquetaId = etId
                    )
                )
            }

            // ---------- 4) Ensalada mixta ----------
            val idRecetaEnsalada = recetaDao.insertar(
                Receta(
                    nombre = "Ensalada mixta",
                    descripcion = "Ensalada fresca de lechuga, tomate, cebolla y verduras.",
                    categoria = "Comida",
                    porciones = 2,
                    tiempoPreparacionMin = 15,
                    imagenUri = null
                )
            ).toInt()

            recetaIngredienteDao.insertar(
                RecetaIngrediente(
                    recetaId = idRecetaEnsalada,
                    ingredienteId = idLechuga,
                    cantidad = 80.0,
                    medidaId = idGramo
                )
            )
            recetaIngredienteDao.insertar(
                RecetaIngrediente(
                    recetaId = idRecetaEnsalada,
                    ingredienteId = idTomate,
                    cantidad = 100.0,
                    medidaId = idGramo
                )
            )
            recetaIngredienteDao.insertar(
                RecetaIngrediente(
                    recetaId = idRecetaEnsalada,
                    ingredienteId = idCebolla,
                    cantidad = 40.0,
                    medidaId = idGramo
                )
            )
            recetaIngredienteDao.insertar(
                RecetaIngrediente(
                    recetaId = idRecetaEnsalada,
                    ingredienteId = idPepino,
                    cantidad = 60.0,
                    medidaId = idGramo
                )
            )
            recetaIngredienteDao.insertar(
                RecetaIngrediente(
                    recetaId = idRecetaEnsalada,
                    ingredienteId = idAceiteOliva,
                    cantidad = 15.0,
                    medidaId = idMl
                )
            )
            recetaIngredienteDao.insertar(
                RecetaIngrediente(
                    recetaId = idRecetaEnsalada,
                    ingredienteId = idVinagre,
                    cantidad = 10.0,
                    medidaId = idMl
                )
            )
            recetaIngredienteDao.insertar(
                RecetaIngrediente(
                    recetaId = idRecetaEnsalada,
                    ingredienteId = idSal,
                    cantidad = 3.0,
                    medidaId = idGramo
                )
            )

            pasoRecetaDao.insertar(
                PasoReceta(
                    recetaId = idRecetaEnsalada,
                    orden = 1,
                    descripcion = "Lavar y cortar todas las verduras.",
                    tiempoMin = 10
                )
            )
            pasoRecetaDao.insertar(
                PasoReceta(
                    recetaId = idRecetaEnsalada,
                    orden = 2,
                    descripcion = "Mezclar en un bol y aliñar con aceite, vinagre y sal.",
                    tiempoMin = 5
                )
            )

            listOf(
                idEtiquetaComida,
                idEtiquetaCena,
                idEtiquetaVegetariano,
                idEtiquetaSaludable,
                idEtiquetaBajoGrasa
            ).forEach { etId ->
                recetaEtiquetaDao.insertar(
                    RecetaEtiqueta(
                        recetaId = idRecetaEnsalada,
                        etiquetaId = etId
                    )
                )
            }

            // ---------- 5) Avena con plátano y leche ----------
            val idRecetaAvena = recetaDao.insertar(
                Receta(
                    nombre = "Avena con plátano y leche",
                    descripcion = "Desayuno caliente de avena con leche, plátano y canela.",
                    categoria = "Desayuno",
                    porciones = 1,
                    tiempoPreparacionMin = 10,
                    imagenUri = null
                )
            ).toInt()

            recetaIngredienteDao.insertar(
                RecetaIngrediente(
                    recetaId = idRecetaAvena,
                    ingredienteId = idAvena,
                    cantidad = 60.0,
                    medidaId = idGramo
                )
            )
            recetaIngredienteDao.insertar(
                RecetaIngrediente(
                    recetaId = idRecetaAvena,
                    ingredienteId = idLeche,
                    cantidad = 250.0,
                    medidaId = idMl
                )
            )
            recetaIngredienteDao.insertar(
                RecetaIngrediente(
                    recetaId = idRecetaAvena,
                    ingredienteId = idPlatano,
                    cantidad = 1.0,
                    medidaId = idUnidad
                )
            )
            recetaIngredienteDao.insertar(
                RecetaIngrediente(
                    recetaId = idRecetaAvena,
                    ingredienteId = idCanela,
                    cantidad = 1.0,
                    medidaId = idGramo   // realmente sería menos, pero vale como ejemplo
                )
            )

            pasoRecetaDao.insertar(
                PasoReceta(
                    recetaId = idRecetaAvena,
                    orden = 1,
                    descripcion = "Calentar la leche y añadir los copos de avena.",
                    tiempoMin = 5
                )
            )
            pasoRecetaDao.insertar(
                PasoReceta(
                    recetaId = idRecetaAvena,
                    orden = 2,
                    descripcion = "Cocinar hasta que espese, añadir plátano en rodajas y canela.",
                    tiempoMin = 5
                )
            )

            listOf(
                idEtiquetaDesayuno,
                idEtiquetaSaludable,
                idEtiquetaVegetariano,
                idEtiquetaAltoProte
            ).forEach { etId ->
                recetaEtiquetaDao.insertar(
                    RecetaEtiqueta(
                        recetaId = idRecetaAvena,
                        etiquetaId = etId
                    )
                )
            }
        }
    }
}
