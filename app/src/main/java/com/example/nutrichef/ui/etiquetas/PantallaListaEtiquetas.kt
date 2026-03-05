package com.example.nutrichef.ui.etiquetas

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.nutrichef.datos.entidades.Etiqueta
import com.example.nutrichef.ui.componentes.BarraSuperiorConBack

@Composable
fun PantallaListaEtiquetas(
    navController: NavHostController,
    viewModel: EtiquetasViewModel
) {
    val etiquetas by viewModel.etiquetas.collectAsState()

    // Cargar etiquetas cuando la pantalla aparece
    LaunchedEffect(Unit) {
        viewModel.cargarEtiquetas()
    }

    Scaffold(
        topBar = {
            BarraSuperiorConBack(
                titulo = "Etiquetas",
                onBack = { navController.popBackStack() }
            )
        }
    ) { padding ->

        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {

            Text(
                text = "Lista de etiquetas",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn {
                items(etiquetas) { etiqueta ->
                    EtiquetaItem(
                        etiqueta = etiqueta,
                        onClick = { /* Más adelante podremos navegar o editar */ }
                    )
                }
            }
        }
    }
}

@Composable
fun EtiquetaItem(etiqueta: Etiqueta, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable { onClick() }
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(
                text = etiqueta.nombre,
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}
