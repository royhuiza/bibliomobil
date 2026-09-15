package pe.edu.upeu.bibliomobil.presentation.inicio

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import pe.edu.upeu.bibliomobil.presentation.navigation.Screen

/**
 * Representa una opción de acceso rápido en la pantalla de inicio.
 */
data class OpcionInicio(val titulo: String, val destino: Screen)

private val OPCIONES_INICIO = listOf(
    OpcionInicio("Gestionar Catálogo de Libros", Screen.Libros),
    OpcionInicio("Afiliar Nuevos Lectores", Screen.Lectores),
    OpcionInicio("Consultar Préstamos Vigentes", Screen.Prestamos)
)

@Composable
fun InicioScreen(
    onNavegar: (Screen) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Bienvenido a BiblioMobil",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center
        )
        Text(
            text = "Sistema integral de gestión bibliotecaria multiplataforma",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(48.dp))
        
        Text(
            text = "Accesos Rápidos",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Generación dinámica de accesos rápidos desde la lista maestra (RF-01)
        OPCIONES_INICIO.forEach { opcion ->
            OutlinedButton(
                onClick = { onNavegar(opcion.destino) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Text(opcion.titulo)
            }
        }
    }
}
