package cl.martinez.puppychopvet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.martinez.puppychopvet.data.CitaVeterinaria
import cl.martinez.puppychopvet.model.CitaVeterinariaRepository
import cl.martinez.puppychopvet.utils.ValidationUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class CitaUiState(
    val nombreDueno: String = "",
    val telefono: String = "",
    val email: String = "",
    val nombreMascota: String = "",
    val raza: String = "",
    val edad: String = "",
    val tipoServicio: String = "",
    val motivo: String = "",
    val fechaCita: Long? = null,
    val horaCita: String = "",
    val veterinario: String = "",
    val prioridad: String = "",
    val notificacionActiva: Boolean = true,
    val notas: String = "",

    // Errores
    val nombreDuenoError: String? = null,
    val telefonoError: String? = null,
    val emailError: String? = null,
    val nombreMascotaError: String? = null,
    val razaError: String? = null,
    val edadError: String? = null,
    val tipoServicioError: String? = null,
    val motivoError: String? = null,
    val fechaCitaError: String? = null,
    val horaError: String? = null,
    val veterinarioError: String? = null,
    val prioridadError: String? = null,

    val isLoading: Boolean = false,
    val guardadoExitoso: Boolean = false
)

class CitaVeterinariaViewModel(
    private val repository: CitaVeterinariaRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CitaUiState())
    val uiState: StateFlow<CitaUiState> = _uiState.asStateFlow()

    val allCitas = repository.allCitas
    val citasPendientes = repository.citasPendientes
    val citasConfirmadas = repository.citasConfirmadas

    fun onNombreDuenoChange(nombre: String) {
        _uiState.value = _uiState.value.copy(
            nombreDueno = nombre,
            nombreDuenoError = null
        )
    }

    fun onTelefonoChange(telefono: String) {
        _uiState.value = _uiState.value.copy(
            telefono = telefono,
            telefonoError = null
        )
    }

    fun onEmailChange(email: String) {
        _uiState.value = _uiState.value.copy(
            email = email,
            emailError = null
        )
    }

    fun onNombreMascotaChange(nombre: String) {
        _uiState.value = _uiState.value.copy(
            nombreMascota = nombre,
            nombreMascotaError = null
        )
    }

    fun onRazaChange(raza: String) {
        _uiState.value = _uiState.value.copy(
            raza = raza,
            razaError = null
        )
    }

    fun onEdadChange(edad: String) {
        _uiState.value = _uiState.value.copy(
            edad = edad,
            edadError = null
        )
    }

    fun onTipoServicioChange(tipo: String) {
        _uiState.value = _uiState.value.copy(
            tipoServicio = tipo,
            tipoServicioError = null
        )
    }

    fun onMotivoChange(motivo: String) {
        _uiState.value = _uiState.value.copy(
            motivo = motivo,
            motivoError = null
        )
    }

    fun onFechaCitaChange(fecha: Long?) {
        _uiState.value = _uiState.value.copy(
            fechaCita = fecha,
            fechaCitaError = null
        )
    }

    fun onHoraCitaChange(hora: String) {
        _uiState.value = _uiState.value.copy(
            horaCita = hora,
            horaError = null
        )
    }

    fun onVeterinarioChange(veterinario: String) {
        _uiState.value = _uiState.value.copy(
            veterinario = veterinario,
            veterinarioError = null
        )
    }

    fun onPrioridadChange(prioridad: String) {
        _uiState.value = _uiState.value.copy(
            prioridad = prioridad,
            prioridadError = null
        )
    }

    fun onNotasChange(notas: String) {
        _uiState.value = _uiState.value.copy(notas = notas)
    }

    fun toggleNotificacion() {
        _uiState.value = _uiState.value.copy(
            notificacionActiva = !_uiState.value.notificacionActiva
        )
    }

    private fun validateFields(): Boolean {
        val state = _uiState.value

        val nombreDuenoError = ValidationUtils.getNombreDuenoErrorMessage(state.nombreDueno)
        val telefonoError = ValidationUtils.getTelefonoErrorMessage(state.telefono)
        val emailError = ValidationUtils.getEmailErrorMessage(state.email)
        val nombreMascotaError = ValidationUtils.getNombreMascotaErrorMessage(state.nombreMascota)
        val razaError = ValidationUtils.getRazaErrorMessage(state.raza)
        val edadError = ValidationUtils.getEdadErrorMessage(state.edad)
        val tipoServicioError = ValidationUtils.getTipoServicioErrorMessage(state.tipoServicio)
        val motivoError = ValidationUtils.getMotivoErrorMessage(state.motivo)
        val fechaCitaError = ValidationUtils.getFechaCitaErrorMessage(state.fechaCita)
        val horaError = ValidationUtils.getHoraErrorMessage(state.horaCita)
        val veterinarioError = ValidationUtils.getVeterinarioErrorMessage(state.veterinario)
        val prioridadError = ValidationUtils.getPrioridadErrorMessage(state.prioridad)

        _uiState.value = _uiState.value.copy(
            nombreDuenoError = nombreDuenoError,
            telefonoError = telefonoError,
            emailError = emailError,
            nombreMascotaError = nombreMascotaError,
            razaError = razaError,
            edadError = edadError,
            tipoServicioError = tipoServicioError,
            motivoError = motivoError,
            fechaCitaError = fechaCitaError,
            horaError = horaError,
            veterinarioError = veterinarioError,
            prioridadError = prioridadError
        )

        return nombreDuenoError == null && telefonoError == null &&
                emailError == null && nombreMascotaError == null &&
                razaError == null && edadError == null &&
                tipoServicioError == null && motivoError == null &&
                fechaCitaError == null && horaError == null &&
                veterinarioError == null && prioridadError == null
    }

    fun onGuardarClick() {
        if (!validateFields()) return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                guardadoExitoso = false
            )

            val cita = CitaVeterinaria(
                nombreDueno = _uiState.value.nombreDueno,
                telefono = _uiState.value.telefono,
                email = _uiState.value.email,
                nombreMascota = _uiState.value.nombreMascota,
                raza = _uiState.value.raza,
                edad = _uiState.value.edad.toInt(),
                tipoServicio = _uiState.value.tipoServicio,
                motivo = _uiState.value.motivo,
                fechaCita = _uiState.value.fechaCita!!,
                horaCita = _uiState.value.horaCita,
                veterinario = _uiState.value.veterinario,
                prioridad = _uiState.value.prioridad,
                confirmada = false,
                notificacionActiva = _uiState.value.notificacionActiva,
                notas = _uiState.value.notas
            )

            repository.insertCita(cita)

            _uiState.value = _uiState.value.copy(
                isLoading = false,
                guardadoExitoso = true
            )
        }
    }

    fun toggleConfirmada(citaId: Int, confirmada: Boolean) {
        viewModelScope.launch {
            repository.updateConfirmada(citaId, confirmada)
        }
    }

    fun deleteCita(cita: CitaVeterinaria) {
        viewModelScope.launch {
            repository.deleteCita(cita)
        }
    }

    fun insertCita(cita: CitaVeterinaria) {
        viewModelScope.launch {
            repository.insertCita(cita)
        }
    }

    fun deleteAllConfirmadas() {
        viewModelScope.launch {
            repository.deleteAllConfirmadas()
        }
    }

    fun resetForm() {
        _uiState.value = CitaUiState()
    }

    suspend fun getCitaById(id: Int): CitaVeterinaria? {
        return repository.getCitaById(id)
    }
}