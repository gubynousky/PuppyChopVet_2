package cl.martinez.puppychopvet.network.models

import com.google.gson.annotations.SerializedName

data class CitaVeterinariaRequest(
    @SerializedName("usuario")
    val usuario: UsuarioRef,

    @SerializedName("mascota")
    val mascota: MascotaRef,

    @SerializedName("veterinario")
    val veterinario: VeterinarioRef,

    @SerializedName("fechaCita")
    val fechaCita: String, // formato: "2025-12-15"

    @SerializedName("horaCita")
    val horaCita: String, // formato: "10:00:00"

    @SerializedName("tipoServicio")
    val tipoServicio: String,

    @SerializedName("motivo")
    val motivo: String,

    @SerializedName("prioridad")
    val prioridad: String,

    @SerializedName("confirmada")
    val confirmada: Boolean = false,

    @SerializedName("notificacionActiva")
    val notificacionActiva: Boolean = true,

    @SerializedName("notas")
    val notas: String = ""
)

data class CitaVeterinariaResponse(
    @SerializedName("id")
    val id: Long,

    @SerializedName("usuario")
    val usuario: Usuario,

    @SerializedName("mascota")
    val mascota: Mascota,

    @SerializedName("veterinario")
    val veterinario: Veterinario,

    @SerializedName("fechaCita")
    val fechaCita: String,

    @SerializedName("horaCita")
    val horaCita: String,

    @SerializedName("tipoServicio")
    val tipoServicio: String,

    @SerializedName("motivo")
    val motivo: String,

    @SerializedName("prioridad")
    val prioridad: String,

    @SerializedName("confirmada")
    val confirmada: Boolean,

    @SerializedName("notificacionActiva")
    val notificacionActiva: Boolean,

    @SerializedName("notas")
    val notas: String
)

data class MascotaRef(
    @SerializedName("id")
    val id: Long
)

data class VeterinarioRef(
    @SerializedName("id")
    val id: Long
)