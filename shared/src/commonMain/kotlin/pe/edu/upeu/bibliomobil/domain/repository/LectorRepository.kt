package pe.edu.upeu.bibliomobil.domain.repository

import pe.edu.upeu.bibliomobil.domain.model.Lector

/**
 * Puerto o contrato para la persistencia y gestión de los lectores afiliados a la biblioteca.
 */
interface LectorRepository {

    /**
     * Registra o afilia un nuevo lector al sistema de la biblioteca.
     * 
     * @param lector El lector con sus datos de contacto y DNI validados a ser registrado.
     */
    suspend fun registrar(lector: Lector)

    /**
     * Obtiene la lista completa de todos los lectores registrados en el sistema de la biblioteca.
     * 
     * @return Colección con todos los lectores afiliados.
     */
    suspend fun listar(): List<Lector>
}
