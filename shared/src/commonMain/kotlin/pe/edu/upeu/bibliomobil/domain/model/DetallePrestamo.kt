package pe.edu.upeu.bibliomobil.domain.model

/**
 * Representa el detalle individual de un libro incluido dentro de un préstamo.
 */
data class DetallePrestamo(
    val id: String,
    val libro: Libro,
    val fechaDevolucionPrevista: Long, // Timestamp en milisegundos
    val fechaDevolucionEfectiva: Long? = null, // Null si aún no se ha devuelto el libro
    val precioMultaPorDia: Double = 5.0
) {
    init {
        require(id.isNotBlank()) { "El ID del detalle de préstamo no puede estar vacío." }
        require(precioMultaPorDia >= 0.0) { "El precio de la multa por día no puede ser negativo." }
    }

    /**
     * Regla de negocio: Calcula el costo de la multa por retraso acumulada para este ejemplar.
     * Si el libro no ha sido devuelto todavía, se calcula el retraso en función de una fecha de corte o fecha actual.
     *
     * @param fechaCorte Timestamp en milisegundos que representa el momento actual o de consulta si no se ha devuelto.
     * @return El monto total de la multa por retraso.
     */
    fun multaPorRetraso(fechaCorte: Long): Double {
        val fechaFin = fechaDevolucionEfectiva ?: fechaCorte
        if (fechaFin <= fechaDevolucionPrevista) return 0.0
        
        // 1 día entero tiene 86,400,000 milisegundos
        val diferenciaMilis = fechaFin - fechaDevolucionPrevista
        val diasRetraso = (diferenciaMilis / 86400000).toInt()
        
        return diasRetraso * precioMultaPorDia
    }
}
