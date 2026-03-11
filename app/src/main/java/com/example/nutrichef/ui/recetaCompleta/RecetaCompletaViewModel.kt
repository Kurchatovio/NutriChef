package com.example.nutrichef.ui.recetaCompleta

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nutrichef.datos.entidades.Ingrediente
import com.example.nutrichef.datos.entidades.Medida
import com.example.nutrichef.datos.entidades.PasoReceta
import com.example.nutrichef.datos.entidades.Receta
import com.example.nutrichef.datos.entidades.RecetaIngrediente
import com.example.nutrichef.datos.repositorio.NutriChefRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RecetaCompletaViewModel(
    private val repositorio: NutriChefRepository
) : ViewModel() {

    // =====================================================
    //   ID DE RECETA EN EDICIÓN (null = modo CREAR)
    // =====================================================
    private var recetaIdEnEdicion: Int? = null

    // 🔥 NUEVO: para no recargar desde BD varias veces
    private var datosInicialesCargados: Boolean = false

    // =====================================================
    //                     DATOS DE LA RECETA
    // =====================================================

    private val _nombre = MutableStateFlow("")
    val nombre = _nombre.asStateFlow()

    private val _descripcion = MutableStateFlow("")
    val descripcion = _descripcion.asStateFlow()

    private val _porciones = MutableStateFlow(1)
    val porciones = _porciones.asStateFlow()

    private val _tiempoPrep = MutableStateFlow(0)
    val tiempoPrep = _tiempoPrep.asStateFlow()

    private val _categoria = MutableStateFlow<String?>(null)
    val categoria = _categoria.asStateFlow()

    private val _imagenUri = MutableStateFlow<String?>(null)
    val imagenUri = _imagenUri.asStateFlow()

    // =====================================================
    //             INGREDIENTES DE LA RECETA (UI)
    // =====================================================

    private val _ingredientes = MutableStateFlow<List<RecetaIngrediente>>(emptyList())
    val ingredientes = _ingredientes.asStateFlow()

    // =====================================================
    //             INGREDIENTES DISPONIBLES (BD)
    // =====================================================

    private val _todosIngredientes = MutableStateFlow<List<Ingrediente>>(emptyList())
    val todosIngredientes = _todosIngredientes.asStateFlow()

    // =====================================================
    //                   MEDIDAS DISPONIBLES
    // =====================================================

    private val _todasLasMedidas = MutableStateFlow<List<Medida>>(emptyList())
    val todasLasMedidas = _todasLasMedidas.asStateFlow()

    // =====================================================
    //                   PASOS DE PREPARACIÓN
    // =====================================================

    private val _pasos = MutableStateFlow<List<PasoReceta>>(emptyList())
    val pasos = _pasos.asStateFlow()

    // =====================================================
    //                   ETIQUETAS
    // =====================================================

    private val _etiquetasSeleccionadas = MutableStateFlow<List<Int>>(emptyList())
    val etiquetasSeleccionadas = _etiquetasSeleccionadas.asStateFlow()

    // =====================================================
    //             ESTADO PARA SUBPANTALLAS
    // =====================================================

    private val _nombreIngredienteTemporal = MutableStateFlow<String?>(null)
    val nombreIngredienteTemporal: StateFlow<String?> = _nombreIngredienteTemporal

    fun setNombreIngredienteTemporal(nombre: String?) {
        _nombreIngredienteTemporal.value = nombre
    }

    private val _ultimoIngredienteCreadoId = MutableStateFlow<Int?>(null)
    val ultimoIngredienteCreadoId: StateFlow<Int?> = _ultimoIngredienteCreadoId

    // Guarda el ID del último ingrediente creado y recarga el catálogo si es ingrediente nuevo
    fun setUltimoIngredienteCreadoId(id: Int?) {
        _ultimoIngredienteCreadoId.value = id
        if (id != null) {
            cargarIngredientesBD() // recarga el catálogo cuando se crea un ingrediente nuevo
        }
    }


    // =====================================================
    //                   CARGAR DATOS BD
    // =====================================================

    // Precarga ingredientes y medidas al crearse el ViewModel
    init {
        cargarIngredientesBD()
        cargarMedidasBD()
    }

    fun cargarIngredientesBD() {
        viewModelScope.launch {
            _todosIngredientes.value = repositorio.obtenerTodosLosIngredientes()
        }
    }

    fun cargarMedidasBD() {
        viewModelScope.launch {
            _todasLasMedidas.value = repositorio.obtenerTodasLasMedidas()
        }
    }


    // =====================================================
    //                ACTUALIZAR CAMPOS RECETA
    // =====================================================

    fun actualizarNombre(valor: String) { _nombre.value = valor }
    fun actualizarDescripcion(valor: String) { _descripcion.value = valor }
    fun actualizarPorciones(valor: Int) { _porciones.value = valor }
    fun actualizarTiempoPrep(valor: Int) { _tiempoPrep.value = valor }
    fun actualizarCategoria(valor: String?) { _categoria.value = valor }
    fun actualizarImagen(uri: String?) { _imagenUri.value = uri }

    // =====================================================
    //            MANEJO DE INGREDIENTES DE RECETA
    // =====================================================

    fun agregarIngrediente(
        ingredienteId: Int,
        cantidad: Double,
        medidaId: Int?
    ) {
        val nuevo = RecetaIngrediente(
            recetaId = 0,  // El repo lo corregirá al guardar
            ingredienteId = ingredienteId,
            cantidad = cantidad,
            medidaId = medidaId
        )
        _ingredientes.value = _ingredientes.value + nuevo
    }

    // Elimina un ingrediente de la receta por su ID único
    fun eliminarIngrediente(ingredienteId: Int) {
        _ingredientes.value = _ingredientes.value.filter { it.ingredienteId != ingredienteId }
    }

    fun obtenerNombreIngrediente(id: Int): String {
        return todosIngredientes.value.find { it.id == id }?.nombre ?: "Ingrediente $id"
    }

    // Unidad a partir de medidaId (corregido)
    fun obtenerUnidadDeIngrediente(medidaId: Int?): String {
        medidaId ?: return ""
        val medida = todasLasMedidas.value.find { it.id == medidaId }
        return medida?.nombre ?: ""
    }

    // =====================================================
    //            MANEJO DE PASOS
    // =====================================================

    fun agregarPaso(descripcion: String, tiempoMin: Int? = null) {
        val nuevoPaso = PasoReceta(
            id = 0,
            recetaId = 0, // El repo lo ajustará al guardar
            orden = _pasos.value.size + 1,
            descripcion = descripcion,
            tiempoMin = tiempoMin
        )
        _pasos.value = _pasos.value + nuevoPaso
    }

    fun eliminarPaso(index: Int) {
        if (index !in _pasos.value.indices) return
        val lista = _pasos.value.toMutableList()
        lista.removeAt(index)
        _pasos.value = lista.mapIndexed { i, paso -> paso.copy(orden = i + 1) }
    }

    fun subirPaso(index: Int) {
        if (index <= 0) return
        val lista = _pasos.value.toMutableList()
        val temp = lista[index - 1]
        lista[index - 1] = lista[index]
        lista[index] = temp
        _pasos.value = lista.mapIndexed { i, paso -> paso.copy(orden = i + 1) }
    }

    fun bajarPaso(index: Int) {
        if (index >= _pasos.value.lastIndex) return
        val lista = _pasos.value.toMutableList()
        val temp = lista[index + 1]
        lista[index + 1] = lista[index]
        lista[index] = temp
        _pasos.value = lista.mapIndexed { i, paso -> paso.copy(orden = i + 1) }
    }

    // =====================================================
    //     CARGAR UNA RECETA COMPLETA PARA EDICIÓN
    // =====================================================

    fun cargarRecetaParaEdicion(idReceta: Int) {
        // 🔥 Si ya hemos cargado esta receta, NO recargamos desde BD
        if (datosInicialesCargados && recetaIdEnEdicion == idReceta) {
            return
        }

        recetaIdEnEdicion = idReceta

        viewModelScope.launch {

            val receta = repositorio.obtenerRecetaPorId(idReceta)
            if (receta != null) {
                _nombre.value = receta.nombre
                _descripcion.value = receta.descripcion ?: ""
                _porciones.value = receta.porciones ?: 1
                _tiempoPrep.value = receta.tiempoPreparacionMin ?: 0
                _categoria.value = receta.categoria
                _imagenUri.value = receta.imagenUri
            }

            _ingredientes.value = repositorio.obtenerIngredientesDeReceta(idReceta)
            _pasos.value = repositorio.obtenerPasosPorReceta(idReceta).sortedBy { it.orden }

            // Marcamos que ya hemos cargado datos iniciales para esta receta
            datosInicialesCargados = true
        }
    }

    // =====================================================
    //              GUARDAR LA RECETA COMPLETA
    // =====================================================

    fun guardarRecetaCompleta(onGuardado: (Long) -> Unit) {
        viewModelScope.launch {

            val idExistente = recetaIdEnEdicion

            val receta = Receta(
                id = idExistente ?: 0,
                nombre = _nombre.value,
                descripcion = _descripcion.value,
                porciones = _porciones.value,
                tiempoPreparacionMin = _tiempoPrep.value,
                categoria = _categoria.value,
                imagenUri = _imagenUri.value
            )

            if (idExistente == null) {

                val idNuevo = repositorio.insertarRecetaCompleta(
                    receta = receta,
                    ingredientes = _ingredientes.value,
                    pasos = _pasos.value,
                    etiquetasIds = _etiquetasSeleccionadas.value
                )

                resetearEstado()
                onGuardado(idNuevo)

            } else {

                repositorio.actualizarRecetaCompleta(
                    receta = receta,
                    ingredientes = _ingredientes.value,
                    pasos = _pasos.value,
                    etiquetasIds = _etiquetasSeleccionadas.value
                )

                resetearEstado()
                onGuardado(idExistente.toLong())
            }
        }
    }

    // =====================================================
    //                     LIMPIAR ESTADO
    // =====================================================

    fun resetearEstado() {
        recetaIdEnEdicion = null
        datosInicialesCargados = false   // 🔥 IMPORTANTE

        _nombre.value = ""
        _descripcion.value = ""
        _porciones.value = 1
        _tiempoPrep.value = 0
        _categoria.value = null
        _imagenUri.value = null
        _ingredientes.value = emptyList()
        _pasos.value = emptyList()
        _etiquetasSeleccionadas.value = emptyList()
        _nombreIngredienteTemporal.value = null
        _ultimoIngredienteCreadoId.value = null
    }
}
