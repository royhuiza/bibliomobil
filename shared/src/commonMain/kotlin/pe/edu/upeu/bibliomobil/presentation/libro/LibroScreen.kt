package pe.edu.upeu.bibliomobil.presentation.libro

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

import pe.edu.upeu.bibliomobil.presentation.components.MensajeExito
import pe.edu.upeu.bibliomobil.presentation.components.ValidatedTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibroScreen(
    viewModel: LibroViewModel,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Gestión de Catálogo - BiblioMobil") })
        },
        modifier = modifier
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // Formulario de Registro
            Text("Registrar Nuevo Libro", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))

            ValidatedTextField(
                value = state.formulario.titulo,
                onValueChange = { viewModel.onTituloChange(it) },
                label = "Título",
                error = state.formulario.tituloError,
                enabled = !state.registrando
            )
            Spacer(modifier = Modifier.height(4.dp))

            ValidatedTextField(
                value = state.formulario.autor,
                onValueChange = { viewModel.onAutorChange(it) },
                label = "Autor",
                error = state.formulario.autorError,
                enabled = !state.registrando
            )
            Spacer(modifier = Modifier.height(4.dp))

            ValidatedTextField(
                value = state.formulario.isbn,
                onValueChange = { viewModel.onIsbnChange(it) },
                label = "ISBN",
                error = state.formulario.isbnError,
                enabled = !state.registrando
            )
            Spacer(modifier = Modifier.height(4.dp))

            ValidatedTextField(
                value = state.formulario.totalEjemplares,
                onValueChange = { viewModel.onTotalEjemplaresChange(it) },
                label = "Total Ejemplares",
                error = state.formulario.ejemplaresError,
                enabled = !state.registrando,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = { viewModel.registrar() },
                modifier = Modifier.fillMaxWidth(),
                enabled = !state.registrando
            ) {
                if (state.registrando) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Registrar Libro")
                }
            }

            state.mensajeExito?.let {
                Spacer(modifier = Modifier.height(8.dp))
                MensajeExito(
                    mensaje = it,
                    onConfirmar = { viewModel.limpiarMensajeExito() }
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(12.dp))

            Text("Catálogo de Libros", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))

            // Gestión exhaustiva de las Fases del Contenido mediante 'when'
            when (val faseActual = state.fase) {
                is FaseLibro.Cargando -> {
                    Box(modifier = Modifier.fillMaxWidth().weight(1f), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                is FaseLibro.SinLibros -> {
                    Box(modifier = Modifier.fillMaxWidth().weight(1f), contentAlignment = Alignment.Center) {
                        Text("No hay libros registrados en la biblioteca.", style = MaterialTheme.typography.bodyMedium)
                    }
                }
                is FaseLibro.ConLibros -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth().weight(1f),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(faseActual.libros, key = { it.id }) { libro ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text(libro.titulo, style = MaterialTheme.typography.titleSmall)
                                    Text("Por: ${libro.autor}", style = MaterialTheme.typography.bodySmall)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(libro.infoLinea, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
                                }
                            }
                        }
                    }
                }
                is FaseLibro.Error -> {
                    Box(modifier = Modifier.fillMaxWidth().weight(1f), contentAlignment = Alignment.Center) {
                        Text("Error: ${faseActual.mensaje}", color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }
}
