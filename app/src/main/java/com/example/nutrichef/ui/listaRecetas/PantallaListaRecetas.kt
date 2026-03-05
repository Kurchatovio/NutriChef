package com.example.nutrichef.ui.listaRecetas

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.nutrichef.ui.navegacion.RutasPantallas
import com.example.nutrichef.datos.entidades.Receta
import com.example.nutrichef.ui.componentes.BarraSuperiorConBack

@Composable
fun PantallaListaRecetas(
    navController: NavHostController,
    viewModel: RecetasViewModel
) {
    val recetas by viewModel.recetas.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.cargarRecetas()
    }

    Scaffold(
        topBar = {
            BarraSuperiorConBack(
                titulo = "Lista de recetas",
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

            // Botón + título están en orden CORRECTO ahora
            Button(
                onClick = { navController.navigate(RutasPantallas.CrearRecetaCompleta.ruta) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Crear nueva receta")
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(recetas) { receta ->
                    RecetaItem(
                        receta = receta,
                        onClick = {
                            navController.navigate(
                                RutasPantallas.DetalleReceta.crearRuta(receta.id)
                            )
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun RecetaItem(receta: Receta, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = receta.nombre, style = MaterialTheme.typography.titleLarge)
            receta.descripcion?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}
