package cl.martinez.puppychopvet.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entidad que representa una cita veterinaria en PuppyChop
 */
@Entity(tableName = "citas_veterinarias")
data class CitaVeterinaria(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    /** Nombre del dueño */
    val nombreDueno: String,

    /** Teléfono de contacto */
    val telefono: String,

    /** Email del dueño */
    val email: String,

    /** Nombre de la mascota */
    val nombreMascota: String,

    /** Raza del perrito */
    val raza: String,

    /** Edad de la mascota en años */
    val edad: Int,

    /** Tipo de servicio (CONSULTA, VACUNACION, CIRUGIA, CONTROL, EMERGENCIA) */
    val tipoServicio: String,

    /** Motivo o descripción de la consulta */
    val motivo: String,

    /** Fecha de la cita en milisegundos */
    val fechaCita: Long,

    /** Hora de la cita en formato HH:mm */
    val horaCita: String,

    /** Veterinario asignado */
    val veterinario: String,

    /** Prioridad (ALTA, MEDIA, BAJA) */
    val prioridad: String,

    /** Estado de la cita (true = confirmada, false = pendiente) */
    val confirmada: Boolean = false,

    /** Notificación activa */
    val notificacionActiva: Boolean = true,

    /** Fecha de creación */
    val fechaCreacion: Long = System.currentTimeMillis(),

    /** Notas adicionales */
    val notas: String = ""
)