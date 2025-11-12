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

    val nombreDueno: String,

    val telefono: String,

    val email: String,

    val nombreMascota: String,

    val raza: String,

    val edad: Int,

    val tipoServicio: String,

    val motivo: String,

    val fechaCita: Long,

    val horaCita: String,

    val veterinario: String,

    val prioridad: String,

    val confirmada: Boolean = false,

    val notificacionActiva: Boolean = true,

    val fechaCreacion: Long = System.currentTimeMillis(),

    val notas: String = ""
)