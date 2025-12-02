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
import cl.martinez.puppychopvet.network.CitaRepository
import cl.martinez.puppychopvet.network.models.*
import cl.martinez.puppychopvet.ui.components.AppTopBarWithBack
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormScreen(
    onNavigateBack: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val repository = remember { CitaRepository() }
    val snackbarHostState = remember { SnackbarHostState() }

    // Estados para veterinarios desde BD
    var veterinarios by remember { mutableStateOf<List<Veterinario>>(emptyList()) }
    var isLoadingVets by remember { mutableStateOf(false) }

    // Estados del formulario - DATOS DEL DUEÑO
    var nombreDueno by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }

    // Estados del formulario - DATOS DE LA MASCOTA
    var nombreMascota by remember { mutableStateOf("") }
    var raza by remember { mutableStateOf("") }
    var edad by remember { mutableStateOf("") }

    // Estados del formulario - DATOS DE LA CITA
    var tipoServicio by remember { mutableStateOf("") }
    var motivo by remember { mutableStateOf("") }
    var fechaCita by remember { mutableStateOf<Long?>(null) }
    var horaCita by remember { mutableStateOf("") }
    var veterinarioSeleccionado by remember { mutableStateOf<Veterinario?>(null) }
    var prioridad by remember { mutableStateOf("") }
    var notas by remember { mutableStateOf("") }
    var notificacionActiva by remember { mutableStateOf(true) }

    // Estados de UI
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }
    var expandedTipoServicio by remember { mutableStateOf(false) }
    var expandedVeterinario by remember { mutableStateOf(false) }
    var expandedPrioridad by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

    // Cargar veterinarios al inicio
    LaunchedEffect(Unit) {
        isLoadingVets = true
        repository.getAllVeterinarios().onSuccess { vets ->
            veterinarios = vets
        }.onFailure {
            snackbarHostState.showSnackbar("Error al cargar veterinarios")
        }
        isLoadingVets = false
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
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
            // SECCIÓN: Datos del Dueño
            Text(
                text = "👤 Datos del Dueño",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )

            OutlinedTextField(
                value = nombreDueno,
                onValueChange = { nombreDueno = it },
                label = { Text("Nombre Completo *") },
                leadingIcon = { Icon(Icons.Default.Person, null) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = telefono,
                onValueChange = { telefono = it },
                label = { Text("Teléfono *") },
                leadingIcon = { Icon(Icons.Default.Phone, null) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email *") },
                leadingIcon = { Icon(Icons.Default.Email, null) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = direccion,
                onValueChange = { direccion = it },
                label = { Text("Dirección *") },
                leadingIcon = { Icon(Icons.Default.Home, null) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            // SECCIÓN: Datos de la Mascota
            Text(
                text = "🐶 Datos de la Mascota",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )

            OutlinedTextField(
                value = nombreMascota,
                onValueChange = { nombreMascota = it },
                label = { Text("Nombre de la Mascota *") },
                leadingIcon = { Icon(Icons.Default.Pets, null) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = raza,
                    onValueChange = { raza = it },
                    label = { Text("Raza *") },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )

                OutlinedTextField(
                    value = edad,
                    onValueChange = { edad = it },
                    label = { Text("Edad *") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            // SECCIÓN: Información de la Cita
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
                    value = tipoServicio,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Tipo de Servicio *") },
                    leadingIcon = { Icon(Icons.Default.MedicalServices, null) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expandedTipoServicio) },
                    modifier = Modifier.fillMaxWidth().menuAnchor(),
                    colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
                )

                ExposedDropdownMenu(
                    expanded = expandedTipoServicio,
                    onDismissRequest = { expandedTipoServicio = false }
                ) {
                    TipoServicio.entries.forEach { tipo ->
                        DropdownMenuItem(
                            text = { Text(tipo.name) },
                            onClick = {
                                tipoServicio = tipo.name
                                expandedTipoServicio = false
                            }
                        )
                    }
                }
            }

            OutlinedTextField(
                value = motivo,
                onValueChange = { motivo = it },
                label = { Text("Motivo de la Consulta *") },
                placeholder = { Text("Describe el motivo de la visita") },
                leadingIcon = { Icon(Icons.Default.Description, null) },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
                maxLines = 5
            )

            // Fecha
            OutlinedTextField(
                value = if (fechaCita != null) formatFecha(fechaCita!!) else "",
                onValueChange = {},
                readOnly = true,
                label = { Text("Fecha de la Cita *") },
                leadingIcon = { Icon(Icons.Default.CalendarToday, null) },
                trailingIcon = {
                    IconButton(onClick = { showDatePicker = true }) {
                        Icon(Icons.Default.EditCalendar, "Seleccionar fecha")
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            // Hora
            OutlinedTextField(
                value = horaCita,
                onValueChange = { horaCita = it },
                label = { Text("Hora (HH:mm) *") },
                placeholder = { Text("Ej: 14:30") },
                leadingIcon = { Icon(Icons.Default.Schedule, null) },
                trailingIcon = {
                    IconButton(onClick = { showTimePicker = true }) {
                        Icon(Icons.Default.AccessTime, "Seleccionar hora")
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Veterinario desde BD
            if (isLoadingVets) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            } else {
                ExposedDropdownMenuBox(
                    expanded = expandedVeterinario,
                    onExpandedChange = { expandedVeterinario = !expandedVeterinario }
                ) {
                    OutlinedTextField(
                        value = veterinarioSeleccionado?.nombre ?: "",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Veterinario *") },
                        leadingIcon = { Icon(Icons.Default.LocalHospital, null) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expandedVeterinario) },
                        modifier = Modifier.fillMaxWidth().menuAnchor(),
                        colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
                    )

                    ExposedDropdownMenu(
                        expanded = expandedVeterinario,
                        onDismissRequest = { expandedVeterinario = false }
                    ) {
                        veterinarios.forEach { vet ->
                            DropdownMenuItem(
                                text = {
                                    Column {
                                        Text(vet.nombre)
                                        Text(
                                            text = vet.especialidad,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                },
                                onClick = {
                                    veterinarioSeleccionado = vet
                                    expandedVeterinario = false
                                }
                            )
                        }
                    }
                }
            }

            // Prioridad
            ExposedDropdownMenuBox(
                expanded = expandedPrioridad,
                onExpandedChange = { expandedPrioridad = !expandedPrioridad }
            ) {
                OutlinedTextField(
                    value = prioridad,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Prioridad *") },
                    leadingIcon = { Icon(Icons.Default.Flag, null) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expandedPrioridad) },
                    modifier = Modifier.fillMaxWidth().menuAnchor(),
                    colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
                )

                ExposedDropdownMenu(
                    expanded = expandedPrioridad,
                    onDismissRequest = { expandedPrioridad = false }
                ) {
                    Prioridad.entries.forEach { prio ->
                        DropdownMenuItem(
                            text = { Text(prio.name) },
                            onClick = {
                                prioridad = prio.name
                                expandedPrioridad = false
                            }
                        )
                    }
                }
            }

            // Notas
            OutlinedTextField(
                value = notas,
                onValueChange = { notas = it },
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
                        Text("Recordatorio de cita")
                    }
                    Switch(
                        checked = notificacionActiva,
                        onCheckedChange = { notificacionActiva = it }
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Botón guardar
            Button(
                onClick = {
                    scope.launch {
                        isLoading = true

                        // Validar campos
                        if (nombreDueno.isBlank() || telefono.isBlank() || email.isBlank() ||
                            direccion.isBlank() || nombreMascota.isBlank() || raza.isBlank() ||
                            edad.isBlank() || tipoServicio.isBlank() || motivo.isBlank() ||
                            fechaCita == null || horaCita.isBlank() || veterinarioSeleccionado == null ||
                            prioridad.isBlank()) {
                            snackbarHostState.showSnackbar("Por favor completa todos los campos obligatorios")
                            isLoading = false
                            return@launch
                        }

                        // 1. Crear usuario
                        val usuarioResult = repository.createUsuario(
                            Usuario(
                                nombre = nombreDueno,
                                telefono = telefono,
                                email = email,
                                direccion = direccion
                            )
                        )

                        if (usuarioResult.isFailure) {
                            snackbarHostState.showSnackbar("Error al crear usuario")
                            isLoading = false
                            return@launch
                        }

                        val usuarioCreado = usuarioResult.getOrNull()!!

                        // 2. Crear mascota
                        val mascotaResult = repository.createMascota(
                            Mascota(
                                nombre = nombreMascota,
                                raza = raza,
                                edad = edad.toInt(),
                                dueno = UsuarioRef(usuarioCreado.id!!)
                            )
                        )

                        if (mascotaResult.isFailure) {
                            snackbarHostState.showSnackbar("Error al crear mascota")
                            isLoading = false
                            return@launch
                        }

                        val mascotaCreada = mascotaResult.getOrNull()!!

                        // 3. Crear cita
                        val citaRequest = CitaVeterinariaRequest(
                            usuario = UsuarioRef(usuarioCreado.id!!),
                            mascota = MascotaRef(mascotaCreada.id!!),
                            veterinario = VeterinarioRef(veterinarioSeleccionado!!.id!!),
                            fechaCita = formatFechaParaBackend(fechaCita!!),
                            horaCita = "$horaCita:00",
                            tipoServicio = tipoServicio,
                            motivo = motivo,
                            prioridad = prioridad,
                            confirmada = false,
                            notificacionActiva = notificacionActiva,
                            notas = notas
                        )

                        val citaResult = repository.createCita(citaRequest)

                        if (citaResult.isSuccess) {
                            snackbarHostState.showSnackbar("Cita agendada exitosamente 🐶")
                            kotlinx.coroutines.delay(500)
                            onNavigateBack()
                        } else {
                            snackbarHostState.showSnackbar("Error al crear cita")
                        }

                        isLoading = false
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading
            ) {
                if (isLoading) {
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
            initialSelectedDateMillis = fechaCita ?: System.currentTimeMillis()
        )
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    fechaCita = datePickerState.selectedDateMillis
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
        val timePickerState = rememberTimePickerState(
            initialHour = 9,
            initialMinute = 0,
            is24Hour = true
        )
        AlertDialog(
            onDismissRequest = { showTimePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    horaCita = String.format("%02d:%02d", timePickerState.hour, timePickerState.minute)
                    showTimePicker = false
                }) { Text("Aceptar") }
            },
            dismissButton = {
                TextButton(onClick = { showTimePicker = false }) { Text("Cancelar") }
            },
            text = {
                TimePicker(state = timePickerState)
            }
        )
    }
}

fun formatFecha(millis: Long): String {
    val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return sdf.format(Date(millis))
}

fun formatFechaParaBackend(millis: Long): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return sdf.format(Date(millis))
}