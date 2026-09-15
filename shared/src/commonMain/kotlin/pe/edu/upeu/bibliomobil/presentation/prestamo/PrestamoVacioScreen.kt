package pe.edu.upeu.bibliomobil.presentation.prestamo

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

import pe.edu.upeu.bibliomobil.presentation.components.EstadoVacio

/**
 * Pantalla que representa el estado vacío para la gestión de préstamos (RF-05).
 */
@Composable
fun PrestamoVacioScreen(
    modifier: Modifier = Modifier
) {
    EstadoVacio(
        titulo = "Historial de Préstamos Vacío",
        descripcion = "Aún no se han registrado préstamos de libros en el sistema. Inicie un nuevo préstamo desde la ficha del lector.",
        modifier = modifier
    )
}
