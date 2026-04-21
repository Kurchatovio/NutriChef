package com.example.nutrichef.ui.ingredientes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nutrichef.datos.entidades.Ingrediente
import com.example.nutrichef.datos.entidades.Medida
import com.example.nutrichef.datos.repositorio.NutriChefRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.example.nutrichef.util.obtenerNombreMedida
import com.example.nutrichef.util.obtenerAbreviraturaMedida
import com.example.nutrichef.util.sinAcentos

/**
 * ViewModel encargado de la lógica relacionada con los ingredientes.
 *
 * Mantiene el estado observable de los ingredientes (StateFlow) y expone
 * funciones para operar sobre la base de datos a través del Repository.
 */
class IngredientesViewModel(
    private val repositorio: NutriChefRepository
) : ViewModel() {

    // -------------------------------------------------------------
    //              ESTADOS OBSERVABLES EXPUESTOS A LA UI
    // -------------------------------------------------------------

    // Lista de ingredientes
    private val _ingredientes = MutableStateFlow<List<Ingrediente>>(emptyList())
    val ingredientes: StateFlow<List<Ingrediente>> = _ingredientes.asStateFlow()

    // Lista de medidas disponibles (para el dropdown de unidad)
    private val _medidas = MutableStateFlow<List<Medida>>(emptyList())
    val medidas: StateFlow<List<Medida>> = _medidas.asStateFlow()

    // Ingrediente cargado para edición (null si estamos en modo crear)
    private val _ingredienteEnEdicion = MutableStateFlow<Ingrediente?>(null)
    val ingredienteEnEdicion: StateFlow<Ingrediente?> = _ingredienteEnEdicion.asStateFlow()


    // Precarga ingredientes y medidas al crearse el ViewModel
    init {
        cargarIngredientes()
        cargarMedidas()
    }

    // -------------------------------------------------------------
    //                      FUNCIONES PÚBLICAS
    // -------------------------------------------------------------

    /** Carga todos los ingredientes en la base de datos. */
    fun cargarIngredientes() {

        viewModelScope.launch {
            _ingredientes.value = repositorio.obtenerTodosLosIngredientes()
        }
    }


    // Busca ingredientes ignorando acentos y mayúsculas
    fun buscarIngredientes(texto: String) {
        viewModelScope.launch {
            val todos = repositorio.obtenerTodosLosIngredientes()
            _ingredientes.value = todos.filter {
                it.nombre.sinAcentos().contains(texto.sinAcentos(), ignoreCase = true)
            }
        }
    }

    /** Carga todas las medidas disponibles (gramos, ml, taza, etc.). */
    fun cargarMedidas() {
        viewModelScope.launch {
            _medidas.value = repositorio.obtenerTodasLasMedidas()
        }
    }

    // Carga un ingrediente específico por ID para modo edición
    fun cargarIngredientePorId(id: Int) {
        viewModelScope.launch {
            _ingredienteEnEdicion.value = repositorio.obtenerIngredientePorId(id)
        }
    }

    // Devuelve la abreviatura de la medida de un ingrediente
    fun obtenerAbreviaturaDeIngrediente(medidaId: Int): String =
        obtenerAbreviraturaMedida(medidaId, _medidas.value)

    /**
     * Inserta un ingrediente en BD y devuelve el ID autogenerado.
     *
     * IMPORTANTE:
     *  - Es una función suspendida porque se llama desde una corrutina
     *    en la UI (por ejemplo, en PantallaCrearIngrediente).
     */
    suspend fun insertarIngrediente(ingrediente: Ingrediente): Long {
        val id = repositorio.insertarIngrediente(ingrediente)
        // Refrescamos la lista en segundo plano
        cargarIngredientes()
        return id
    }

    /** Actualiza un ingrediente existente. */
    fun actualizarIngrediente(ingrediente: Ingrediente) {
        viewModelScope.launch {
            repositorio.actualizarIngrediente(ingrediente)
            cargarIngredientes()
        }
    }

    /** Elimina un ingrediente. */
    fun eliminarIngrediente(ingrediente: Ingrediente) {
        viewModelScope.launch {
            repositorio.eliminarIngrediente(ingrediente)
            cargarIngredientes()
        }
    }

    // Comprueba si un ingrediente está siendo usado en alguna receta
    suspend fun ingredienteEstaEnUso(id: Int): Boolean =
        repositorio.ingredienteEstaEnUso(id)
}
