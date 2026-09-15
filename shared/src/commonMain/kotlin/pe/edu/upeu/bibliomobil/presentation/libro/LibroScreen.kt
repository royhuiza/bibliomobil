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

            OutlinedTextField(
                value = state.formulario.titulo,
                onValueChange = { viewModel.onTituloChange(it) },
                label = { Text("Título") },
                isError = state.formulario.tituloError != null,
                supportingText = state.formulario.tituloError?.let { { Text(it, color = MaterialTheme.colorScheme.error) } },
                modifier = Modifier.fillMaxWidth(),
                enabled = !state.registrando
            )
            Spacer(modifier = Modifier.height(4.dp))

            OutlinedTextField(
                value = state.formulario.autor,
                onValueChange = { viewModel.onAutorChange(it) },
                label = { Text("Autor") },
                isError = state.formulario.autorError != null,
                supportingText = state.formulario.autorError?.let { { Text(it, color = MaterialTheme.colorScheme.error) } },
                modifier = Modifier.fillMaxWidth(),
                enabled = !state.registrando
            )
            Spacer(modifier = Modifier.height(4.dp))

            OutlinedTextField(
                value = state.formulario.isbn,
                onValueChange = { viewModel.onIsbnChange(it) },
                label = { Text("ISBN") },
                isError = state.formulario.isbnError != null,
                supportingText = state.formulario.isbnError?.let { { Text(it, color = MaterialTheme.colorScheme.error) } },
                modifier = Modifier.fillMaxWidth(),
                enabled = !state.registrando
            )
            Spacer(modifier = Modifier.height(4.dp))

            OutlinedTextField(
                value = state.formulario.totalEjemplares,
                onValueChange = { viewModel.onTotalEjemplaresChange(it) },
                label = { Text("Total Ejemplares") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = state.formulario.ejemplaresError != null,
                supportingText = state.formulario.ejemplaresError?.let { { Text(it, color = MaterialTheme.colorScheme.error) } },
                modifier = Modifier.fillMaxWidth(),
                enabled = !state.registrando
            )
            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = { viewModel.registrar() },
                modifier = Modifier.fillMaxWidth(),
                enabled = !state.registrando
            ) {
                if (state.registrando) {
                    CircularProgressIndicator(size = 20.dp, strokeWidth = 2.dp, color = MaterialTheme.colorScheme.onPrimary)
                } else {
                    Text("Registrar Libro")
                }
            }

            state.mensajeExito?.let {
                Spacer(modifier = Modifier.height(8.dp))
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(it, modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodyMedium)
                        TextButton(onClick = { viewModel.limpiarMensajeExito() }) {
                            Text("OK")
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            Divider()
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
