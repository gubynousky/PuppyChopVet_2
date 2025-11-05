package cl.martinez.puppychopvet.view

import androidx.compose.animation.*
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import cl.martinez.puppychopvet.data.CitaVeterinaria
import cl.martinez.puppychopvet.model.Prioridad
import cl.martinez.puppychopvet.model.TipoServicio
import cl.martinez.puppychopvet.ui.components.AppTopBar
import cl.martinez.puppychopvet.viewmodel.CitaVeterinariaViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: CitaVeterinariaViewModel,
    onNavigateToForm: () -> Unit,
    onNavigateToDetail: (Int) -> Unit
) {
    val citasPendientes by viewModel.citasPendientes.collectAsState(initial = emptyList())
    val citasConfirmadas by viewModel.citasConfirmadas.collectAsState(initial = emptyList())

    var filtroActual by remember { mutableStateOf(FiltroCita.PENDIENTES) }

    val citasMostradas = when (filtroActual) {
        FiltroCita.PENDIENTES -> citasPendientes
        FiltroCita.CONFIRMADAS -> citasConfirmadas
        FiltroCita.TODAS -> citasPendientes + citasConfirmadas
    }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var showDeleteAllDialog by remember { mutableStateOf(false) }
    var citaToDelete by remember { mutableStateOf<CitaVeterinaria?>(null) }

    // Diálogo eliminar todas confirmadas
    if (showDeleteAllDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteAllDialog = false },
            icon = {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error
                )
            },
            title = { Text("Eliminar citas confirmadas") },
            text = {
                Text("¿Estás seguro de eliminar ${citasConfirmadas.size} citas confirmadas? Esta acción no se puede deshacer.")
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deleteAllConfirmadas()
                        showDeleteAllDialog = false
                        scope.launch {
                            snackbarHostState.showSnackbar(
                                message = "Se eliminaron ${citasConfirmadas.size} citas",
                                duration = SnackbarDuration.Short
                            )
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteAllDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    // Diálogo eliminar cita individual
    citaToDelete?.let { cita ->
        AlertDialog(
            onDismissRequest = { citaToDelete = null },
            icon = {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error
                )
            },
            title = { Text("Eliminar cita") },
            text = {
                Text("¿Deseas eliminar la cita de ${cita.nombreMascota}? Podrás deshacerlo después.")
            },
            confirmButton = {
                Button(
                    onClick = {
                        val citaTemp = cita
                        viewModel.deleteCita(cita)
                        citaToDelete = null
                        scope.launch {
                            val result = snackbarHostState.showSnackbar(
                                message = "Cita eliminada",
                                actionLabel = "Deshacer",
                                duration = SnackbarDuration.Long
                            )
                            if (result == SnackbarResult.ActionPerformed) {
                                viewModel.insertCita(citaTemp)
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                TextButton(onClick = { citaToDelete = null }) {
                    Text("Cancelar")
                }
            }
        )
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { data ->
                Snackbar(
                    snackbarData = data,
                    containerColor = MaterialTheme.colorScheme.inverseSurface,
                    contentColor = MaterialTheme.colorScheme.inverseOnSurface,
                    actionColor = MaterialTheme.colorScheme.primary
                )
            }
        },
        topBar = {
            AppTopBar(
                title = "PuppyChop Citas",
                actions = {
                    if (citasConfirmadas.isNotEmpty()) {
                        IconButton(onClick = { showDeleteAllDialog = true }) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Eliminar confirmadas",
                                tint = Color.White
                            )
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToForm,
                containerColor = MaterialTheme.colorScheme.secondary,
                elevation = FloatingActionButtonDefaults.elevation(
                    defaultElevation = 8.dp,
                    pressedElevation = 12.dp,
                    hoveredElevation = 10.dp
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Nueva cita",
                    tint = Color.White
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            FiltroChips(
                filtroActual = filtroActual,
                onFiltroChange = { filtroActual = it },
                cantidadPendientes = citasPendientes.size,
                cantidadConfirmadas = citasConfirmadas.size
            )

            AnimatedVisibility(
                visible = citasMostradas.isEmpty(),
                enter = fadeIn(animationSpec = tween(300)),
                exit = fadeOut(animationSpec = tween(300))
            ) {
                EmptyState(filtro = filtroActual)
            }

            AnimatedVisibility(
                visible = citasMostradas.isNotEmpty(),
                enter = fadeIn(animationSpec = tween(300)),
                exit = fadeOut(animationSpec = tween(300))
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(
                        items = citasMostradas,
                        key = { it.id }
                    ) { cita ->
                        AnimatedVisibility(
                            visible = true,
                            enter = fadeIn(animationSpec = tween(300)) +
                                    slideInVertically(
                                        animationSpec = spring(
                                            dampingRatio = Spring.DampingRatioMediumBouncy,
                                            stiffness = Spring.StiffnessLow
                                        )
                                    ),
                            exit = fadeOut(animationSpec = tween(300)) +
                                    slideOutVertically(animationSpec = tween(300))
                        ) {
                            CitaItem(
                                cita = cita,
                                onToggleConfirmada = {
                                    viewModel.toggleConfirmada(cita.id, !cita.confirmada)
                                    scope.launch {
                                        val mensaje = if (!cita.confirmada) {
                                            "Cita confirmada ✓"
                                        } else {
                                            "Cita marcada como pendiente"
                                        }
                                        snackbarHostState.showSnackbar(
                                            message = mensaje,
                                            duration = SnackbarDuration.Short
                                        )
                                    }
                                },
                                onDelete = {
                                    citaToDelete = cita
                                },
                                onClick = { onNavigateToDetail(cita.id) }
                            )
                        }
                    }
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

@Composable
fun FiltroChips(
    filtroActual: FiltroCita,
    onFiltroChange: (FiltroCita) -> Unit,
    cantidadPendientes: Int,
    cantidadConfirmadas: Int
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                        MaterialTheme.colorScheme.surface
                    )
                )
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            FilterChip(
                selected = filtroActual == FiltroCita.PENDIENTES,
                onClick = { onFiltroChange(FiltroCita.PENDIENTES) },
                label = {
                    Text(
                        text = "Pendientes ($cantidadPendientes)",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = if (filtroActual == FiltroCita.PENDIENTES) FontWeight.Bold else FontWeight.Normal
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Schedule,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                    selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    selectedLeadingIconColor = MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier.height(42.dp)
            )

            FilterChip(
                selected = filtroActual == FiltroCita.CONFIRMADAS,
                onClick = { onFiltroChange(FiltroCita.CONFIRMADAS) },
                label = {
                    Text(
                        text = "Confirmadas ($cantidadConfirmadas)",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = if (filtroActual == FiltroCita.CONFIRMADAS) FontWeight.Bold else FontWeight.Normal
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                    selectedLabelColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    selectedLeadingIconColor = MaterialTheme.colorScheme.secondary
                ),
                modifier = Modifier.height(42.dp)
            )

            FilterChip(
                selected = filtroActual == FiltroCita.TODAS,
                onClick = { onFiltroChange(FiltroCita.TODAS) },
                label = {
                    Text(
                        text = "Todas",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = if (filtroActual == FiltroCita.TODAS) FontWeight.Bold else FontWeight.Normal
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.List,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.tertiaryContainer,
                    selectedLabelColor = MaterialTheme.colorScheme.onTertiaryContainer,
                    selectedLeadingIconColor = MaterialTheme.colorScheme.tertiary
                ),
                modifier = Modifier.height(42.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CitaItem(
    cita: CitaVeterinaria,
    onToggleConfirmada: () -> Unit,
    onDelete: () -> Unit,
    onClick: () -> Unit
) {
    val prioridad = Prioridad.fromString(cita.prioridad)
    val colorPrioridad = Color(prioridad.colorValue)
    val tipoServicio = TipoServicio.fromString(cita.tipoServicio)

    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (cita.confirmada) {
                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)
            } else {
                MaterialTheme.colorScheme.surface
            }
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp,
            pressedElevation = 8.dp,
            hoveredElevation = 6.dp
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Barra lateral de prioridad
            Box(
                modifier = Modifier
                    .width(6.dp)
                    .fillMaxHeight()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                colorPrioridad,
                                colorPrioridad.copy(alpha = 0.6f)
                            )
                        )
                    )
            )

            Row(
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = cita.confirmada,
                    onCheckedChange = { onToggleConfirmada() }
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Pets,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = cita.nombreMascota,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            textDecoration = if (cita.confirmada) TextDecoration.LineThrough else null,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = cita.nombreDueno,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AssistChip(
                            onClick = {},
                            label = {
                                Text(
                                    text = tipoServicio.displayName,
                                    style = MaterialTheme.typography.labelSmall
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.MedicalServices,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        )

                        AssistChip(
                            onClick = {},
                            label = {
                                Text(
                                    text = formatearFechaHome(cita.fechaCita),
                                    style = MaterialTheme.typography.labelSmall
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.CalendarToday,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        )
                    }
                }

                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Eliminar",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

@Composable
fun EmptyState(filtro: FiltroCita) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = when (filtro) {
                    FiltroCita.PENDIENTES -> Icons.Default.Schedule
                    FiltroCita.CONFIRMADAS -> Icons.Default.CheckCircle
                    FiltroCita.TODAS -> Icons.Default.Pets
                },
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = when (filtro) {
                    FiltroCita.PENDIENTES -> "No hay citas pendientes"
                    FiltroCita.CONFIRMADAS -> "No hay citas confirmadas"
                    FiltroCita.TODAS -> "No hay citas agendadas"
                },
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = when (filtro) {
                    FiltroCita.PENDIENTES -> "Agenda la primera cita"
                    FiltroCita.CONFIRMADAS -> "Confirma algunas citas"
                    FiltroCita.TODAS -> "Presiona + para crear una"
                },
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

// Función privada para formatear fechas en HomeScreen
private fun formatearFechaHome(millis: Long): String {
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    dateFormat.timeZone = TimeZone.getDefault()
    return dateFormat.format(Date(millis))
}