package com.example.nutrichef.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.nutrichef.ui.navegacion.RutasPantallas

@Composable
fun PantallaHome(
    navController: NavHostController,
    onCrearReceta: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        // Título aplicación
        Text(
            text = "NutriChef",
            style = MaterialTheme.typography.headlineLarge
        )

        Spacer(Modifier.height(12.dp))

        // Botón: Lista de recetas
        Button(
            onClick = {
                navController.navigate(RutasPantallas.ListaRecetas.ruta)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Ver recetas")
        }

        // Botón: Crear receta nueva
        Button(
            onClick = onCrearReceta,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Crear nueva receta")
        }

        // Botón: Lista de ingredientes
        Button(
            onClick = {
                navController.navigate(RutasPantallas.Ingredientes.ruta)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Ingredientes")
        }

        // Botón: Lista de etiquetas
        Button(
            onClick = {
                navController.navigate(RutasPantallas.Etiquetas.ruta)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Etiquetas")
        }

        // Botón: Lista de libros (cookbooks)
        Button(
            onClick = {
                navController.navigate(RutasPantallas.Libros.ruta)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Libros")
        }
    }
}
