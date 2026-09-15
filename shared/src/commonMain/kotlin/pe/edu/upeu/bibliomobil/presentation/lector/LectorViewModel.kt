package pe.edu.upeu.bibliomobil.presentation.lector

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pe.edu.upeu.bibliomobil.domain.usecase.LectorInvalidoException
import pe.edu.upeu.bibliomobil.domain.usecase.ListarLectoresUseCase
import pe.edu.upeu.bibliomobil.domain.usecase.RegistrarLectorUseCase

class LectorViewModel(
    private val registrarLectorUseCase: RegistrarLectorUseCase,
    private val listarLectoresUseCase: ListarLectoresUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LectorUiState())
    val uiState: StateFlow<LectorUiState> = _uiState.asStateFlow()

    init {
        cargarLectores()
    }

    fun cargarLectores() {
        _uiState.update { it.copy(fase = FaseLector.Cargando) }
        viewModelScope.launch {
            try {
                val lectores = listarLectoresUseCase.ejecutar()
                _uiState.update { estado ->
                    if (lectores.isEmpty()) {
                        estado.copy(fase = FaseLector.SinLectores)
                    } else {
                        estado.copy(fase = FaseLector.ConLectores(lectores.map { it.aUi() }))
                    }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(fase = FaseLector.Error(e.message ?: "Error al cargar lectores")) }
            }
        }
    }

    fun onNombreChange(nuevoValor: String) {
        _uiState.update { it.copy(formulario = it.formulario.copy(nombre = nuevoValor, nombreError = null)) }
    }

    fun onDniChange(nuevoValor: String) {
        _uiState.update { it.copy(formulario = it.formulario.copy(dni = nuevoValor, dniError = null)) }
    }

    fun onEmailChange(nuevoValor: String) {
        _uiState.update { it.copy(formulario = it.formulario.copy(email = nuevoValor, emailError = null)) }
    }

    fun onTelefonoChange(nuevoValor: String) {
        _uiState.update { it.copy(formulario = it.formulario.copy(telefono = nuevoValor)) }
    }

    fun registrar() {
        // Protección integrada contra doble toque o clicks rápidos repetidos
        if (_uiState.value.registrando) return

        _uiState.update { it.copy(registrando = true, mensajeExito = null) }

        viewModelScope.launch {
            val form = _uiState.value.formulario
            val resultado = registrarLectorUseCase.ejecutar(
                nombre = form.nombre,
                dni = form.dni,
                email = form.email,
                telefono = form.telefono
            )

            resultado.fold(
                onSuccess = {
                    _uiState.update { estado ->
                        estado.copy(
                            registrando = false,
                            formulario = FormularioLector(), // Resetea el formulario limpiamente
                            mensajeExito = "¡Lector afiliado exitosamente!"
                        )
                    }
                    cargarLectores() // Refresca lista automáticamente
                },
                onFailure = { excepcion ->
                    _uiState.update { estado ->
                        if (excepcion is LectorInvalidoException) {
                            estado.copy(
                                registrando = false,
                                formulario = estado.formulario.copy(
                                    nombreError = excepcion.errores.nombreError,
                                    dniError = excepcion.errores.dniError,
                                    emailError = excepcion.errores.emailError
                                )
                            )
                        } else {
                            estado.copy(
                                registrando = false,
                                fase = FaseLector.Error(excepcion.message ?: "Error al registrar lector")
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
