package pe.edu.upeu.bibliomobil.fakes

import pe.edu.upeu.bibliomobil.domain.model.Libro
import pe.edu.upeu.bibliomobil.domain.repository.LibroRepository

class FakeLibroRepository : LibroRepository {
    val listaLibros = mutableListOf<Libro>()
    var ultimoId = 0
    var deberiaFallar = false
    var mensajeError = "Error simulado en repositorio de libros"

    override suspend fun registrar(libro: Libro) {
        if (deberiaFallar) throw RuntimeException(mensajeError)
        ultimoId++
        listaLibros.add(libro.copy(id = ultimoId.toString()))
    }

    override suspend fun listar(): List<Libro> {
        if (deberiaFallar) throw RuntimeException(mensajeError)
        return listaLibros.toList()
    }
}
