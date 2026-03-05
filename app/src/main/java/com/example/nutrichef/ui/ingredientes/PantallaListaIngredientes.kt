package com.example.nutrichef.ui.ingredientes

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.nutrichef.datos.entidades.Ingrediente
import com.example.nutrichef.ui.componentes.BarraSuperiorConBack
import com.example.nutrichef.ui.navegacion.RutasPantallas

@Composable
fun PantallaListaIngredientes(
    navController: NavHostController,
    viewModel: IngredientesViewModel
) {
    val ingredientes by viewModel.ingredientes.collectAsState()

    // Estado del cuadro de búsqueda
    var textoBusqueda by remember { mutableStateOf("") }

    // Cargar ingredientes al entrar
    LaunchedEffect(Unit) {
        viewModel.cargarIngredientes()
    }

    Scaffold(
        topBar = {
            BarraSuperiorConBack(
                titulo = "Ingredientes",
                onBack = { navController.popBackStack() }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // --------------------------------------------------
            // 🔍 BUSCADOR DE INGREDIENTES
            // --------------------------------------------------
            OutlinedTextField(
                value = textoBusqueda,
                onValueChange = { nuevoTexto ->
                    textoBusqueda = nuevoTexto
                    if (nuevoTexto.isBlank()) {
                        viewModel.cargarIngredientes()
                    } else {
                        viewModel.buscarIngredientes(nuevoTexto)
                    }
                },
                label = { Text("Buscar ingrediente") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // --------------------------------------------------
            // BOTÓN CREAR INGREDIENTE
            // --------------------------------------------------
            Button(
                onClick = {
                    navController.navigate(RutasPantallas.CrearIngrediente.ruta)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Crear nuevo ingrediente")
            }

            // --------------------------------------------------
            // LISTADO DE INGREDIENTES
            // --------------------------------------------------
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(ingredientes) { ingrediente ->
                    IngredienteItem(
                        ingrediente = ingrediente,
                        onClick = {
                            navController.navigate(
                                RutasPantallas.DetalleIngrediente.crearRuta(ingrediente.id)
                            )
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun IngredienteItem(
    ingrediente: Ingrediente,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable { onClick() }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Text(
                text = ingrediente.nombre,
                style = MaterialTheme.typography.titleLarge
            )

            ingrediente.descripcion?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
