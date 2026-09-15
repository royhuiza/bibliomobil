package pe.edu.upeu.bibliomobil.presentation.lector

import pe.edu.upeu.bibliomobil.domain.model.Lector

/**
 * Representa los diferentes estados visuales exclusivos del listado de lectores afiliados.
 */
sealed interface FaseLector {
    object Cargando : FaseLector
    object SinLectores : FaseLector
    data class ConLectores(val lectores: List<LectorUi>) : FaseLector
    data class Error(val mensaje: String) : FaseLector
}

/**
 * Agrupación de campos y errores asociados al formulario de afiliación de lectores.
 */
data class FormularioLector(
    val nombre: String = "",
    val dni: String = "",
    val email: String = "",
    val telefono: String = "",
    val nombreError: String? = null,
    val dniError: String? = null,
    val emailError: String? = null
)

/**
 * Modelo de presentación optimizado para renderizar la información del lector en la UI.
 */
data class LectorUi(
    val id: String,
    val nombre: String,
    val infoContacto: String
)

/**
 * Estado general inmutable que maneja la pantalla de gestión de lectores.
 */
data class LectorUiState(
    val fase: FaseLector = FaseLector.Cargando,
    val formulario: FormularioLector = FormularioLector(),
    val registrando: Boolean = false,
    val mensajeExito: String? = null
)

/**
 * Mapea el modelo de dominio Lector a un modelo optimizado para la capa de presentación.
 */
fun Lector.aUi(): LectorUi {
    val telMostrado = if (this.telefono.isNullOrBlank()) "No registrado" else this.telefono
    return LectorUi(
        id = this.id,
        nombre = this.nombre,
        infoContacto = "DNI: ${this.dni} · Tel: $telMostrado · Email: ${this.email}"
    )
}
