package pe.edu.upeu.bibliomobil.data.repository

import kotlinx.coroutines.delay
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import pe.edu.upeu.bibliomobil.domain.model.Libro
import pe.edu.upeu.bibliomobil.domain.repository.LibroRepository

/**
 * Implementación en memoria del repositorio de libros, simulando almacenamiento y concurrencia.
 */
class LibroRepositorioEnMemoria : LibroRepository {

    private val mutex = Mutex()
    private val listaLibros = mutableListOf<Libro>()
    private var ultimoId = 0

    override suspend fun registrar(libro: Libro) {
        // Simulación de latencia de red/DB entre 300 y 800 milisegundos
        val latencia = (300..800).random().toLong()
        delay(latencia)

        mutex.withLock {
            ultimoId++
            // Se clona el libro asignándole su ID correlativo generado
            val libroConId = libro.copy(id = ultimoId.toString())
            listaLibros.add(libroConId)
        }
    }

    override suspend fun listar(): List<Libro> {
        // Simulación de latencia de red/DB entre 300 y 800 milisegundos
        val latencia = (300..800).random().toLong()
        delay(latencia)

        mutex.withLock {
            // Retorna una copia de la lista para salvaguardar la inmutabilidad fuera del repositorio
            return listaLibros.toList()
        }
    }
}
