package cl.martinez.puppychopvet.view

import android.util.Log
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
    var refreshTrigger by remember { mutableStateOf(0) } // ✅ Para forzar recarga

    // Función para cargar citas
    fun cargarCitas() {
        scope.launch {
            isLoading = true
            try {
                val result = when (filtroActual) {
                    FiltroCita.PENDIENTES -> repository.getCitasPendientes()
                    FiltroCita.CONFIRMADAS -> repository.getCitasConfirmadas()
                    FiltroCita.TODAS -> repository.getAllCitas()
                }

                result.onSuccess { listaCitas ->
                    citas = listaCitas
                }.onFailure { error ->
                    Log.e("HomeScreen", "Error cargando citas", error)
                    snackbarHostState.showSnackbar(
                        message = "Error: ${error.message ?: "No se pudo conectar al servidor"}",
                        duration = SnackbarDuration.Short
                    )
                }
            } catch (e: Exception) {
                Log.e("HomeScreen", "Excepción al cargar citas", e)
                snackbarHostState.showSnackbar(
                    message = "Error de conexión. Verifica que el backend esté corriendo.",
                    duration = SnackbarDuration.Long
                )
            } finally {
                isLoading = false
            }
        }
    }

    // Cargar citas cuando cambia el filtro o se solicita refresh
    LaunchedEffect(filtroActual, refreshTrigger) {
        cargarCitas()
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
                    label = { Text("Pendientes") },
                    enabled = !isLoading
                )
                FilterChip(
                    selected = filtroActual == FiltroCita.CONFIRMADAS,
                    onClick = { filtroActual = FiltroCita.CONFIRMADAS },
                    label = { Text("Confirmadas") },
                    enabled = !isLoading
                )
                FilterChip(
                    selected = filtroActual == FiltroCita.TODAS,
                    onClick = { filtroActual = FiltroCita.TODAS },
                    label = { Text("Todas") },
                    enabled = !isLoading
                )
            }

            // Estado de carga
            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        CircularProgressIndicator()
                        Text(
                            text = "Cargando citas...",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
            // Lista vacía
            else if (citas.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CalendarToday,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "No hay citas ${filtroActual.nombre}",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Button(onClick = { refreshTrigger++ }) {
                            Icon(Icons.Default.Refresh, null)
                            Spacer(Modifier.width(8.dp))
                            Text("Recargar")
                        }
                    }
                }
            }
            // Lista de citas
            else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(citas, key = { it.id }) { cita ->
                        CitaCard(
                            cita = cita,
                            onClick = { onNavigateToDetail(cita.id) },
                            onConfirmar = {
                                scope.launch {
                                    try {
                                        repository.confirmarCita(cita.id).onSuccess {
                                            snackbarHostState.showSnackbar("Cita confirmada ✓")
                                            refreshTrigger++ // ✅ Recarga explícita
                                        }.onFailure { error ->
                                            Log.e("HomeScreen", "Error confirmando", error)
                                            snackbarHostState.showSnackbar(
                                                "Error al confirmar: ${error.message}"
                                            )
                                        }
                                    } catch (e: Exception) {
                                        Log.e("HomeScreen", "Excepción confirmando", e)
                                        snackbarHostState.showSnackbar("Error de conexión")
                                    }
                                }
                            },
                            onEliminar = {
                                scope.launch {
                                    try {
                                        repository.deleteCita(cita.id).onSuccess {
                                            snackbarHostState.showSnackbar("Cita eliminada")
                                            refreshTrigger++ // ✅ Recarga explícita
                                        }.onFailure { error ->
                                            Log.e("HomeScreen", "Error eliminando", error)
                                            snackbarHostState.showSnackbar(
                                                "Error al eliminar: ${error.message}"
                                            )
                                        }
                                    } catch (e: Exception) {
                                        Log.e("HomeScreen", "Excepción eliminando", e)
                                        snackbarHostState.showSnackbar("Error de conexión")
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
    var showDeleteDialog by remember { mutableStateOf(false) }
    var isProcessing by remember { mutableStateOf(false) }

    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header con estado
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "🐶 ${cita.mascota.nombre}",
                    style = MaterialTheme.typography.titleMedium
                )

                // Badge de estado
                if (cita.confirmada) {
                    AssistChip(
                        onClick = {},
                        label = { Text("Confirmada") },
                        leadingIcon = {
                            Icon(
                                Icons.Default.CheckCircle,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                        },
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    )
                } else {
                    AssistChip(
                        onClick = {},
                        label = { Text("Pendiente") },
                        leadingIcon = {
                            Icon(
                                Icons.Default.Schedule,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                        },
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer
                        )
                    )
                }
            }

            Spacer(Modifier.height(8.dp))

            // Información
            Text(
                text = "Dueño: ${cita.usuario.nombre}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Veterinario: ${cita.veterinario.nombre}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "📅 ${cita.fechaCita} - ⏰ ${cita.horaCita}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            if (cita.motivo.isNotBlank()) {
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "Motivo: ${cita.motivo}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(Modifier.height(12.dp))

            // Botones de acción
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (!cita.confirmada) {
                    Button(
                        onClick = {
                            isProcessing = true
                            onConfirmar()
                        },
                        modifier = Modifier.weight(1f),
                        enabled = !isProcessing
                    ) {
                        if (isProcessing) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                strokeWidth = 2.dp
                            )
                        } else {
                            Icon(Icons.Default.Check, null, modifier = Modifier.size(18.dp))
                            Spacer(Modifier.width(4.dp))
                            Text("Confirmar")
                        }
                    }
                }

                OutlinedButton(
                    onClick = { showDeleteDialog = true },
                    modifier = Modifier.weight(1f),
                    enabled = !isProcessing,
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Icon(Icons.Default.Delete, null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("Eliminar")
                }
            }
        }
    }

    // Diálogo de confirmación de eliminación
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            icon = {
                Icon(
                    Icons.Default.Warning,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error
                )
            },
            title = { Text("¿Eliminar cita?") },
            text = {
                Text("Se eliminará la cita de ${cita.mascota.nombre} el ${cita.fechaCita}. Esta acción no se puede deshacer.")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                        isProcessing = true
                        onEliminar()
                    },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

enum class FiltroCita(val nombre: String) {
    PENDIENTES("pendientes"),
    CONFIRMADAS("confirmadas"),
    TODAS("todas")
}