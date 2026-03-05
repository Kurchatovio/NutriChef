package com.example.nutrichef.ui.ingredientes

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.nutrichef.datos.entidades.Ingrediente
import com.example.nutrichef.datos.entidades.Medida
import com.example.nutrichef.ui.componentes.BarraSuperiorConBack
import com.example.nutrichef.ui.recetaCompleta.RecetaCompletaViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaCrearIngrediente(
    navController: NavHostController,
    viewModel: IngredientesViewModel,
    recetaViewModel: RecetaCompletaViewModel? = null   // null cuando venimos de "Lista ingredientes"
) {
    // ------------------------------------------------------------
    // Snackbar + corrutina para mensajes y operaciones suspend
    // ------------------------------------------------------------
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // ------------------------------------------------------------
    // Estados locales de formulario
    // ------------------------------------------------------------
    // Si venimos desde PantallaAñadirIngrediente, intentamos pre-rellenar el nombre
    val nombreTempState = recetaViewModel?.nombreIngredienteTemporal?.collectAsState()
    var nombre by remember(nombreTempState?.value) {
        mutableStateOf(nombreTempState?.value ?: "")
    }

    var descripcion by remember { mutableStateOf("") }

    // Medidas
    LaunchedEffect(Unit) {
        viewModel.cargarMedidas()
    }
    val medidas by viewModel.medidas.collectAsState()
    var medidaSeleccionada by remember { mutableStateOf<Medida?>(null) }
    var menuAbierto by remember { mutableStateOf(false) }

    // Macros opcionales (por 100g / 100ml / unidad, según tipo de medida)
    var carbohidratos by remember { mutableStateOf("") }
    var grasas by remember { mutableStateOf("") }
    var proteinas by remember { mutableStateOf("") }

    // Texto informativo según tipo de medida
    val textoUnidadMacros = remember(medidaSeleccionada) {
        when (medidaSeleccionada?.tipo) {
            "masa" -> "Valores por cada 100 g"
            "volumen" -> "Valores por cada 100 ml"
            "cantidad" -> "Valores por unidad"
            else -> null
        }
    }

    Scaffold(
        topBar = {
            BarraSuperiorConBack(
                titulo = "Crear ingrediente",
                onBack = {
                    // Si el usuario se va hacia atrás, simplemente volvemos.
                    // (Opcional: podríamos limpiar el nombre temporal, pero no es obligatorio.)
                    navController.popBackStack()
                }
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // ------------------------------------------------------------
            // NOMBRE
            // ------------------------------------------------------------
            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre del ingrediente") },
                modifier = Modifier.fillMaxWidth()
            )

            // ------------------------------------------------------------
            // DESCRIPCIÓN
            // ------------------------------------------------------------
            OutlinedTextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                label = { Text("Descripción (opcional)") },
                modifier = Modifier.fillMaxWidth()
            )

            // ------------------------------------------------------------
            // SELECTOR DE MEDIDA
            // ------------------------------------------------------------
            ExposedDropdownMenuBox(
                expanded = menuAbierto,
                onExpandedChange = { menuAbierto = !menuAbierto }
            ) {
                OutlinedTextField(
                    value = medidaSeleccionada?.nombre ?: "",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Unidad de medida") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = menuAbierto)
                    },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )

                ExposedDropdownMenu(
                    expanded = menuAbierto,
                    onDismissRequest = { menuAbierto = false }
                ) {
                    medidas.forEach { medida ->
                        DropdownMenuItem(
                            text = { Text(medida.nombre) },
                            onClick = {
                                medidaSeleccionada = medida
                                menuAbierto = false
                            }
                        )
                    }
                }
            }

            // ------------------------------------------------------------
            // MACROS OPCIONALES
            // ------------------------------------------------------------
            Text("Macros (opcional)", style = MaterialTheme.typography.titleMedium)

            if (textoUnidadMacros != null) {
                Text(
                    textoUnidadMacros,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = grasas,
                    onValueChange = { grasas = it },
                    label = { Text("Grasas") },
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = carbohidratos,
                    onValueChange = { carbohidratos = it },
                    label = { Text("Carbs") },
                    modifier = Modifier.weight(1f)
                )

                OutlinedTextField(
                    value = proteinas,
                    onValueChange = { proteinas = it },
                    label = { Text("Proteínas") },
                    modifier = Modifier.weight(1f)
                )
            }

            // ------------------------------------------------------------
            // BOTÓN GUARDAR
            // ------------------------------------------------------------
            Button(
                onClick = {
                    // Validaciones básicas
                    if (nombre.isBlank()) {
                        scope.launch {
                            snackbarHostState.showSnackbar("El nombre no puede estar vacío.")
                        }
                        return@Button
                    }
                    if (medidaSeleccionada == null) {
                        scope.launch {
                            snackbarHostState.showSnackbar("Debes seleccionar una unidad de medida.")
                        }
                        return@Button
                    }

                    // Construimos el ingrediente con macros opcionales
                    val nuevo = Ingrediente(
                        id = 0,
                        nombre = nombre,
                        descripcion = descripcion.ifBlank { null },
                        medidaId = medidaSeleccionada!!.id,
                        proteinasPorMedida = proteinas.toDoubleOrNull(),
                        carbohidratosPorMedida = carbohidratos.toDoubleOrNull(),
                        grasasPorMedida = grasas.toDoubleOrNull(),
                        imagenUri = null
                    )

                    // Llamamos a la función suspend del ViewModel dentro de una corrutina
                    scope.launch {
                        try {
                            // Esta función la tienes como:
                            // suspend fun insertarIngrediente(ingrediente: Ingrediente): Long
                            val nuevoId = viewModel.insertarIngrediente(nuevo)

                            // Si venimos desde la creación de receta:
                            if (recetaViewModel != null) {
                                // Informamos a RecetaCompletaViewModel para que la subpantalla lo seleccione
                                recetaViewModel.setUltimoIngredienteCreadoId(nuevoId.toInt())
                                // Limpiamos el nombre temporal
                                recetaViewModel.setNombreIngredienteTemporal("")
                                // Volvemos a PantallaAñadirIngrediente
                                navController.popBackStack()
                            } else {
                                // Si venimos desde ListaIngredientes normal, solo volvemos atrás
                                navController.popBackStack()
                            }
                        } catch (e: Exception) {
                            snackbarHostState.showSnackbar("Error al guardar ingrediente.")
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar ingrediente")
            }
        }
    }
}
