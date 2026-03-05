package com.example.nutrichef.ui.recetaCompleta

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.nutrichef.ui.componentes.BarraSuperiorConBack

@Composable
fun PantallaAñadirPaso(
    navController: NavHostController,
    viewModel: RecetaCompletaViewModel
) {

    var descripcion by remember { mutableStateOf("") }
    var tiempoMinutos by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            BarraSuperiorConBack(
                titulo = "Añadir paso",
                onBack = { navController.popBackStack() }  // ← SIN DIÁLOGO, SIN RESET
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

            // ---------------- DESCRIPCIÓN ----------------
            OutlinedTextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                label = { Text("Descripción del paso") },
                modifier = Modifier.fillMaxWidth()
            )

            // ---------------- TIEMPO OPCIONAL ----------------
            OutlinedTextField(
                value = tiempoMinutos,
                onValueChange = { tiempoMinutos = it },
                label = { Text("Tiempo (min) - opcional") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(20.dp))

            // ---------------- BOTÓN AÑADIR ----------------
            Button(
                onClick = {

                    val tiempo: Int? = tiempoMinutos.toIntOrNull()

                    viewModel.agregarPaso(
                        descripcion = descripcion,
                        tiempoMin = tiempo
                    )

                    // Volver sin resetear nada
                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = descripcion.isNotBlank()
            ) {
                Text("Añadir paso")
            }
        }
    }
}
