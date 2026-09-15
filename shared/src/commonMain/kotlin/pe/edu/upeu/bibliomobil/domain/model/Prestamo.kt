package pe.edu.upeu.bibliomobil.domain.model

/**
 * Representa un registro de préstamo de libros realizado por un lector.
 */
data class Prestamo(
    val id: String,
    val lector: Lector,
    val detalles: List<DetallePrestamo>,
    val fechaPrestamo: Long, // Timestamp en milisegundos
    val estado: EstadoPrestamo
) {
    init {
        require(id.isNotBlank()) { "El ID del préstamo no puede estar vacío." }
        require(detalles.isNotEmpty()) { "Un préstamo debe contener al menos un detalle de libro." }
    }
}
