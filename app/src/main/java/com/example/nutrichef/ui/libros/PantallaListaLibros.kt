package com.example.nutrichef.ui.libros

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
import com.example.nutrichef.datos.entidades.Libro
import com.example.nutrichef.ui.componentes.BarraSuperiorConBack

@Composable
fun PantallaListaLibros(
    navController: NavHostController,
    viewModel: LibrosViewModel
) {
    val libros by viewModel.libros.collectAsState()

    // Cargar libros al entrar en la pantalla
    LaunchedEffect(Unit) {
        viewModel.cargarLibros()
    }

    Scaffold(
        topBar = {
            BarraSuperiorConBack(
                titulo = "Libros",
                onBack = { navController.popBackStack() }
            )
        }
    ) { padding ->

        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {

            Text(
                text = "Lista de libros",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn {
                items(libros) { libro ->
                    LibroItem(
                        libro = libro,
                        onClick = { /* más adelante abriremos el detalle */ }
                    )
                }
            }
        }
    }
}

@Composable
fun LibroItem(libro: Libro, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable { onClick() }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = libro.nombre,
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}
