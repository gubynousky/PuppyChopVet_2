package cl.martinez.puppychopvet.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cl.martinez.puppychopvet.network.CitaRepository
import cl.martinez.puppychopvet.network.models.*
import cl.martinez.puppychopvet.ui.components.AppTopBarWithBack
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    citaId: Long,
    onNavigateBack: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val repository = remember { CitaRepository() }
    val snackbarHostState = remember { SnackbarHostState() }

    var cita by remember { mutableStateOf<CitaVeterinariaResponse?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showConfirmDialog by remember { mutableStateOf(false) }

    // Cargar cita al inicio
    LaunchedEffect(citaId) {
        isLoading = true
        repository.getAllCitas().onSuccess { citas ->
            cita = citas.find { it.id == citaId }
        }.onFailure {
            snackbarHostState.showSnackbar("Error al cargar la cita")
        }
        isLoading = false
    }

    // Diálogo eliminar
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            icon = {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error
                )
            },
            title = { Text("Eliminar cita") },
            text = {
                Text("¿Estás seguro de eliminar la cita de ${cita?.mascota?.nombre}? Esta acción no se puede deshacer.")
            },
            confirmButton = {
                Button(
                    onClick = {
                        scope.launch {
                            repository.deleteCita(citaId).onSuccess {
                                showDeleteDialog = false
                                snackbarHostState.showSnackbar("Cita eliminada")
                                kotlinx.coroutines.delay(500)
                                onNavigateBack()
                            }.onFailure {
                                snackbarHostState.showSnackbar("Error al eliminar")
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
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    // Diálogo confirmar/desconfirmar
    if (showConfirmDialog && cita != null) {
        val citaActual = cita!!
        val nuevoEstado = !citaActual.confirmada

        AlertDialog(
            onDismissRequest = { showConfirmDialog = false },
            icon = {
                Icon(
                    imageVector = if (citaActual.confirmada) Icons.Default.Refresh else Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            },
            title = {
                Text(if (citaActual.confirmada) "Marcar como pendiente" else "Confirmar cita")
            },
            text = {
                val accion = if (citaActual.confirmada) "pendiente" else "confirmada"
                Text("¿Deseas marcar la cita de ${citaActual.mascota.nombre} como $accion?")
            },
            confirmButton = {
                Button(
                    onClick = {
                        scope.launch {
                            val citaActualizada = CitaVeterinariaRequest(
                                usuario = UsuarioRef(citaActual.usuario.id!!),
                                mascota = MascotaRef(citaActual.mascota.id!!),
                                veterinario = VeterinarioRef(citaActual.veterinario.id!!),
                                fechaCita = citaActual.fechaCita,
                                horaCita = citaActual.horaCita,
                                tipoServicio = citaActual.tipoServicio,
                                motivo = citaActual.motivo,
                                prioridad = citaActual.prioridad,
                                confirmada = nuevoEstado,
                                notificacionActiva = citaActual.notificacionActiva,
                                notas = citaActual.notas
                            )

                            repository.actualizarCita(citaId, citaActualizada).onSuccess {
                                showConfirmDialog = false
                                val mensaje = if (nuevoEstado) {
                                    "Cita confirmada ✓"
                                } else {
                                    "Marcada como pendiente"
                                }
                                snackbarHostState.showSnackbar(mensaje)
                                kotlinx.coroutines.delay(500)
                                onNavigateBack()
                            }.onFailure {
                                snackbarHostState.showSnackbar("Error al actualizar: ${it.message}")
                            }
                        }
                    }
                ) {
                    Text("Confirmar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showConfirmDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            AppTopBarWithBack(
                title = "Detalle de Cita",
                onBackClick = onNavigateBack
            )
        }
    ) { paddingValues ->

        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (cita == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Error,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.error
                    )
                    Text("Cita no encontrada")
                    Button(onClick = onNavigateBack) {
                        Text("Volver")
                    }
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                val citaActual = cita!!

                // Card de estado
                if (citaActual.confirmada) {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.secondary
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Cita Confirmada",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                    }
                }

                // SECCIÓN: Datos del Dueño
                Text(
                    text = "👤 Datos del Dueño",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        InfoRow(
                            icon = Icons.Default.Person,
                            label = "Nombre",
                            value = citaActual.usuario.nombre
                        )
                        HorizontalDivider()
                        InfoRow(
                            icon = Icons.Default.Phone,
                            label = "Teléfono",
                            value = citaActual.usuario.telefono
                        )
                        HorizontalDivider()
                        InfoRow(
                            icon = Icons.Default.Email,
                            label = "Email",
                            value = citaActual.usuario.email
                        )
                        HorizontalDivider()
                        InfoRow(
                            icon = Icons.Default.Home,
                            label = "Dirección",
                            value = citaActual.usuario.direccion
                        )
                    }
                }

                // SECCIÓN: Datos de la Mascota
                Text(
                    text = "🐶 Datos de la Mascota",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        InfoRow(
                            icon = Icons.Default.Pets,
                            label = "Nombre",
                            value = citaActual.mascota.nombre
                        )
                        HorizontalDivider()
                        InfoRow(
                            icon = Icons.Default.Category,
                            label = "Raza",
                            value = citaActual.mascota.raza
                        )
                        HorizontalDivider()
                        InfoRow(
                            icon = Icons.Default.Cake,
                            label = "Edad",
                            value = "${citaActual.mascota.edad} años"
                        )
                    }
                }

                // SECCIÓN: Información de la Cita
                Text(
                    text = "📋 Información de la Cita",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        InfoRow(
                            icon = Icons.Default.MedicalServices,
                            label = "Tipo de Servicio",
                            value = citaActual.tipoServicio
                        )
                        HorizontalDivider()
                        InfoRow(
                            icon = Icons.Default.CalendarToday,
                            label = "Fecha",
                            value = citaActual.fechaCita
                        )
                        HorizontalDivider()
                        InfoRow(
                            icon = Icons.Default.Schedule,
                            label = "Hora",
                            value = citaActual.horaCita
                        )
                        HorizontalDivider()
                        InfoRow(
                            icon = Icons.Default.LocalHospital,
                            label = "Veterinario",
                            value = citaActual.veterinario.nombre
                        )
                        HorizontalDivider()
                        InfoRow(
                            icon = Icons.Default.Flag,
                            label = "Prioridad",
                            value = citaActual.prioridad
                        )
                    }
                }

                // Motivo
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Description,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = "Motivo de la Consulta",
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = citaActual.motivo,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }

                // Notas
                if (citaActual.notas.isNotEmpty()) {
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Note,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = "Notas Adicionales",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = citaActual.notas,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                }

                // Notificación
                Card(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (citaActual.notificacionActiva) Icons.Default.Notifications else Icons.Default.NotificationsOff,
                            contentDescription = null,
                            tint = if (citaActual.notificacionActiva) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Notificaciones",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = if (citaActual.notificacionActiva) "Activadas" else "Desactivadas",
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Botón confirmar/desconfirmar
                Button(
                    onClick = { showConfirmDialog = true },
                    modifier = Modifier.fillMaxWidth(),
                    colors = if (citaActual.confirmada) {
                        ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondary
                        )
                    } else {
                        ButtonDefaults.buttonColors()
                    }
                ) {
                    Icon(
                        imageVector = if (citaActual.confirmada) Icons.Default.Refresh else Icons.Default.CheckCircle,
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (citaActual.confirmada) "Marcar como Pendiente" else "Confirmar Cita"
                    )
                }

                // Botón eliminar
                OutlinedButton(
                    onClick = { showDeleteDialog = true },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Eliminar Cita")
                }
            }
        }
    }
}

@Composable
fun InfoRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
        }
    }
}