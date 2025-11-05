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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cl.martinez.puppychopvet.data.CitaVeterinaria
import cl.martinez.puppychopvet.model.Prioridad
import cl.martinez.puppychopvet.model.TipoServicio
import cl.martinez.puppychopvet.model.Veterinario
import cl.martinez.puppychopvet.ui.components.AppTopBarWithBack
import cl.martinez.puppychopvet.utils.ShareHelper
import cl.martinez.puppychopvet.viewmodel.CitaVeterinariaViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    citaId: Int,
    viewModel: CitaVeterinariaViewModel,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var cita by remember { mutableStateOf<CitaVeterinaria?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showToggleDialog by remember { mutableStateOf(false) }

    LaunchedEffect(citaId) {
        scope.launch {
            cita = viewModel.getCitaById(citaId)
        }
    }

    // Diálogo eliminar
    if (showDeleteDialog && cita != null) {
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
                Text("¿Estás seguro de eliminar la cita de ${cita?.nombreMascota}? Esta acción no se puede deshacer.")
            },
            confirmButton = {
                Button(
                    onClick = {
                        scope.launch {
                            cita?.let { viewModel.deleteCita(it) }
                            showDeleteDialog = false
                            snackbarHostState.showSnackbar(
                                message = "Cita eliminada",
                                duration = SnackbarDuration.Short
                            )
                            kotlinx.coroutines.delay(500)
                            onNavigateBack()
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
    if (showToggleDialog && cita != null) {
        val citaActual = cita!!
        AlertDialog(
            onDismissRequest = { showToggleDialog = false },
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
                Text("¿Deseas marcar la cita de ${citaActual.nombreMascota} como $accion?")
            },
            confirmButton = {
                Button(
                    onClick = {
                        scope.launch {
                            viewModel.toggleConfirmada(citaActual.id, !citaActual.confirmada)
                            showToggleDialog = false
                            val mensaje = if (!citaActual.confirmada) {
                                "Cita confirmada ✓"
                            } else {
                                "Marcada como pendiente"
                            }
                            snackbarHostState.showSnackbar(
                                message = mensaje,
                                duration = SnackbarDuration.Short
                            )
                            kotlinx.coroutines.delay(500)
                            onNavigateBack()
                        }
                    }
                ) {
                    Text("Confirmar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showToggleDialog = false }) {
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
            AppTopBarWithBack(
                title = "Detalle de Cita",
                onBackClick = onNavigateBack,
                actions = {
                    IconButton(
                        onClick = {
                            cita?.let {
                                ShareHelper.compartirCita(context, it)
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Compartir"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        if (cita == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
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
                val prioridad = Prioridad.fromString(citaActual.prioridad)
                val tipoServicio = TipoServicio.fromString(citaActual.tipoServicio)
                val veterinario = Veterinario.fromString(citaActual.veterinario)

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

                // Sección: Datos del Dueño
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
                            value = citaActual.nombreDueno
                        )
                        HorizontalDivider()
                        InfoRow(
                            icon = Icons.Default.Phone,
                            label = "Teléfono",
                            value = citaActual.telefono
                        )
                        HorizontalDivider()
                        InfoRow(
                            icon = Icons.Default.Email,
                            label = "Email",
                            value = citaActual.email
                        )
                    }
                }

                // Sección: Datos de la Mascota
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
                            value = citaActual.nombreMascota
                        )
                        HorizontalDivider()
                        InfoRow(
                            icon = Icons.Default.Category,
                            label = "Raza",
                            value = citaActual.raza
                        )
                        HorizontalDivider()
                        InfoRow(
                            icon = Icons.Default.Cake,
                            label = "Edad",
                            value = "${citaActual.edad} años"
                        )
                    }
                }

                // Sección: Información de la Cita
                Text(
                    text = "📋 Información de la Cita",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Card(
                        modifier = Modifier.weight(1f),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.MedicalServices,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Servicio",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Text(
                                text = tipoServicio.displayName,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }

                    Card(
                        modifier = Modifier.weight(1f),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(prioridad.colorValue)
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.Flag,
                                contentDescription = null,
                                tint = Color.White
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Prioridad",
                                style = MaterialTheme.typography.labelMedium,
                                color = Color.White
                            )
                            Text(
                                text = prioridad.displayName,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }

                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        InfoRow(
                            icon = Icons.Default.CalendarToday,
                            label = "Fecha",
                            value = formatearFechaDetail(citaActual.fechaCita)
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
                            value = veterinario.displayName
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
                    onClick = { showToggleDialog = true },
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

// Función privada para formatear fechas en DetailScreen
private fun formatearFechaDetail(millis: Long): String {
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    dateFormat.timeZone = TimeZone.getDefault()
    return dateFormat.format(Date(millis))
}