package com.example.nutrichef.ui.ingredientes

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.nutrichef.ui.componentes.BarraSuperiorConBack
import com.example.nutrichef.ui.navegacion.RutasPantallas
import com.example.nutrichef.ui.textos.MensajesIngrediente

@Composable
fun PantallaDetalleIngrediente(
    navController: NavHostController,
    ingredienteId: Int,
    viewModel: DetalleIngredienteViewModel
) {
    val ingrediente by viewModel.ingrediente.collectAsState()

    var mostrarDialogoEliminar by remember { mutableStateOf(false) }
    var mostrarDialogoBloqueo by remember { mutableStateOf(false) }

    // Cargar ingrediente solo una vez
    LaunchedEffect(ingredienteId) {
        viewModel.cargarIngrediente(ingredienteId)
    }

    // ------------------------------
    // DIÁLOGO: Confirmar eliminación
    // ------------------------------
    if (mostrarDialogoEliminar) {
        AlertDialog(
            onDismissRequest = { mostrarDialogoEliminar = false },
            title = { Text("Eliminar ingrediente") },
            text = {
                Text("Esta acción eliminará permanentemente el ingrediente. ¿Desea continuar?")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        mostrarDialogoEliminar = false
                        viewModel.eliminarIngrediente { exito, estabaEnUso ->
                            if (exito) {
                                navController.popBackStack()
                            } else if (estabaEnUso) {
                                // Mostramos el diálogo de bloqueo
                                mostrarDialogoBloqueo = true
                            } else {
                                // Error genérico: podrías añadir un Snackbar si quieres
                            }
                        }
                    }
                ) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarDialogoEliminar = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    // --------------------------------------
    // DIÁLOGO: Ingrediente en uso (bloqueado)
    // --------------------------------------
    if (mostrarDialogoBloqueo) {
        val mensajeBloqueo = """
            El ingrediente está siendo utilizado en una o más recetas.

            Para eliminarlo, primero debes quitarlo o sustituirlo en todas las recetas donde aparece.

            Como alternativa, puedes crear distintas versiones del ingrediente
            Por ejemplo: 
            “Huevo pequeño”, “Huevo mediano”, “Huevo grande”.
            
        """.trimIndent()

        AlertDialog(
            onDismissRequest = { mostrarDialogoBloqueo = false },
            title = { Text("No es posible eliminar este ingrediente") },
            text = { Text(mensajeBloqueo) },
            confirmButton = {
                TextButton(onClick = { mostrarDialogoBloqueo = false }) {
                    Text("Entendido")
                }
            }
        )
    }

    // ------------------------------
    // UI PRINCIPAL
    // ------------------------------
    Scaffold(
        topBar = {
            BarraSuperiorConBack(
                titulo = ingrediente?.nombre ?: "Ingrediente",
                onBack = { navController.popBackStack() }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            val ing = ingrediente
            if (ing == null) {
                Text("Cargando ingrediente…")
                return@Column
            }

            // NOMBRE
            Text(ing.nombre, style = MaterialTheme.typography.headlineSmall)

            // DESCRIPCIÓN
            ing.descripcion?.let {
                Text(it, style = MaterialTheme.typography.bodyMedium)
            }

            // Unidad base (nombre y medida)
            val nombreMedida by viewModel.nombreMedida.collectAsState()
            if (nombreMedida.isNotBlank()) {
                Text(
                    "Unidad base: $nombreMedida",
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Divider()

            // MACROS
            Text("Información nutricional (por 100 g)", style = MaterialTheme.typography.titleMedium)
            Text("Proteínas: ${ing.proteinasPorMedida ?: "—"} g")
            Text("Carbohidratos: ${ing.carbohidratosPorMedida ?: "—"} g")
            Text("Grasas: ${ing.grasasPorMedida ?: "—"} g")

            Spacer(Modifier.height(20.dp))


            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // BOTÓN EDITAR
                Button(
                    onClick = {
                        navController.navigate(
                            RutasPantallas.EditarIngrediente.crearRuta(ingredienteId)
                        )
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Editar")
                }

                // BOTÓN ELIMINAR
                Button(
                    onClick = { mostrarDialogoEliminar = true },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer,
                        contentColor = MaterialTheme.colorScheme.onErrorContainer
                    )
                ) {
                    Text("Eliminar")
                }
            }

        }
    }
}
