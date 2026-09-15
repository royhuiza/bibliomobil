package pe.edu.upeu.bibliomobil.domain.repository

import pe.edu.upeu.bibliomobil.domain.model.Libro

/**
 * Puerto o contrato para la persistencia y gestión de los libros del catálogo de la biblioteca.
 */
interface LibroRepository {

    /**
     * Registra o incorpora un nuevo libro al catálogo disponible de la biblioteca.
     * 
     * @param libro El libro con todos sus datos validados y ejemplares iniciales a registrar.
     */
    suspend fun registrar(libro: Libro)

    /**
     * Obtiene la lista completa de todos los libros registrados en el catálogo de la biblioteca.
     * 
     * @return Colección con todos los libros existentes en el sistema.
     */
    suspend fun listar(): List<Libro>
}
