package pe.edu.upeu.bibliomobil.domain.model

/**
 * Representa un libro dentro del sistema de la biblioteca.
 */
data class Libro(
    val id: String,
    val titulo: String,
    val autor: String,
    val isbn: String,
    val totalEjemplares: Int,
    val ejemplaresDisponibles: Int,
    val esDanyado: Boolean = false
) {
    init {
        require(id.isNotBlank()) { "El ID del libro no puede estar vacío." }
        require(titulo.isNotBlank()) { "El título del libro no puede estar vacío." }
        require(autor.isNotBlank()) { "El autor del libro no puede estar vacío." }
        require(isbn.isNotBlank()) { "El ISBN del libro no puede estar vacío." }
        require(totalEjemplares >= 0) { "El total de ejemplares no puede ser negativo." }
        require(ejemplaresDisponibles in 0..totalEjemplares) { 
            "Los ejemplares disponibles ($ejemplaresDisponibles) deben estar entre 0 y el total de ejemplares ($totalEjemplares)." 
        }
    }

    /**
     * Regla de negocio: Determina si el libro requiere ser repuesto o adquirido nuevamente.
     * Un libro requiere reposición si está marcado como dañado o si ya no quedan ejemplares disponibles en el inventario.
     */
    val requiereReposicion: Boolean
        get() = esDanyado || ejemplaresDisponibles == 0
}
