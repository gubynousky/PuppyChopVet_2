package cl.martinez.puppychopvet.network.models

import com.google.gson.annotations.SerializedName

data class Mascota(
    @SerializedName("id")
    val id: Long? = null,

    @SerializedName("nombre")
    val nombre: String,

    @SerializedName("raza")
    val raza: String,

    @SerializedName("edad")
    val edad: Int,

    @SerializedName("dueno")
    val dueno: UsuarioRef
)

data class UsuarioRef(
    @SerializedName("id")
    val id: Long
)