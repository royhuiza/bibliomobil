package pe.edu.upeu.bibliomobil.presentation.libro

import pe.edu.upeu.bibliomobil.domain.model.Libro

/**
 * Representa los diferentes estados visuales exclusivos de la lista o flujo principal de libros.
 */
sealed interface FaseLibro {
    object Cargando : FaseLibro
    object SinLibros : FaseLibro
    data class ConLibros(val libros: List<LibroUi>) : FaseLibro
    data class Error(val mensaje: String) : FaseLibro
}

/**
 * Agrupación de campos y errores asociados al formulario de creación.
 */
data class FormularioLibro(
    val titulo: String = "",
    val autor: String = "",
    val isbn: String = "",
    val totalEjemplares: String = "",
    val tituloError: String? = null,
    val autorError: String? = null,
    val isbnError: String? = null,
    val ejemplaresError: String? = null
)

/**
 * Modelo de presentación optimizado para la interfaz de usuario.
 */
data class LibroUi(
    val id: String,
    val titulo: String,
    val autor: String,
    val infoLinea: String
)

/**
 * Estado general inmutable que maneja la pantalla de gestión de libros.
 */
data class LibroUiState(
    val fase: FaseLibro = FaseLibro.Cargando,
    val formulario: FormularioLibro = FormularioLibro(),
    val registrando: Boolean = false,
    val mensajeExito: String? = null
)

/**
 * Mapea el modelo de dominio a un modelo puramente visual de presentación.
 */
fun Libro.aUi(): LibroUi {
    return LibroUi(
        id = this.id,
        titulo = this.titulo,
        autor = this.autor,
        infoLinea = "${this.isbn} · ${this.totalEjemplares} ejemplares"
    )
}
