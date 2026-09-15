package pe.edu.upeu.bibliomobil.presentation.libro

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pe.edu.upeu.bibliomobil.domain.usecase.LibroInvalidoException
import pe.edu.upeu.bibliomobil.domain.usecase.ListarLibrosUseCase
import pe.edu.upeu.bibliomobil.domain.usecase.RegistrarLibroUseCase

class LibroViewModel(
    private val registrarLibroUseCase: RegistrarLibroUseCase,
    private val listarLibrosUseCase: ListarLibrosUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LibroUiState())
    val uiState: StateFlow<LibroUiState> = _uiState.asStateFlow()

    init {
        cargarLibros()
    }

    fun cargarLibros() {
        _uiState.update { it.copy(fase = FaseLibro.Cargando) }
        viewModelScope.launch {
            try {
                val libros = listarLibrosUseCase.ejecutar()
                _uiState.update { estado ->
                    if (libros.isEmpty()) {
                        estado.copy(fase = FaseLibro.SinLibros)
                    } else {
                        estado.copy(fase = FaseLibro.ConLibros(libros.map { it.aUi() }))
                    }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(fase = FaseLibro.Error(e.message ?: "Error desconocido al cargar catálogo")) }
            }
        }
    }

    fun onTituloChange(nuevoValor: String) {
        _uiState.update { it.copy(formulario = it.formulario.copy(titulo = nuevoValor, tituloError = null)) }
    }

    fun onAutorChange(nuevoValor: String) {
        _uiState.update { it.copy(formulario = it.formulario.copy(autor = nuevoValor, autorError = null)) }
    }

    fun onIsbnChange(nuevoValor: String) {
        _uiState.update { it.copy(formulario = it.formulario.copy(isbn = nuevoValor, isbnError = null)) }
    }

    fun onTotalEjemplaresChange(nuevoValor: String) {
        _uiState.update { it.copy(formulario = it.formulario.copy(totalEjemplares = nuevoValor, ejemplaresError = null)) }
    }

    fun registrar() {
        // Bloqueo de doble toque o clicks repetidos rápidos
        if (_uiState.value.registrando) return

        _uiState.update { it.copy(registrando = true, mensajeExito = null) }

        viewModelScope.launch {
            val form = _uiState.value.formulario
            val resultado = registrarLibroUseCase.ejecutar(
                titulo = form.titulo,
                autor = form.autor,
                isbn = form.isbn,
                totalEjemplares = form.totalEjemplares
            )

            resultado.fold(
                onSuccess = {
                    _uiState.update { estado ->
                        estado.copy(
                            registrando = false,
                            formulario = FormularioLibro(), // Limpia el formulario
                            mensajeExito = "¡Libro registrado exitosamente!"
                        )
                    }
                    cargarLibros() // Recarga catálogo actualizado
                },
                onFailure = { excepcion ->
                    _uiState.update { estado ->
                        if (excepcion is LibroInvalidoException) {
                            estado.copy(
                                registrando = false,
                                formulario = estado.formulario.copy(
                                    tituloError = excepcion.errores.tituloError,
                                    autorError = excepcion.errores.autorError,
                                    isbnError = excepcion.errores.isbnError,
                                    ejemplaresError = excepcion.errores.ejemplaresError
                                )
                            )
                        } else {
                            estado.copy(
                                registrando = false,
                                fase = FaseLibro.Error(excepcion.message ?: "Error al registrar libro")
                            )
                        }
                    }
                }
            )
        }
    }

    fun limpiarMensajeExito() {
        _uiState.update { it.copy(mensajeExito = null) }
    }
}
