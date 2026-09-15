package pe.edu.upeu.bibliomobil.domain.model

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ModelosTest {

    @Test
    fun libro_creacionExitosa_conValoresValidos() {
        val libro = Libro("1", "El Quijote", "Cervantes", "12345", 5, 3)
        assertEquals("1", libro.id)
        assertEquals("El Quijote", libro.titulo)
        assertFalse(libro.requiereReposicion)
    }

    @Test
    fun libro_error_cuandoIdEstaVacio() {
        assertFailsWith<IllegalArgumentException> {
            Libro(" ", "El Quijote", "Cervantes", "12345", 5, 3)
        }
    }

    @Test
    fun libro_error_cuandoTituloEstaVacio() {
        assertFailsWith<IllegalArgumentException> {
            Libro("1", "", "Cervantes", "12345", 5, 3)
        }
    }

    @Test
    fun libro_requiereReposicion_cuandoEjemplaresDisponiblesEsCero() {
        val libro = Libro("1", "El Quijote", "Cervantes", "12345", 5, 0)
        assertTrue(libro.requiereReposicion)
    }

    @Test
    fun libro_requiereReposicion_cuandoEsDanyado() {
        val libro = Libro("1", "El Quijote", "Cervantes", "12345", 5, 5, esDanyado = true)
        assertTrue(libro.requiereReposicion)
    }

    @Test
    fun lector_creacionExitosa_conValoresValidos() {
        val lector = Lector("1", "Juan Perez", "77777777", "juan@gmail.com")
        assertEquals("Juan Perez", lector.nombre)
        assertEquals("juan@gmail.com", lector.email)
    }

    @Test
    fun lector_error_cuandoEmailNoContieneArroba() {
        assertFailsWith<IllegalArgumentException> {
            Lector("1", "Juan Perez", "77777777", "juan_gmail.com")
        }
    }

    @Test
    fun detallePrestamo_multaPorRetraso_retornaCero_siSeDevuelveATiempo() {
        val libro = Libro("1", "El Quijote", "Cervantes", "12345", 5, 5)
        val detalle = DetallePrestamo("d1", libro, fechaDevolucionPrevista = 1000L, fechaDevolucionEfectiva = 900L)
        assertEquals(0.0, detalle.multaPorRetraso(1500L))
    }

    @Test
    fun detallePrestamo_multaPorRetraso_calculaCorrectamente_cuandoHayRetraso() {
        val libro = Libro("1", "El Quijote", "Cervantes", "12345", 5, 5)
        // 2 días de retraso = 2 * 86,400,000 milisegundos = 172,800,000
        val prevista = 100000L
        val efectiva = prevista + (2 * 86400000L)
        val detalle = DetallePrestamo("d1", libro, fechaDevolucionPrevista = prevista, fechaDevolucionEfectiva = efectiva, precioMultaPorDia = 5.0)
        assertEquals(10.0, detalle.multaPorRetraso(efectiva))
    }
}
