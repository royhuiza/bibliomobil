package pe.edu.upeu.bibliomobil.fakes

import pe.edu.upeu.bibliomobil.domain.model.Lector
import pe.edu.upeu.bibliomobil.domain.repository.LectorRepository

class FakeLectorRepository : LectorRepository {
    val listaLectores = mutableListOf<Lector>()
    var ultimoId = 0
    var deberiaFallar = false
    var mensajeError = "Error simulado en repositorio de lectores"

    override suspend fun registrar(lector: Lector) {
        if (deberiaFallar) throw RuntimeException(mensajeError)
        ultimoId++
        listaLectores.add(lector.copy(id = ultimoId.toString()))
    }

    override suspend fun listar(): List<Lector> {
        if (deberiaFallar) throw RuntimeException(mensajeError)
        return listaLectores.toList()
    }
}
