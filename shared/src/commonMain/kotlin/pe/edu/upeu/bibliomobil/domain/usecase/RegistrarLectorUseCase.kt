package pe.edu.upeu.bibliomobil.domain.usecase

import pe.edu.upeu.bibliomobil.domain.model.Lector
import pe.edu.upeu.bibliomobil.domain.repository.LectorRepository

/**
 * Estructura que consolida las fallas específicas de validación en los campos de un lector.
 */
data class ErroresDeLector(
    val nombreError: String? = null,
    val dniError: String? = null,
    val emailError: String? = null
) {
    val tieneErrores: Boolean
        get() = nombreError != null || dniError != null || emailError != null
}

/**
 * Excepción de negocio lanzada cuando los datos de un lector no satisfacen las reglas obligatorias.
 */
class LectorInvalidoException(val errores: ErroresDeLector) : 
    IllegalArgumentException("Los datos proporcionados para el lector no son válidos.")

/**
 * Caso de Uso responsable de coordinar la validación y afiliación de un nuevo usuario lector.
 */
class RegistrarLectorUseCase(private val lectorRepository: LectorRepository) {

    /**
     * Valida rigurosamente los campos recibidos en texto y realiza la afiliación del lector.
     * Envía por defecto el ID en "0" según la especificación técnica.
     * 
     * @return [Result] con el objeto [Lector] registrado, o una falla conteniendo [LectorInvalidoException].
     */
    suspend fun ejecutar(
        nombre: String,
        dni: String,
        email: String
    ): Result<Lector> = resultadoDe {
        var errores = ErroresDeLector()

        if (nombre.trim().isBlank()) {
            errores = errores.copy(nombreError = "El nombre completo es obligatorio.")
        }
        if (dni.trim().isBlank()) {
            errores = errores.copy(dniError = "El documento nacional de identidad (DNI) es obligatorio.")
        }
        if (!email.trim().contains("@")) {
            errores = errores.copy(emailError = "El correo electrónico provisto debe poseer una estructura válida con '@'.")
        }

        if (errores.tieneErrores) {
            throw LectorInvalidoException(errores)
        }

        val nuevoLector = Lector(
            id = "0", // Equivalente a la especificación de ID base 0 en cadena/L
            nombre = nombre.trim(),
            dni = dni.trim(),
            email = email.trim(),
            estaActivo = true
        )

        lectorRepository.registrar(nuevoLector)
        nuevoLector
    }
}
