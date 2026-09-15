package pe.edu.upeu.bibliomobil

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.koin.compose.KoinContext
import org.koin.compose.viewmodel.koinViewModel
import pe.edu.upeu.bibliomobil.presentation.inicio.InicioScreen
import pe.edu.upeu.bibliomobil.presentation.lector.LectorScreen
import pe.edu.upeu.bibliomobil.presentation.libro.LibroScreen
import pe.edu.upeu.bibliomobil.presentation.navigation.DESTINOS
import pe.edu.upeu.bibliomobil.presentation.navigation.Screen
import pe.edu.upeu.bibliomobil.presentation.navigation.ScreenSaver
import pe.edu.upeu.bibliomobil.presentation.prestamo.PrestamoVacioScreen

import pe.edu.upeu.bibliomobil.presentation.theme.BiblioMobilTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App() {
    KoinContext {
        // Variable para controlar de manera dinámica el modo oscuro
        var modoOscuroActivo by rememberSaveable { mutableStateOf(false) }

        BiblioMobilTheme(darkTheme = modoOscuroActivo) {
            // Estado de navegación que sobrevive a la rotación mediante el Saver personalizado
            var pantallaActual by rememberSaveable(stateSaver = ScreenSaver) { mutableStateOf<Screen>(Screen.Inicio) }
            
            val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
            val scope = rememberCoroutineScope()

            ModalNavigationDrawer(
                drawerState = drawerState,
                drawerContent = {
                    ModalDrawerSheet {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            "BiblioMobil Menu",
                            modifier = Modifier.padding(16.dp),
                            style = MaterialTheme.typography.titleLarge
                        )
                        HorizontalDivider()
                        
                        // Contenedor de los destinos principales
                        Column(modifier = Modifier.weight(1f)) {
                            DESTINOS.forEach { destino ->
                                NavigationDrawerItem(
                                    label = { Text(destino.titulo) },
                                    selected = pantallaActual == destino,
                                    onClick = {
                                        pantallaActual = destino
                                        scope.launch { drawerState.close() }
                                    },
                                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                                )
                            }
                        }

                        // Interruptor de modo oscuro ubicado de manera fija al pie del menú lateral
                        HorizontalDivider()
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Modo Oscuro", style = MaterialTheme.typography.bodyMedium)
                            Switch(
                                checked = modoOscuroActivo,
                                onCheckedChange = { modoOscuroActivo = it }
                            )
                        }
                    }
                }
            ) {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text(pantallaActual.titulo) }, // Título alimentado por el destino actual de la lista
                            navigationIcon = {
                                IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                    Icon(Icons.Default.Menu, contentDescription = "Abrir Menú")
                                }
                            }
                        )
                    }
                ) { paddingValues ->
                    Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
                        when (pantallaActual) {
                            Screen.Inicio -> InicioScreen(
                                onNavegar = { pantallaActual = it }
                            )
                            Screen.Libros -> LibroScreen(
                                viewModel = koinViewModel(),
                                modifier = Modifier.fillMaxSize()
                            )
                            Screen.Lectores -> LectorScreen(
                                viewModel = koinViewModel(),
                                modifier = Modifier.fillMaxSize()
                            )
                            Screen.Prestamos -> PrestamoVacioScreen(modifier = Modifier.fillMaxSize())
                        }
                    }
                }
            }
        }
    }
}
