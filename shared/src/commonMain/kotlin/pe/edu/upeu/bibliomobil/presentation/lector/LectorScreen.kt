package pe.edu.upeu.bibliomobil.presentation.lector

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
fun LectorScreen(
    viewModel: LectorViewModel,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Afiliación de Lectores - BiblioMobil") })
        },
        modifier = modifier
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text("Registrar Nuevo Lector", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))

            ValidatedTextField(
                value = state.formulario.nombre,
                onValueChange = { viewModel.onNombreChange(it) },
                label = "Nombre Completo",
                error = state.formulario.nombreError,
                enabled = !state.registrando
            )
            Spacer(modifier = Modifier.height(4.dp))

            ValidatedTextField(
                value = state.formulario.dni,
                onValueChange = { viewModel.onDniChange(it) },
                label = "DNI",
                error = state.formulario.dniError,
                enabled = !state.registrando,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            Spacer(modifier = Modifier.height(4.dp))

            ValidatedTextField(
                value = state.formulario.email,
                onValueChange = { viewModel.onEmailChange(it) },
                label = "Correo Electrónico",
                error = state.formulario.emailError,
                enabled = !state.registrando,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )
            Spacer(modifier = Modifier.height(4.dp))

            ValidatedTextField(
                value = state.formulario.telefono,
                onValueChange = { viewModel.onTelefonoChange(it) },
                label = "Teléfono (Opcional)",
                error = null,
                enabled = !state.registrando,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
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
                    Text("Afiliar Lector")
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

            Text("Lectores Afiliados", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))

            // Renderizado exhaustivo de FaseLector mediante when
            when (val faseActual = state.fase) {
                is FaseLector.Cargando -> {
                    Box(modifier = Modifier.fillMaxWidth().weight(1f), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                is FaseLector.SinLectores -> {
                    Box(modifier = Modifier.fillMaxWidth().weight(1f), contentAlignment = Alignment.Center) {
                        Text("No hay lectores registrados en el sistema.", style = MaterialTheme.typography.bodyMedium)
                    }
                }
                is FaseLector.ConLectores -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth().weight(1f),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(faseActual.lectores, key = { it.id }) { lector ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text(lector.nombre, style = MaterialTheme.typography.titleSmall)
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(lector.infoContacto, style = MaterialTheme.typography.bodySmall)
                                }
                            }
                        }
                    }
                }
                is FaseLector.Error -> {
                    Box(modifier = Modifier.fillMaxWidth().weight(1f), contentAlignment = Alignment.Center) {
                        Text("Error: ${faseActual.mensaje}", color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }
}
