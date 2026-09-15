package pe.edu.upeu.bibliomobil.presentation

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import pe.edu.upeu.bibliomobil.domain.usecase.ListarLectoresUseCase
import pe.edu.upeu.bibliomobil.domain.usecase.ListarLibrosUseCase
import pe.edu.upeu.bibliomobil.domain.usecase.RegistrarLectorUseCase
import pe.edu.upeu.bibliomobil.domain.usecase.RegistrarLibroUseCase
import pe.edu.upeu.bibliomobil.fakes.FakeLectorRepository
import pe.edu.upeu.bibliomobil.fakes.FakeLibroRepository
import pe.edu.upeu.bibliomobil.presentation.lector.FaseLector
import pe.edu.upeu.bibliomobil.presentation.lector.LectorViewModel
import pe.edu.upeu.bibliomobil.presentation.libro.FaseLibro
import pe.edu.upeu.bibliomobil.presentation.libro.LibroViewModel
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class ViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()

    private val fakeLibroRepository = FakeLibroRepository()
    private val fakeLectorRepository = FakeLectorRepository()

    private lateinit var libroViewModel: LibroViewModel
    private lateinit var lectorViewModel: LectorViewModel

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        val registrarLibroUseCase = RegistrarLibroUseCase(fakeLibroRepository)
        val listarLibrosUseCase = ListarLibrosUseCase(fakeLibroRepository)
        libroViewModel = LibroViewModel(registrarLibroUseCase, listarLibrosUseCase)

        val registrarLectorUseCase = RegistrarLectorUseCase(fakeLectorRepository)
        val listarLectoresUseCase = ListarLectoresUseCase(fakeLectorRepository)
        lectorViewModel = LectorViewModel(registrarLectorUseCase, listarLectoresUseCase)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun libroViewModel_inicial_cargaFaseSinLibros_siRepositorioEstaVacio() = runTest {
        assertEquals(FaseLibro.SinLibros, libroViewModel.uiState.value.fase)
    }

    @Test
    fun libroViewModel_onValueChange_actualizaFormularioCorrectamente() {
        libroViewModel.onTituloChange("Don Quijote")
        assertEquals("Don Quijote", libroViewModel.uiState.value.formulario.titulo)
    }

    @Test
    fun libroViewModel_registrar_exito_limpiaFormularioYActualizaFase() = runTest {
        libroViewModel.onTituloChange("Calculo I")
        libroViewModel.onAutorChange("Stewart")
        libroViewModel.onIsbnChange("ISBN-111")
        libroViewModel.onTotalEjemplaresChange("5")

        libroViewModel.registrar()

        assertEquals("", libroViewModel.uiState.value.formulario.titulo)
        assertNotNull(libroViewModel.uiState.value.mensajeExito)
        assertTrue(libroViewModel.uiState.value.fase is FaseLibro.ConLibros)
        val conLibros = libroViewModel.uiState.value.fase as FaseLibro.ConLibros
        assertEquals(1, conLibros.libros.size)
        assertEquals("ISBN-111 · 5 ejemplares", conLibros.libros.first().infoLinea)
    }

    @Test
    fun libroViewModel_registrar_falla_asignaErroresAlFormulario_sinCambiarFase() = runTest {
        libroViewModel.onTituloChange("") // Invalido
        libroViewModel.registrar()

        assertNotNull(libroViewModel.uiState.value.formulario.tituloError)
        assertTrue(libroViewModel.uiState.value.fase is FaseLibro.SinLibros)
    }

    @Test
    fun lectorViewModel_inicial_cargaFaseSinLectores_siRepositorioEstaVacio() = runTest {
        assertEquals(FaseLector.SinLectores, lectorViewModel.uiState.value.fase)
    }

    @Test
    fun lectorViewModel_registrar_exito_maqueaTelefonoAusenteComoNoRegistrado() = runTest {
        lectorViewModel.onNombreChange("Ana Maria")
        lectorViewModel.onDniChange("12345678")
        lectorViewModel.onEmailChange("ana@gmail.com")
        lectorViewModel.onTelefonoChange("") // Vacio -> Null

        lectorViewModel.registrar()

        assertTrue(lectorViewModel.uiState.value.fase is FaseLector.ConLectores)
        val conLectores = lectorViewModel.uiState.value.fase as FaseLector.ConLectores
        assertTrue(conLectores.lectores.first().infoContacto.contains("Tel: No registrado"))
    }
}
