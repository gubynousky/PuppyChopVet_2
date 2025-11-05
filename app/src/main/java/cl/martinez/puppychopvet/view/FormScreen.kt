package cl.martinez.puppychopvet.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import cl.martinez.puppychopvet.model.TipoServicio
import cl.martinez.puppychopvet.model.Prioridad
import cl.martinez.puppychopvet.model.Veterinario
import cl.martinez.puppychopvet.ui.components.AppTopBarWithBack
import cl.martinez.puppychopvet.viewmodel.CitaVeterinariaViewModel
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.TimeZone

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormScreen(
    viewModel: CitaVeterinariaViewModel,
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }
    var expandedTipoServicio by remember { mutableStateOf(false) }
    var expandedVeterinario by remember { mutableStateOf(false) }
    var expandedPrioridad by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(uiState.guardadoExitoso) {
        if (uiState.guardadoExitoso) {
            snackbarHostState.showSnackbar(
                message = "Cita agendada exitosamente 🐶",
                duration = SnackbarDuration.Short
            )
            kotlinx.coroutines.delay(500)
            viewModel.resetForm()
            onNavigateBack()
        }
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
                title = "Nueva Cita Veterinaria",
                onBackClick = onNavigateBack
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Sección: Datos del Dueño
            Text(
                text = "👤 Datos del Dueño",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )

            OutlinedTextField(
                value = uiState.nombreDueno,
                onValueChange = { viewModel.onNombreDuenoChange(it) },
                label = { Text("Nombre Completo *") },
                leadingIcon = { Icon(Icons.Default.Person, null) },
                isError = uiState.nombreDuenoError != null,
                supportingText = { uiState.nombreDuenoError?.let { Text(it) } },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = uiState.telefono,
                onValueChange = { viewModel.onTelefonoChange(it) },
                label = { Text("Teléfono *") },
                leadingIcon = { Icon(Icons.Default.Phone, null) },
                isError = uiState.telefonoError != null,
                supportingText = { uiState.telefonoError?.let { Text(it) } },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = uiState.email,
                onValueChange = { viewModel.onEmailChange(it) },
                label = { Text("Email *") },
                leadingIcon = { Icon(Icons.Default.Email, null) },
                isError = uiState.emailError != null,
                supportingText = { uiState.emailError?.let { Text(it) } },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Divider(modifier = Modifier.padding(vertical = 8.dp))

            // Sección: Datos de la Mascota
            Text(
                text = "🐶 Datos de la Mascota",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )

            OutlinedTextField(
                value = uiState.nombreMascota,
                onValueChange = { viewModel.onNombreMascotaChange(it) },
                label = { Text("Nombre de la Mascota *") },
                leadingIcon = { Icon(Icons.Default.Pets, null) },
                isError = uiState.nombreMascotaError != null,
                supportingText = { uiState.nombreMascotaError?.let { Text(it) } },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = uiState.raza,
                    onValueChange = { viewModel.onRazaChange(it) },
                    label = { Text("Raza *") },
                    isError = uiState.razaError != null,
                    supportingText = { uiState.razaError?.let { Text(it) } },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )

                OutlinedTextField(
                    value = uiState.edad,
                    onValueChange = { viewModel.onEdadChange(it) },
                    label = { Text("Edad *") },
                    isError = uiState.edadError != null,
                    supportingText = { uiState.edadError?.let { Text(it) } },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
            }

            Divider(modifier = Modifier.padding(vertical = 8.dp))

            // Sección: Información de la Cita
            Text(
                text = "📋 Información de la Cita",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )

            // Tipo de Servicio
            ExposedDropdownMenuBox(
                expanded = expandedTipoServicio,
                onExpandedChange = { expandedTipoServicio = !expandedTipoServicio }
            ) {
                OutlinedTextField(
                    value = uiState.tipoServicio,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Tipo de Servicio *") },
                    leadingIcon = { Icon(Icons.Default.MedicalServices, null) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expandedTipoServicio) },
                    isError = uiState.tipoServicioError != null,
                    supportingText = { uiState.tipoServicioError?.let { Text(it) } },
                    modifier = Modifier.fillMaxWidth().menuAnchor(),
                    colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
                )

                ExposedDropdownMenu(
                    expanded = expandedTipoServicio,
                    onDismissRequest = { expandedTipoServicio = false }
                ) {
                    TipoServicio.entries.forEach { tipo ->
                        DropdownMenuItem(
                            text = { Text(tipo.displayName) },
                            onClick = {
                                viewModel.onTipoServicioChange(tipo.name)
                                expandedTipoServicio = false
                            }
                        )
                    }
                }
            }

            OutlinedTextField(
                value = uiState.motivo,
                onValueChange = { viewModel.onMotivoChange(it) },
                label = { Text("Motivo de la Consulta *") },
                placeholder = { Text("Describe el motivo de la visita") },
                leadingIcon = { Icon(Icons.Default.Description, null) },
                isError = uiState.motivoError != null,
                supportingText = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(uiState.motivoError ?: "")
                        Text("${uiState.motivo.length}/300")
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
                maxLines = 5
            )

            // Fecha y Hora
            OutlinedTextField(
                value = if (uiState.fechaCita != null) {
                    formatFecha(uiState.fechaCita!!)
                } else "",
                onValueChange = {},
                readOnly = true,
                label = { Text("Fecha de la Cita *") },
                leadingIcon = { Icon(Icons.Default.CalendarToday, null) },
                trailingIcon = {
                    IconButton(onClick = { showDatePicker = true }) {
                        Icon(Icons.Default.EditCalendar, "Seleccionar fecha")
                    }
                },
                isError = uiState.fechaCitaError != null,
                supportingText = { uiState.fechaCitaError?.let { Text(it) } },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = uiState.horaCita,
                onValueChange = { viewModel.onHoraCitaChange(it) },
                label = { Text("Hora (HH:mm) *") },
                placeholder = { Text("Ej: 14:30") },
                leadingIcon = { Icon(Icons.Default.Schedule, null) },
                trailingIcon = {
                    IconButton(onClick = { showTimePicker = true }) {
                        Icon(Icons.Default.AccessTime, "Seleccionar hora")
                    }
                },
                isError = uiState.horaError != null,
                supportingText = { uiState.horaError?.let { Text(it) } },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Veterinario
            ExposedDropdownMenuBox(
                expanded = expandedVeterinario,
                onExpandedChange = { expandedVeterinario = !expandedVeterinario }
            ) {
                OutlinedTextField(
                    value = uiState.veterinario,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Veterinario *") },
                    leadingIcon = { Icon(Icons.Default.LocalHospital, null) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expandedVeterinario) },
                    isError = uiState.veterinarioError != null,
                    supportingText = { uiState.veterinarioError?.let { Text(it) } },
                    modifier = Modifier.fillMaxWidth().menuAnchor(),
                    colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
                )

                ExposedDropdownMenu(
                    expanded = expandedVeterinario,
                    onDismissRequest = { expandedVeterinario = false }
                ) {
                    Veterinario.entries.forEach { vet ->
                        DropdownMenuItem(
                            text = {
                                Column {
                                    Text(vet.displayName)
                                    Text(
                                        text = vet.especialidad,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            },
                            onClick = {
                                viewModel.onVeterinarioChange(vet.name)
                                expandedVeterinario = false
                            }
                        )
                    }
                }
            }

            // Prioridad
            ExposedDropdownMenuBox(
                expanded = expandedPrioridad,
                onExpandedChange = { expandedPrioridad = !expandedPrioridad }
            ) {
                OutlinedTextField(
                    value = uiState.prioridad,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Prioridad *") },
                    leadingIcon = { Icon(Icons.Default.Flag, null) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expandedPrioridad) },
                    isError = uiState.prioridadError != null,
                    supportingText = { uiState.prioridadError?.let { Text(it) } },
                    modifier = Modifier.fillMaxWidth().menuAnchor(),
                    colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
                )

                ExposedDropdownMenu(
                    expanded = expandedPrioridad,
                    onDismissRequest = { expandedPrioridad = false }
                ) {
                    Prioridad.entries.forEach { prioridad ->
                        DropdownMenuItem(
                            text = { Text(prioridad.displayName) },
                            onClick = {
                                viewModel.onPrioridadChange(prioridad.name)
                                expandedPrioridad = false
                            }
                        )
                    }
                }
            }

            // Notas adicionales
            OutlinedTextField(
                value = uiState.notas,
                onValueChange = { viewModel.onNotasChange(it) },
                label = { Text("Notas Adicionales") },
                placeholder = { Text("Información adicional...") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 2,
                maxLines = 4
            )

            // Switch notificaciones
            Card(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            Icons.Default.Notifications,
                            null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Column {
                            Text(
                                "Recordatorio de cita",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                "Recibir notificación antes de la cita",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    Switch(
                        checked = uiState.notificacionActiva,
                        onCheckedChange = { viewModel.toggleNotificacion() }
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Botón guardar
            Button(
                onClick = { viewModel.onGuardarClick() },
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isLoading
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Icon(Icons.Default.Save, null)
                    Spacer(Modifier.width(8.dp))
                    Text("Agendar Cita")
                }
            }
        }
    }

    // DatePicker
    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = uiState.fechaCita ?: System.currentTimeMillis()
        )

        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { utcMillis ->
                        val calendar = Calendar.getInstance()
                        val timeZone = TimeZone.getDefault()
                        val offset = timeZone.getOffset(utcMillis)
                        val localMillis = utcMillis + offset
                        calendar.timeInMillis = localMillis
                        calendar.set(Calendar.HOUR_OF_DAY, 12)
                        calendar.set(Calendar.MINUTE, 0)
                        calendar.set(Calendar.SECOND, 0)
                        calendar.set(Calendar.MILLISECOND, 0)
                        viewModel.onFechaCitaChange(calendar.timeInMillis)
                    }
                    showDatePicker = false
                }) { Text("Aceptar") }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("Cancelar") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    // TimePicker
    if (showTimePicker) {
        val currentTime = try {
            if (uiState.horaCita.isNotEmpty()) {
                val parts = uiState.horaCita.split(":")
                Pair(parts[0].toInt(), parts[1].toInt())
            } else {
                val calendar = Calendar.getInstance()
                Pair(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE))
            }
        } catch (e: Exception) {
            val calendar = Calendar.getInstance()
            Pair(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE))
        }

        val timePickerState = rememberTimePickerState(
            initialHour = currentTime.first,
            initialMinute = currentTime.second,
            is24Hour = true
        )

        AlertDialog(
            onDismissRequest = { showTimePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    val hora = String.format("%02d:%02d", timePickerState.hour, timePickerState.minute)
                    viewModel.onHoraCitaChange(hora)
                    showTimePicker = false
                }) { Text("Aceptar") }
            },
            dismissButton = {
                TextButton(onClick = { showTimePicker = false }) { Text("Cancelar") }
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "Seleccionar Hora",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    TimePicker(
                        state = timePickerState,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        )
    }
}

fun formatFecha(millis: Long): String {
    val dateFormat = java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault())
    dateFormat.timeZone = java.util.TimeZone.getDefault()
    return dateFormat.format(java.util.Date(millis))
}