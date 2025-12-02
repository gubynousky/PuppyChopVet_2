package cl.martinez.puppychopvet.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cl.martinez.puppychopvet.network.CitaRepository
import cl.martinez.puppychopvet.network.models.CitaVeterinariaResponse
import cl.martinez.puppychopvet.ui.components.AppTopBar
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToForm: () -> Unit,
    onNavigateToDetail: (Long) -> Unit
) {
    val scope = rememberCoroutineScope()
    val repository = remember { CitaRepository() }
    val snackbarHostState = remember { SnackbarHostState() }

    var citas by remember { mutableStateOf<List<CitaVeterinariaResponse>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }
    var filtroActual by remember { mutableStateOf(FiltroCita.PENDIENTES) }

    // Cargar citas al inicio
    LaunchedEffect(filtroActual) {
        isLoading = true
        val result = when (filtroActual) {
            FiltroCita.PENDIENTES -> repository.getCitasPendientes()
            FiltroCita.CONFIRMADAS -> repository.getCitasConfirmadas()
            FiltroCita.TODAS -> repository.getAllCitas()
        }

        result.onSuccess {
            citas = it
        }.onFailure {
            snackbarHostState.showSnackbar("Error al cargar citas")
        }
        isLoading = false
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            AppTopBar(title = "PuppyChop Citas")
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToForm,
                containerColor = MaterialTheme.colorScheme.secondary
            ) {
                Icon(Icons.Default.Add, "Nueva cita")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Filtros
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                FilterChip(
                    selected = filtroActual == FiltroCita.PENDIENTES,
                    onClick = { filtroActual = FiltroCita.PENDIENTES },
                    label = { Text("Pendientes") }
                )
                FilterChip(
                    selected = filtroActual == FiltroCita.CONFIRMADAS,
                    onClick = { filtroActual = FiltroCita.CONFIRMADAS },
                    label = { Text("Confirmadas") }
                )
                FilterChip(
                    selected = filtroActual == FiltroCita.TODAS,
                    onClick = { filtroActual = FiltroCita.TODAS },
                    label = { Text("Todas") }
                )
            }

            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else if (citas.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No hay citas")
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(citas) { cita ->
                        CitaCard(
                            cita = cita,
                            onClick = { onNavigateToDetail(cita.id) },
                            onConfirmar = {
                                scope.launch {
                                    repository.confirmarCita(cita.id).onSuccess {
                                        snackbarHostState.showSnackbar("Cita confirmada ✓")
                                        // Recargar
                                        filtroActual = filtroActual
                                    }
                                }
                            },
                            onEliminar = {
                                scope.launch {
                                    repository.deleteCita(cita.id).onSuccess {
                                        snackbarHostState.showSnackbar("Cita eliminada")
                                        // Recargar
                                        filtroActual = filtroActual
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CitaCard(
    cita: CitaVeterinariaResponse,
    onClick: () -> Unit,
    onConfirmar: () -> Unit,
    onEliminar: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "🐶 ${cita.mascota.nombre}",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "Dueño: ${cita.usuario.nombre}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Veterinario: ${cita.veterinario.nombre}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "${cita.fechaCita} - ${cita.horaCita}",
                style = MaterialTheme.typography.bodySmall
            )

            Spacer(Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (!cita.confirmada) {
                    Button(
                        onClick = onConfirmar,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Confirmar")
                    }
                }
                OutlinedButton(
                    onClick = onEliminar,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Eliminar")
                }
            }
        }
    }
}

enum class FiltroCita {
    PENDIENTES,
    CONFIRMADAS,
    TODAS
}