package com.example.nutrichef.ui.recetaCompleta

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.nutrichef.ui.componentes.BarraSuperiorConBack
import com.example.nutrichef.ui.navegacion.RutasPantallas

@Composable
fun PantallaCrearRecetaCompleta(
    navController: NavHostController,
    viewModel: RecetaCompletaViewModel,
    alGuardarReceta: (Long) -> Unit,
    alVolver: () -> Unit
) {

    // ============================================
    // DIÁLOGO DE CONFIRMACIÓN AL SALIR
    // ============================================
    var mostrarDialogoSalir by remember { mutableStateOf(false) }

    if (mostrarDialogoSalir) {
        AlertDialog(
            onDismissRequest = { mostrarDialogoSalir = false },
            title = { Text("¿Salir sin guardar?") },
            text = { Text("Los ingredientes, pasos y cambios realizados se perderán.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        mostrarDialogoSalir = false
                        viewModel.resetearEstado()   // Descartar todo
                        alVolver()                   // Salir de la pantalla
                    }
                ) {
                    Text("Sí, salir")
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarDialogoSalir = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    // ============================================
    // Cargar listas base (ingredientes / medidas)
    // ============================================
    LaunchedEffect(Unit) {
        viewModel.cargarIngredientesBD()
        viewModel.cargarMedidasBD()
    }

    // ============================================
    // Estado del ViewModel
    // ============================================
    val nombre by viewModel.nombre.collectAsState()
    val descripcion by viewModel.descripcion.collectAsState()
    val porciones by viewModel.porciones.collectAsState()
    val tiempoPrep by viewModel.tiempoPrep.collectAsState()
    val ingredientes by viewModel.ingredientes.collectAsState()
    val pasos by viewModel.pasos.collectAsState()

    // ============================================
    // Mensaje de error de validación
    // ============================================
    var mensajeError by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            BarraSuperiorConBack(
                titulo = "Crear / Editar receta",
                onBack = { mostrarDialogoSalir = true }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    // -------- VALIDACIÓN ANTES DE GUARDAR --------
                    val errores = mutableListOf<String>()

                    if (nombre.isBlank()) {
                        errores += "La receta debe tener un nombre."
                    }
                    if (ingredientes.isEmpty()) {
                        errores += "Debe añadir al menos un ingrediente."
                    }
                    if (pasos.isEmpty()) {
                        errores += "Debe añadir al menos un paso."
                    }

                    if (errores.isNotEmpty()) {
                        // Mostramos mensaje en la pantalla, no guardamos
                        mensajeError = errores.joinToString("\n")
                    } else {
                        // Todo correcto → limpiamos error y guardamos
                        mensajeError = null
                        viewModel.guardarRecetaCompleta { idGuardado ->
                            alGuardarReceta(idGuardado)
                        }
                    }
                }
            ) {
                Text("Guardar")
            }
        }
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            // ---------- MENSAJE DE ERROR (SI LO HAY) ----------
            if (mensajeError != null) {
                item {
                    Text(
                        text = mensajeError!!,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            // ---------------- NOMBRE ----------------
            item {
                OutlinedTextField(
                    value = nombre,
                    onValueChange = viewModel::actualizarNombre,
                    label = { Text("Nombre de la receta") },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // ---------------- DESCRIPCIÓN ----------------
            item {
                OutlinedTextField(
                    value = descripcion,
                    onValueChange = viewModel::actualizarDescripcion,
                    label = { Text("Descripción") },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // ---------------- PORCIONES ----------------
            item {
                OutlinedTextField(
                    value = porciones.toString(),
                    onValueChange = { it.toIntOrNull()?.let(viewModel::actualizarPorciones) },
                    label = { Text("Porciones") },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // ---------------- TIEMPO DE PREPARACIÓN ----------------
            item {
                OutlinedTextField(
                    value = tiempoPrep.toString(),
                    onValueChange = { it.toIntOrNull()?.let(viewModel::actualizarTiempoPrep) },
                    label = { Text("Tiempo preparación (min)") },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // ---------------- INGREDIENTES ----------------
            item {
                Text("Ingredientes", style = MaterialTheme.typography.titleMedium)
            }

            itemsIndexed(ingredientes) { index, ing ->

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {

                        Column(Modifier.weight(1f)) {

                            val nombreIng = viewModel.obtenerNombreIngrediente(ing.ingredienteId)
                            val unidad = viewModel.obtenerUnidadDeIngrediente(ing.medidaId)

                            Text(nombreIng)
                            Text("${ing.cantidad} $unidad")
                        }

                        IconButton(onClick = { viewModel.eliminarIngrediente(index) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Eliminar")
                        }
                    }
                }
            }

            item {
                Button(
                    onClick = { navController.navigate(RutasPantallas.AñadirIngrediente.ruta) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Añadir")
                    Spacer(Modifier.width(8.dp))
                    Text("Añadir ingrediente")
                }
            }

            // ---------------- PASOS ----------------
            item {
                Text("Pasos", style = MaterialTheme.typography.titleMedium)
            }

            itemsIndexed(pasos) { index, paso ->

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {

                    Row(
                        modifier = Modifier
                            .padding(12.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {

                        // --------- COLUMNA IZQUIERDA: INFO DEL PASO ---------
                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                "Paso ${paso.orden}",
                                style = MaterialTheme.typography.titleMedium
                            )

                            Text(
                                paso.descripcion,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }

                        // --------- COLUMNA DERECHA: BOTONES ---------
                        Column(
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {

                            // Subir
                            IconButton(
                                onClick = { viewModel.subirPaso(index) },
                                enabled = index > 0
                            ) {
                                Icon(
                                    imageVector = Icons.Default.KeyboardArrowUp,
                                    contentDescription = "Subir paso"
                                )
                            }

                            // Bajar
                            IconButton(
                                onClick = { viewModel.bajarPaso(index) },
                                enabled = index < pasos.lastIndex
                            ) {
                                Icon(
                                    imageVector = Icons.Default.KeyboardArrowDown,
                                    contentDescription = "Bajar paso"
                                )
                            }

                            // Eliminar
                            IconButton(
                                onClick = { viewModel.eliminarPaso(index) }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Eliminar paso"
                                )
                            }
                        }
                    }
                }
            }

            item {
                Button(
                    onClick = { navController.navigate(RutasPantallas.AñadirPaso.ruta) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Añadir Paso")
                    Spacer(Modifier.width(8.dp))
                    Text("Añadir paso")
                }
            }


            item {
                Button(
                    onClick = { navController.navigate(RutasPantallas.AñadirPaso.ruta) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Añadir Paso")
                    Spacer(Modifier.width(8.dp))
                    Text("Añadir paso")
                }
            }

            // ---------------- ETIQUETAS (FUTURO) ----------------
            item {
                Text("Etiquetas", style = MaterialTheme.typography.titleMedium)
                Text("(Próxima implementación)")
            }
        }
    }
}
