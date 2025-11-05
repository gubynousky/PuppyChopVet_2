package cl.martinez.puppychopvet.utils

/**
 * Utilidades de validación para formularios de PuppyChop
 */
object ValidationUtils {

    fun isValidNombre(nombre: String): Boolean {
        if (nombre.isBlank()) return false
        if (nombre.length < 2 || nombre.length > 50) return false
        return nombre.matches(Regex("^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$"))
    }

    fun isValidTelefono(telefono: String): Boolean {
        if (telefono.isBlank()) return false
        return telefono.matches(Regex("^\\+?[0-9]{8,15}$"))
    }

    fun isValidEmail(email: String): Boolean {
        if (email.isBlank()) return false
        return email.matches(Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}$"))
    }

    fun isValidRaza(raza: String): Boolean {
        return raza.isNotBlank() && raza.length >= 2
    }

    fun isValidEdad(edad: String): Boolean {
        return try {
            val edadInt = edad.toInt()
            edadInt in 0..30
        } catch (e: Exception) {
            false
        }
    }

    fun isValidMotivo(motivo: String): Boolean {
        if (motivo.isBlank()) return false
        if (motivo.length < 10 || motivo.length > 300) return false
        return true
    }

    fun isValidHora(hora: String): Boolean {
        if (hora.isBlank()) return false
        val horaRegex = "^([01][0-9]|2[0-3]):[0-5][0-9]$".toRegex()
        return horaRegex.matches(hora)
    }

    fun isValidFechaCita(fechaCitaMillis: Long): Boolean {
        return fechaCitaMillis > System.currentTimeMillis()
    }

    fun isValidTipoServicio(tipo: String): Boolean {
        return tipo.isNotBlank()
    }

    fun isValidVeterinario(veterinario: String): Boolean {
        return veterinario.isNotBlank()
    }

    fun isValidPrioridad(prioridad: String): Boolean {
        return prioridad.isNotBlank()
    }

    // Mensajes de error
    fun getNombreDuenoErrorMessage(nombre: String): String? {
        return when {
            nombre.isBlank() -> "El nombre es requerido"
            nombre.length < 2 -> "El nombre debe tener al menos 2 caracteres"
            nombre.length > 50 -> "El nombre no puede exceder 50 caracteres"
            !nombre.matches(Regex("^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$")) -> "Solo se permiten letras"
            else -> null
        }
    }

    fun getTelefonoErrorMessage(telefono: String): String? {
        return when {
            telefono.isBlank() -> "El teléfono es requerido"
            !telefono.matches(Regex("^\\+?[0-9]{8,15}$")) -> "Formato de teléfono inválido"
            else -> null
        }
    }

    fun getEmailErrorMessage(email: String): String? {
        return when {
            email.isBlank() -> "El email es requerido"
            !email.matches(Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}$")) ->
                "Formato de email inválido"
            else -> null
        }
    }

    fun getNombreMascotaErrorMessage(nombre: String): String? {
        return when {
            nombre.isBlank() -> "El nombre de la mascota es requerido"
            nombre.length < 2 -> "El nombre debe tener al menos 2 caracteres"
            nombre.length > 50 -> "El nombre no puede exceder 50 caracteres"
            else -> null
        }
    }

    fun getRazaErrorMessage(raza: String): String? {
        return when {
            raza.isBlank() -> "La raza es requerida"
            raza.length < 2 -> "La raza debe tener al menos 2 caracteres"
            else -> null
        }
    }

    fun getEdadErrorMessage(edad: String): String? {
        return try {
            val edadInt = edad.toInt()
            when {
                edadInt < 0 -> "La edad no puede ser negativa"
                edadInt > 30 -> "La edad no puede ser mayor a 30 años"
                else -> null
            }
        } catch (e: Exception) {
            "La edad debe ser un número válido"
        }
    }

    fun getMotivoErrorMessage(motivo: String): String? {
        return when {
            motivo.isBlank() -> "El motivo es requerido"
            motivo.length < 10 -> "El motivo debe tener al menos 10 caracteres"
            motivo.length > 300 -> "El motivo no puede exceder 300 caracteres"
            else -> null
        }
    }

    fun getHoraErrorMessage(hora: String): String? {
        return when {
            hora.isBlank() -> "La hora es requerida"
            !isValidHora(hora) -> "Formato de hora inválido (HH:mm)"
            else -> null
        }
    }

    fun getFechaCitaErrorMessage(fechaCitaMillis: Long?): String? {
        return when {
            fechaCitaMillis == null -> "La fecha es requerida"
            !isValidFechaCita(fechaCitaMillis) -> "La fecha debe ser futura"
            else -> null
        }
    }

    fun getTipoServicioErrorMessage(tipo: String): String? {
        return if (tipo.isBlank()) "Debe seleccionar un tipo de servicio" else null
    }

    fun getVeterinarioErrorMessage(veterinario: String): String? {
        return if (veterinario.isBlank()) "Debe seleccionar un veterinario" else null
    }

    fun getPrioridadErrorMessage(prioridad: String): String? {
        return if (prioridad.isBlank()) "Debe seleccionar una prioridad" else null
    }
}