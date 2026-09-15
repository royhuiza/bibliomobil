package pe.edu.upeu.bibliomobil.domain.model

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class MasModelosTest {

    @Test
    fun libro_error_cuandoAutorEstaVacio() {
        assertFailsWith<IllegalArgumentException> {
            Libro("1", "Titulo", " ", "12345", 5, 3)
        }
    }

    @Test
    fun libro_error_cuandoIsbnEstaVacio() {
        assertFailsWith<IllegalArgumentException> {
            Libro("1", "Titulo", "Autor", "", 5, 3)
        }
    }

    @Test
    fun libro_error_cuandoTotalEjemplaresEsNegativo() {
        assertFailsWith<IllegalArgumentException> {
            Libro("1", "Titulo", "Autor", "123", -1, 0)
        }
    }

    @Test
    fun libro_error_cuandoDisponiblesExcedeTotal() {
        assertFailsWith<IllegalArgumentException> {
            Libro("1", "Titulo", "Autor", "123", 5, 6)
        }
    }

    @Test
    fun libro_error_cuandoDisponiblesEsNegativo() {
        assertFailsWith<IllegalArgumentException> {
            Libro("1", "Titulo", "Autor", "123", 5, -1)
        }
    }

    @Test
    fun lector_error_cuandoIdEstaVacio() {
        assertFailsWith<IllegalArgumentException> {
            Lector("", "Nombre", "Dni", "email@test.com")
        }
    }

    @Test
    fun lector_error_cuandoNombreEstaVacio() {
        assertFailsWith<IllegalArgumentException> {
            Lector("1", "  ", "Dni", "email@test.com")
        }
    }

    @Test
    fun lector_error_cuandoDniEstaVacio() {
        assertFailsWith<IllegalArgumentException> {
            Lector("1", "Nombre", "", "email@test.com")
        }
    }

    @Test
    fun prestamo_error_cuandoIdEstaVacio() {
        val lector = Lector("1", "Nombre", "Dni", "email@test.com")
        assertFailsWith<IllegalArgumentException> {
            Prestamo(" ", lector, emptyList(), 1000L, EstadoPrestamo.ACTIVO)
        }
    }

    @Test
    fun prestamo_error_cuandoDetallesEstaVacio() {
        val lector = Lector("1", "Nombre", "Dni", "email@test.com")
        assertFailsWith<IllegalArgumentException> {
            Prestamo("P1", lector, emptyList(), 1000L, EstadoPrestamo.ACTIVO)
        }
    }
}
