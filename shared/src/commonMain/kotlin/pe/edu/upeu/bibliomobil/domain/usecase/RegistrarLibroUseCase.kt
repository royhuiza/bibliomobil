package pe.edu.upeu.bibliomobil.domain.usecase

import pe.edu.upeu.bibliomobil.domain.model.Libro
import pe.edu.upeu.bibliomobil.domain.repository.LibroRepository

/**
 * Estructura que consolida las fallas específicas de validación en los campos de un libro.
 */
data class ErroresDeLibro(
    val tituloError: String? = null,
    val autorError: String? = null,
    val isbnError: String? = null,
    val ejemplaresError: String? = null
) {
    val tieneErrores: Boolean
        get() = tituloError != null || autorError != null || isbnError != null || ejemplaresError != null
}

/**
 * Excepción de negocio lanzada cuando los datos de un libro no satisfacen las reglas obligatorias.
 */
class LibroInvalidoException(val errores: ErroresDeLibro) : 
    IllegalArgumentException("Los datos proporcionados para el libro no son válidos.")

/**
 * Caso de Uso responsable de coordinar la validación y el registro formal de un nuevo libro en el sistema.
 */
class RegistrarLibroUseCase(private val libroRepository: LibroRepository) {

    /**
     * Valida de manera exhaustiva y registra un libro recibiendo todos los parámetros de entrada en formato de texto.
     * Envía por defecto el ID en "0" según la regla técnica establecida.
     * 
     * @return [Result] con el objeto [Libro] registrado exitosamente, o una falla conteniendo [LibroInvalidoException].
     */
    suspend fun ejecutar(
        titulo: String,
        autor: String,
        isbn: String,
        totalEjemplares: String
    ): Result<Libro> = resultadoDe {
        var errores = ErroresDeLibro()

        if (titulo.trim().isBlank()) {
            errores = errores.copy(tituloError = "El título es obligatorio y no puede estar vacío.")
        }
        if (autor.trim().isBlank()) {
            errores = errores.copy(autorError = "El autor es obligatorio y no puede estar vacío.")
        }
        if (isbn.trim().isBlank()) {
            errores = errores.copy(isbnError = "El ISBN es obligatorio y no puede estar vacío.")
        }

        val ejemplaresInt = totalEjemplares.trim().toIntOrNull()
        if (ejemplaresInt == null || ejemplaresInt < 0) {
            errores = errores.copy(ejemplaresError = "El número de ejemplares debe ser un valor entero no negativo.")
        }

        if (errores.tieneErrores) {
            throw LibroInvalidoException(errores)
        }

        val nuevoLibro = Libro(
            id = "0", // Equivalente a la especificación de ID base 0 en cadena/L
            titulo = titulo.trim(),
            autor = autor.trim(),
            isbn = isbn.trim(),
            totalEjemplares = ejemplaresInt!!,
            ejemplaresDisponibles = ejemplaresInt,
            esDanyado = false
        )

        libroRepository.registrar(nuevoLibro)
        nuevoLibro
    }
}
