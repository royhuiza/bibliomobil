package pe.edu.upeu.bibliomobil.domain.model

/**
 * Representa a un usuario lector registrado en el sistema de la biblioteca.
 */
data class Lector(
    val id: String,
    val nombre: String,
    val dni: String,
    val email: String,
    val estaActivo: Boolean = true
) {
    init {
        require(id.isNotBlank()) { "El ID del lector no puede estar vacío." }
        require(nombre.isNotBlank()) { "El nombre del lector no puede estar vacío." }
        require(dni.isNotBlank()) { "El DNI del lector no puede estar vacío." }
        require(email.contains("@")) { "El correo electrónico del lector debe ser válido y contener un '@'." }
    }
}
