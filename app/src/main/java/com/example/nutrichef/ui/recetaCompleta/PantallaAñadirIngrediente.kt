package com.example.nutrichef.ui.recetaCompleta

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.compose.foundation.text.KeyboardOptions
import com.example.nutrichef.ui.componentes.BarraSuperiorConBack
import com.example.nutrichef.ui.navegacion.RutasPantallas

@Composable
fun PantallaAñadirIngrediente(
    navController: NavHostController,
    viewModel: RecetaCompletaViewModel
) {
    // ---------------------------------------
    // Cargar ingredientes BD (por seguridad)
    // ---------------------------------------
    LaunchedEffect(Unit) {
        viewModel.cargarIngredientesBD()
        viewModel.cargarMedidasBD()
    }

    // Lista de ingredientes disponible en BD
    val ingredientesBD by viewModel.todosIngredientes.collectAsState()

    // Lista temporal SOLO para mostrar en esta pantalla
    // (la receta real vive en el ViewModel)
    var ingredientesTemp by remember { mutableStateOf(listOf<Triple<Int, String, Double>>()) }
    // Triple = (ingredienteId, nombreIngrediente, cantidad)

    // Estado búsqueda
    var textoBusqueda by remember { mutableStateOf("") }

    // Ingrediente seleccionado (solo ID)
    var ingredienteSeleccionado by remember { mutableStateOf<Int?>(null) }

    // Cantidad
    var cantidadTexto by remember { mutableStateOf("") }

    // ----------------------------------------------
    //  INGREDIENTE CREADO DESDE PANTALLA CREAR
    // ----------------------------------------------
    val ultimoIngredienteCreado by viewModel.ultimoIngredienteCreadoId.collectAsState()
    val nombreTemporal by viewModel.nombreIngredienteTemporal.collectAsState()


    // Auto-selecciona el ingrediente recién creado cuando el catálogo ya lo tiene cargado
    LaunchedEffect(ultimoIngredienteCreado, ingredientesBD) {
        // Si venimos de crear un ingrediente nuevo:
        if (ultimoIngredienteCreado != null) {
            val idNuevo = ultimoIngredienteCreado!!
            // Solo actuamos cuando el ingrediente ya está disponible en el catálogo
            val existeEnCatalogo = ingredientesBD.any { it.id == idNuevo }
            if (existeEnCatalogo) {
                // Buscamos el nombre directamente del catálogo, no del nombre temporal
                // (el nombre temporal puede haberse limpiado antes de llegar aquí)
                val nombreIngrediente = ingredientesBD.find { it.id == idNuevo }?.nombre ?: ""
                ingredienteSeleccionado = idNuevo
                textoBusqueda = nombreIngrediente
                // Limpiamos los flags del ViewModel
                viewModel.setUltimoIngredienteCreadoId(null)
                viewModel.setNombreIngredienteTemporal("")
            }
        }
    }

    Scaffold(
        topBar = {
            BarraSuperiorConBack(
                titulo = "Añadir ingredientes",
                onBack = {
                    // NO hacemos más lógica aquí:
                    // los ingredientes ya se han ido guardando en el ViewModel
                    navController.popBackStack()
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {

            // ----------------------------------------------
            // BUSCADOR
            // ----------------------------------------------
            OutlinedTextField(
                value = textoBusqueda,
                onValueChange = { textoBusqueda = it },
                label = { Text("Buscar o crear ingrediente") },
                modifier = Modifier.fillMaxWidth()
            )

            // Resultados según texto
            val resultados = remember(textoBusqueda, ingredientesBD) {
                if (textoBusqueda.isBlank()) {
                    ingredientesBD
                } else {
                    ingredientesBD.filter {
                        it.nombre.contains(textoBusqueda, ignoreCase = true)
                    }
                }
            }

            // ----------------------------------------------
            // BOTÓN CREAR INGREDIENTE NUEVO (SIEMPRE VISIBLE)
            // ----------------------------------------------
            Button(
                onClick = {
                    // Guardamos el texto que el usuario ha escrito
                    viewModel.setNombreIngredienteTemporal(textoBusqueda)
                    navController.navigate(RutasPantallas.CrearIngredienteDesdeReceta.ruta)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Crear nuevo ingrediente")
            }

            Spacer(Modifier.height(12.dp))

            // ----------------------------------------------
            // LISTA DE SUGERENCIAS
            // ----------------------------------------------
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.heightIn(max = 250.dp)
            ) {
                itemsIndexed(resultados) { _, ing ->
                    val estaSeleccionado = ingredienteSeleccionado == ing.id
                    val unidad = viewModel.obtenerAbreviaturaDeIngrediente(ing.medidaId)

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            ingredienteSeleccionado = ing.id
                            textoBusqueda = ing.nombre
                        },
                        // Resalta visualmente el ingrediente seleccionado
                        colors = CardDefaults.cardColors(
                            containerColor = if (estaSeleccionado)
                                MaterialTheme.colorScheme.primaryContainer
                            else
                                MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(12.dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            // Nombre del ingrediente
                            Text(
                                ing.nombre,
                                color = if (estaSeleccionado)
                                    MaterialTheme.colorScheme.onPrimaryContainer
                                else
                                    MaterialTheme.colorScheme.onSurface
                            )
                            // Unidad de medida
                            if (unidad.isNotBlank()) {
                                Text(
                                    unidad,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // ----------------------------------------------
            // CANTIDAD
            // Solo visible si hay ingrediente seleccionado
            // ----------------------------------------------
            if (ingredienteSeleccionado != null) {
                OutlinedTextField(
                    value = cantidadTexto,
                    onValueChange = { cantidadTexto = it },
                    label = { Text("Cantidad") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(12.dp))

                Button(
                    onClick = {
                        val id = ingredienteSeleccionado ?: return@Button
                        val nombreIng =
                            ingredientesBD.find { it.id == id }?.nombre ?: return@Button
                        val cantidad = cantidadTexto.toDoubleOrNull() ?: return@Button

                        // Medida base del ingrediente (para la receta)
                        val medidaId = ingredientesBD.find { it.id == id }?.medidaId

                        // 1) Añadir al ViewModel (receta real)
                        viewModel.agregarIngrediente(
                            ingredienteId = id,
                            cantidad = cantidad,
                            medidaId = medidaId
                        )

                        // 2) Añadir a la lista temporal SOLO para esta pantalla
                        ingredientesTemp = ingredientesTemp + Triple(id, nombreIng, cantidad)

                        // 3) Limpiar selección y cantidad
                        ingredienteSeleccionado = null
                        textoBusqueda = ""
                        cantidadTexto = ""
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Añadir a la lista")
                }
            }

            Spacer(Modifier.height(20.dp))

            // ----------------------------------------------
            // LISTA TEMPORAL DE INGREDIENTES YA AÑADIDOS
            // ----------------------------------------------
            Text("Ingredientes añadidos:", style = MaterialTheme.typography.titleMedium)

            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                itemsIndexed(ingredientesTemp) { index, triple ->
                    Card(Modifier.fillMaxWidth()) {
                        Row(
                            Modifier
                                .padding(12.dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(triple.second)
                                val unidadCompleta = viewModel.obtenerUnidadDeIngrediente(triple.first.let { id ->
                                    viewModel.todosIngredientes.value.find { it.id == id }?.medidaId
                                })
                                Text("${triple.third} $unidadCompleta".trim())
                            }
                            IconButton(
                                onClick = {
                                    viewModel.eliminarIngrediente(triple.first)
                                    ingredientesTemp =
                                        ingredientesTemp.toMutableList().also { it.removeAt(index) }
                                }
                            ) {
                                Icon(
                                    Icons.Default.Delete,
                                    contentDescription = "Eliminar"
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
