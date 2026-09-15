package pe.edu.upeu.bibliomobil.data.repository

import kotlinx.coroutines.delay
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import pe.edu.upeu.bibliomobil.domain.model.Lector
import pe.edu.upeu.bibliomobil.domain.repository.LectorRepository

/**
 * Implementación en memoria del repositorio de lectores, simulando almacenamiento y concurrencia.
 */
class LectorRepositorioEnMemoria : LectorRepository {

    private val mutex = Mutex()
    private val listaLectores = mutableListOf<Lector>()
    private var ultimoId = 0

    override suspend fun registrar(lector: Lector) {
        // Simulación de latencia de red/DB entre 300 y 800 milisegundos
        val latencia = (300..800).random().toLong()
        delay(latencia)

        mutex.withLock {
            ultimoId++
            // Se clona el lector asignándole su ID correlativo generado
            val lectorConId = lector.copy(id = ultimoId.toString())
            listaLectores.add(lectorConId)
        }
    }

    override suspend fun listar(): List<Lector> {
        // Simulación de latencia de red/DB entre 300 y 800 milisegundos
        val latencia = (300..800).random().toLong()
        delay(latencia)

        mutex.withLock {
            // Retorna una copia de la lista para salvaguardar la inmutabilidad fuera del repositorio
            return listaLectores.toList()
        }
    }
}
