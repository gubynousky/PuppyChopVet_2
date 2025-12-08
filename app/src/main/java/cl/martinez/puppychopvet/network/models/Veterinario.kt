package cl.martinez.puppychopvet.network.models

import com.google.gson.annotations.SerializedName

data class Veterinario(
    @SerializedName("id")
    val id: Long? = null,

    @SerializedName("nombre")
    val nombre: String,

    @SerializedName("especialidad")
    val especialidad: String,

    @SerializedName("telefono")
    val telefono: String,

    @SerializedName("email")
    val email: String
)