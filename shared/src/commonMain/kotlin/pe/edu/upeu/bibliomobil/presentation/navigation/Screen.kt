package pe.edu.upeu.bibliomobil.presentation.navigation

import androidx.compose.runtime.saveable.Saver

/**
 * Representa los destinos de navegación disponibles en la aplicación.
 */
sealed class Screen(val titulo: String) {
    object Inicio : Screen("Inicio")
    object Libros : Screen("Catálogo de Libros")
    object Lectores : Screen("Gestión de Lectores")
    object Prestamos : Screen("Préstamos Realizados")
}

/**
 * Lista maestra que alimenta el Menú Lateral y los títulos de la barra superior.
 */
val DESTINOS = listOf(
    Screen.Inicio,
    Screen.Libros,
    Screen.Lectores,
    Screen.Prestamos
)

/**
 * Saver personalizado para que el estado de la pantalla actual sobreviva a la rotación
 * guardando solo el nombre de la clase y restaurando el objeto singleton correspondiente.
 */
val ScreenSaver = Saver<Screen, String>(
    save = { it::class.simpleName ?: "Inicio" },
    restore = { name ->
        when (name) {
            "Libros" -> Screen.Libros
            "Lectores" -> Screen.Lectores
            "Prestamos" -> Screen.Prestamos
            else -> Screen.Inicio
        }
    }
)
