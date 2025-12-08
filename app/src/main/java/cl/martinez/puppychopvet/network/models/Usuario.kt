package cl.martinez.puppychopvet.network.models

import com.google.gson.annotations.SerializedName

data class Usuario(
    @SerializedName("id")
    val id: Long? = null,

    @SerializedName("nombre")
    val nombre: String,

    @SerializedName("telefono")
    val telefono: String,

    @SerializedName("email")
    val email: String,

    @SerializedName("direccion")
    val direccion: String
)