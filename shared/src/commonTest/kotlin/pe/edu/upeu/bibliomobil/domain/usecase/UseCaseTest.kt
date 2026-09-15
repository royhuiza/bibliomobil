package pe.edu.upeu.bibliomobil.domain.usecase

import kotlinx.coroutines.test.runTest
import pe.edu.upeu.bibliomobil.fakes.FakeLectorRepository
import pe.edu.upeu.bibliomobil.fakes.FakeLibroRepository
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class UseCaseTest {

    private val fakeLibroRepository = FakeLibroRepository()
    private val fakeLectorRepository = FakeLectorRepository()

    private val registrarLibroUseCase = RegistrarLibroUseCase(fakeLibroRepository)
    private val listarLibrosUseCase = ListarLibrosUseCase(fakeLibroRepository)
    private val registrarLectorUseCase = RegistrarLectorUseCase(fakeLectorRepository)
    private val listarLectoresUseCase = ListarLectoresUseCase(fakeLectorRepository)

    @Test
    fun registrarLibro_exito_cuandoDatosSonCorrectos() = runTest {
        val resultado = registrarLibroUseCase.ejecutar("Moby Dick", "Melville", "999-123", "10")
        assertTrue(resultado.isSuccess)
        val libro = resultado.getOrNull()
        assertNotNull(libro)
        assertEquals("Moby Dick", libro.titulo)
        assertEquals(10, libro.totalEjemplares)
        assertEquals(1, fakeLibroRepository.listaLibros.size)
        assertEquals("1", fakeLibroRepository.listaLibros.first().id) // Id correlativo asignado en fake
    }

    @Test
    fun registrarLibro_falla_conLibroInvalidoException_siTituloEstaVacio() = runTest {
        val resultado = registrarLibroUseCase.ejecutar("", "Melville", "999-123", "10")
        assertTrue(resultado.isFailure)
        val excepcion = resultado.exceptionOrNull()
        assertTrue(excepcion is LibroInvalidoException)
        assertNotNull(excepcion.errores.tituloError)
    }

    @Test
    fun registrarLibro_falla_siEjemplaresEsInvalido() = runTest {
        val resultado = registrarLibroUseCase.ejecutar("Titulo", "Autor", "ISBN", "-5")
        assertTrue(resultado.isFailure)
        val excepcion = resultado.exceptionOrNull() as LibroInvalidoException
        assertNotNull(excepcion.errores.ejemplaresError)
    }

    @Test
    fun listarLibros_retornaCatalogoCorrectamente() = runTest {
        registrarLibroUseCase.ejecutar("L1", "A1", "I1", "5")
        registrarLibroUseCase.ejecutar("L2", "A2", "I2", "2")
        val lista = listarLibrosUseCase.ejecutar()
        assertEquals(2, lista.size)
    }

    @Test
    fun registrarLector_exito_conCamposRecortados() = runTest {
        val resultado = registrarLectorUseCase.ejecutar("   Carlos Gomez   ", "44444444", "carlos@gmail.com", "987654321")
        assertTrue(resultado.isSuccess)
        val lector = resultado.getOrNull()!!
        assertEquals("Carlos Gomez", lector.nombre) // Guardado recortado
        assertEquals("987654321", lector.telefono)
    }

    @Test
    fun registrarLector_guardaTelefonoComoNull_siEstaVacio() = runTest {
        val resultado = registrarLectorUseCase.ejecutar("Carlos Gomez", "44444444", "carlos@gmail.com", "   ")
        assertTrue(resultado.isSuccess)
        val lector = resultado.getOrNull()!!
        kotlin.test.assertNull(lector.telefono)
    }

    @Test
    fun registrarLector_falla_siEmailNoTieneArroba() = runTest {
        val resultado = registrarLectorUseCase.ejecutar("Carlos Gomez", "44444444", "carlos_gmail.com")
        assertTrue(resultado.isFailure)
        val excepcion = resultado.exceptionOrNull() as LectorInvalidoException
        assertNotNull(excepcion.errores.emailError)
    }

    @Test
    fun listarLectores_retornaAfiliadosCorrectamente() = runTest {
        registrarLectorUseCase.ejecutar("Lector 1", "00000001", "l1@gmail.com")
        val lista = listarLectoresUseCase.ejecutar()
        assertEquals(1, lista.size)
    }
}
