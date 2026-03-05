package com.example.nutrichef

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.nutrichef.datos.baseDatos.BaseDatos
import com.example.nutrichef.datos.repositorio.NutriChefRepository
import com.example.nutrichef.ui.ingredientes.DetalleIngredienteViewModel
import com.example.nutrichef.ui.ingredientes.PantallaDetalleIngrediente

// Pantallas UI
import com.example.nutrichef.ui.home.PantallaHome

import com.example.nutrichef.ui.listaRecetas.*
import com.example.nutrichef.ui.detalleReceta.*
import com.example.nutrichef.ui.ingredientes.*
import com.example.nutrichef.ui.etiquetas.*
import com.example.nutrichef.ui.libros.*

import com.example.nutrichef.ui.recetaCompleta.*
import com.example.nutrichef.ui.navegacion.RutasPantallas
import com.example.nutrichef.ui.theme.NutriChefTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        enableEdgeToEdge()

        setContent {

            NutriChefTheme {

                val navController = rememberNavController()

                Surface(color = MaterialTheme.colorScheme.background) {

                    // BD + Repositorio compartido
                    val context = LocalContext.current
                    val baseDatos = BaseDatos.obtenerInstancia(context)
                    val repositorio = NutriChefRepository.obtenerInstancia(baseDatos)

                    // ViewModel compartido para crear / editar recetas
                    val recetaCompletaVM: RecetaCompletaViewModel = viewModel(
                        factory = RecetaCompletaViewModelFactory(repositorio)
                    )

                    NavHost(
                        navController = navController,
                        startDestination = RutasPantallas.Home.ruta
                    ) {

                        // -------------------- HOME --------------------
                        composable(RutasPantallas.Home.ruta) {
                            PantallaHome(
                                navController = navController,
                                onCrearReceta = {
                                    recetaCompletaVM.resetearEstado() //Resetea el ViewModel
                                    navController.navigate(RutasPantallas.CrearRecetaCompleta.ruta)
                                }
                            )
                        }

                        // ---------------- LISTA DE RECETAS ----------------
                        composable(RutasPantallas.ListaRecetas.ruta) {
                            val vm: RecetasViewModel = viewModel(
                                factory = RecetasViewModelFactory(repositorio)
                            )
                            PantallaListaRecetas(navController, vm)
                        }

                        // ---------------- DETALLE RECETA ----------------
                        composable(
                            route = RutasPantallas.DetalleReceta.ruta,
                            arguments = listOf(navArgument("idReceta") { type = NavType.IntType })
                        ) { backStackEntry ->

                            val id = backStackEntry.arguments!!.getInt("idReceta")

                            val vm: DetalleRecetaViewModel = viewModel(
                                factory = DetalleRecetaViewModelFactory(repositorio)
                            )

                            PantallaDetalleReceta(
                                navController = navController,
                                recetaId = id,
                                viewModel = vm
                            )
                        }

                        // ---------------- CREAR RECETA COMPLETA ----------------
                        composable(RutasPantallas.CrearRecetaCompleta.ruta) {
                            PantallaCrearRecetaCompleta(
                                navController = navController,
                                viewModel = recetaCompletaVM,
                                alGuardarReceta = { navController.popBackStack() },
                                alVolver = { navController.popBackStack() }
                            )
                        }


                        // ---------------- EDITAR RECETA COMPLETA ----------------
                        composable(
                            route = RutasPantallas.EditarReceta.ruta,
                            arguments = listOf(
                                navArgument("idReceta") { type = NavType.IntType }
                            )
                        ) { backStackEntry ->

                            val id = backStackEntry.arguments!!.getInt("idReceta")

                            // 🔥 Ahora dejamos que el ViewModel decida si recarga o no
                            androidx.compose.runtime.LaunchedEffect(id) {
                                recetaCompletaVM.cargarRecetaParaEdicion(id)
                            }

                            PantallaCrearRecetaCompleta(
                                navController = navController,
                                viewModel = recetaCompletaVM,
                                alGuardarReceta = { idGuardado ->

                                    navController.navigate(
                                        RutasPantallas.DetalleReceta.crearRuta(idGuardado.toInt())
                                    ) {
                                        popUpTo(RutasPantallas.DetalleReceta.ruta) {
                                            inclusive = true
                                        }
                                    }
                                },
                                alVolver = { navController.popBackStack() }
                            )
                        }

                        // ---------------- INGREDIENTES ----------------
                        composable(RutasPantallas.Ingredientes.ruta) {
                            val vm: IngredientesViewModel = viewModel(
                                factory = IngredientesViewModelFactory(repositorio)
                            )
                            PantallaListaIngredientes(navController, vm)
                        }

                        composable(RutasPantallas.CrearIngrediente.ruta) {
                            val vm: IngredientesViewModel = viewModel(
                                factory = IngredientesViewModelFactory(repositorio)
                            )
                            PantallaCrearIngrediente(
                                navController = navController,
                                viewModel = vm,
                                recetaViewModel = null
                            )
                        }

                        // ---------------- DETALLE INGREDIENTE ----------------
                        composable(
                            route = RutasPantallas.DetalleIngrediente.ruta,
                            arguments = listOf(
                                navArgument("idIngrediente") { type = NavType.IntType }
                            )
                        ) { backStackEntry ->
                            val id = backStackEntry.arguments!!.getInt("idIngrediente")
                            val vm: DetalleIngredienteViewModel = viewModel(
                                factory = DetalleIngredienteViewModelFactory(repositorio)
                            )

                            PantallaDetalleIngrediente(
                                navController = navController,
                                ingredienteId = id,
                                viewModel = vm
                            )
                        }

                        // ---- SUBPANTALLA: AÑADIR INGREDIENTE A RECETA ----
                        composable(RutasPantallas.AñadirIngrediente.ruta) {
                            PantallaAñadirIngrediente(
                                navController = navController,
                                viewModel = recetaCompletaVM
                            )
                        }

                        // ---- SUBPANTALLA: CREAR INGREDIENTE DESDE RECETA ----
                        composable(RutasPantallas.CrearIngredienteDesdeReceta.ruta) {
                            val vm: IngredientesViewModel = viewModel(
                                factory = IngredientesViewModelFactory(repositorio)
                            )
                            PantallaCrearIngrediente(
                                navController = navController,
                                viewModel = vm,
                                recetaViewModel = recetaCompletaVM
                            )
                        }

                        // ---------------- SUBPANTALLA: AÑADIR PASO ----------------
                        composable(RutasPantallas.AñadirPaso.ruta) {
                            PantallaAñadirPaso(
                                navController = navController,
                                viewModel = recetaCompletaVM
                            )
                        }

                        // ---------------- ETIQUETAS ----------------
                        composable(RutasPantallas.Etiquetas.ruta) {
                            val vm: EtiquetasViewModel = viewModel(
                                factory = EtiquetasViewModelFactory(repositorio)
                            )
                            PantallaListaEtiquetas(navController, vm)
                        }

                        // ---------------- LIBROS ----------------
                        composable(RutasPantallas.Libros.ruta) {
                            val vm: LibrosViewModel = viewModel(
                                factory = LibrosViewModelFactory(repositorio)
                            )
                            PantallaListaLibros(navController, vm)
                        }
                    }
                }
            }
        }
    }
}
