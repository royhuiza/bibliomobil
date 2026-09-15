package pe.edu.upeu.bibliomobil.domain.usecase

import pe.edu.upeu.bibliomobil.domain.model.Libro
import pe.edu.upeu.bibliomobil.domain.repository.LibroRepository

/**
 * Caso de Uso encargado de obtener y exponer la totalidad del inventario de libros de la biblioteca.
 */
class ListarLibrosUseCase(private val libroRepository: LibroRepository) {

    /**
     * Consulta el catálogo global de libros vigentes.
     * 
     * @return Colección de objetos [Libro].
     */
    suspend fun ejecutar(): List<Libro> {
        return libroRepository.listar()
    }
}
