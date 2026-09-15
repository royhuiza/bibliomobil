package pe.edu.upeu.bibliomobil.domain.usecase

import pe.edu.upeu.bibliomobil.domain.model.Lector
import pe.edu.upeu.bibliomobil.domain.repository.LectorRepository

/**
 * Caso de Uso encargado de obtener la lista completa de todos los lectores afiliados.
 */
class ListarLectoresUseCase(private val lectorRepository: LectorRepository) {

    /**
     * Consulta la lista total de lectores activos e inactivos del sistema.
     * 
     * @return Colección de objetos [Lector].
     */
    suspend fun ejecutar(): List<Lector> {
        return lectorRepository.listar()
    }
}
