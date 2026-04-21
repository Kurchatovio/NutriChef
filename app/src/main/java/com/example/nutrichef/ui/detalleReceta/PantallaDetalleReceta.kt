package com.example.nutrichef.ui.detalleReceta

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.nutrichef.ui.componentes.BarraSuperiorConBack
import com.example.nutrichef.ui.navegacion.RutasPantallas

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaDetalleReceta(
    navController: NavHostController,
    recetaId: Int,
    viewModel: DetalleRecetaViewModel
) {

    // Cargar datos al entrar
    LaunchedEffect(recetaId) {
        viewModel.cargarReceta(recetaId)
        viewModel.cargarIngredientes(recetaId)
        viewModel.cargarPasos(recetaId)
    }

    val receta by viewModel.receta.collectAsState()
    val ingredientes by viewModel.ingredientes.collectAsState()
    val pasos by viewModel.pasos.collectAsState()
    val eliminacionOk by viewModel.eliminacionOk.collectAsState()
    val resumenNutricional by viewModel.resumenNutricional.collectAsState()

    // Estado para mostrar el diálogo
    var mostrarDialogoEliminar by remember { mutableStateOf(false) }

    // Cuando se elimina → volver automáticamente
    LaunchedEffect(eliminacionOk) {
        if (eliminacionOk == true) {
            viewModel.resetEliminarEstado()
            navController.popBackStack()
        }
    }

    Scaffold(
        topBar = {
            BarraSuperiorConBack(
                titulo = "Detalle de receta",
                onBack = { navController.popBackStack() }
            )
        },

        // ================================
        //   BOTONES FIJOS ABAJO (NUEVO)
        // ================================
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                // ------ BOTÓN ELIMINAR ------
                Button(
                    onClick = { mostrarDialogoEliminar = true },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    ),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Delete, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Eliminar")
                }

                // ------ BOTÓN EDITAR ------
                Button(
                    onClick = {
                        navController.navigate(
                            RutasPantallas.EditarReceta.crearRuta(recetaId)
                        )
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Edit, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Editar")
                }
            }
        }

    ) { padding ->

        if (receta == null) {
            Box(Modifier.padding(padding).padding(16.dp)) {
                Text("Cargando…")
            }
            return@Scaffold
        }

        // ===========================
        //     CONTENIDO PRINCIPAL
        // ===========================
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // ------- NOMBRE + DESCRIPCIÓN + PORCIONES + TIEMPO -------
            item {
                Text(receta!!.nombre, style = MaterialTheme.typography.headlineMedium)
                receta!!.descripcion?.let {
                    Spacer(Modifier.height(4.dp))
                    Text(it, style = MaterialTheme.typography.bodyLarge)
                }
                Spacer(Modifier.height(8.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    receta!!.porciones?.let {
                        Text(
                            "🍽 $it porciones",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    receta!!.tiempoPreparacionMin?.let {
                        Text(
                            "⏱ $it min",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }

            // -------------- INGREDIENTES --------------
            item {
                Text("Ingredientes", style = MaterialTheme.typography.titleMedium)
            }

            items(ingredientes) { ing ->
                Text("- ${ing.nombre}: ${ing.cantidad} ${ing.unidad}")
            }


            // ---------------- INFORMACIÓN NUTRICIONAL ----------------
            resumenNutricional?.let { resumen ->
                item {
                    Divider()
                    Spacer(Modifier.height(4.dp))
                    Text(
                        "Información nutricional",
                        style = MaterialTheme.typography.titleMedium
                    )
                    if (!resumen.completo) {
                        Text(
                            "⚠ Algunos ingredientes no tienen macros definidos. " +
                                    "Los valores mostrados son aproximados.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Spacer(Modifier.height(8.dp))
                    val porciones = receta!!.porciones?.takeIf { it > 0 } ?: 1
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Total", style = MaterialTheme.typography.labelMedium)
                            Text("Calorías: ${"%.0f".format(resumen.proteinas * 4 + resumen.carbohidratos * 4 + resumen.grasas * 9)} kcal")
                            Text("Proteínas: ${"%.1f".format(resumen.proteinas)} g")
                            Text("Carbohidratos: ${"%.1f".format(resumen.carbohidratos)} g")
                            Text("Grasas: ${"%.1f".format(resumen.grasas)} g")
                        }
                        Column {
                            Text("Por porción", style = MaterialTheme.typography.labelMedium)
                            Text("Calorías: ${"%.0f".format((resumen.proteinas * 4 + resumen.carbohidratos * 4 + resumen.grasas * 9) / porciones)} kcal")
                            Text("Proteínas: ${"%.1f".format(resumen.proteinas / porciones)} g")
                            Text("Carbohidratos: ${"%.1f".format(resumen.carbohidratos / porciones)} g")
                            Text("Grasas: ${"%.1f".format(resumen.grasas / porciones)} g")
                        }
                    }
                    Spacer(Modifier.height(4.dp))
                    Divider()
                }
            }


            // ------------------- PASOS -------------------
            item {
                Text("Pasos", style = MaterialTheme.typography.titleMedium)
            }

            items(pasos) { paso ->
                Column {
                    Text("${paso.orden}. ${paso.descripcion}")
                    paso.tiempoMin?.let { Text("(${it} min)") }
                }
            }

            item { Spacer(Modifier.height(80.dp)) } // Para que no tape el contenido
        }
    }

    // ============================
    //   DIÁLOGO DE CONFIRMACIÓN
    // ============================
    if (mostrarDialogoEliminar) {
        AlertDialog(
            onDismissRequest = { mostrarDialogoEliminar = false },
            title = { Text("¿Eliminar receta?") },
            text = { Text("Esta acción eliminará permanentemente la receta completa. ¿Deseas continuar?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        mostrarDialogoEliminar = false
                        viewModel.borrarRecetaCompleta(recetaId)
                    }
                ) {
                    Text("Eliminar", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarDialogoEliminar = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}
