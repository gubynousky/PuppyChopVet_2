package cl.martinez.puppychopvet.network

import cl.martinez.puppychopvet.network.models.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    // ========== USUARIOS ==========
    @GET("usuarios")
    suspend fun getAllUsuarios(): Response<List<Usuario>>

    @GET("usuarios/{id}")
    suspend fun getUsuarioById(@Path("id") id: Long): Response<Usuario>

    @POST("usuarios")
    suspend fun createUsuario(@Body usuario: Usuario): Response<Usuario>

    // ========== MASCOTAS ==========
    @GET("mascotas")
    suspend fun getAllMascotas(): Response<List<Mascota>>

    @GET("mascotas/{id}")
    suspend fun getMascotaById(@Path("id") id: Long): Response<Mascota>

    @GET("mascotas/dueno/{duenoId}")
    suspend fun getMascotasByDueno(@Path("duenoId") duenoId: Long): Response<List<Mascota>>

    @POST("mascotas")
    suspend fun createMascota(@Body mascota: Mascota): Response<Mascota>

    // ========== VETERINARIOS ==========
    @GET("veterinarios")
    suspend fun getAllVeterinarios(): Response<List<Veterinario>>

    @GET("veterinarios/{id}")
    suspend fun getVeterinarioById(@Path("id") id: Long): Response<Veterinario>

    @POST("veterinarios")
    suspend fun createVeterinario(@Body veterinario: Veterinario): Response<Veterinario>

    // ========== CITAS ==========
    @GET("citas")
    suspend fun getAllCitas(): Response<List<CitaVeterinariaResponse>>

    @GET("citas/{id}")
    suspend fun getCitaById(@Path("id") id: Long): Response<CitaVeterinariaResponse>

    @POST("citas")
    suspend fun createCita(@Body cita: CitaVeterinariaRequest): Response<CitaVeterinariaResponse>

    @PUT("citas/{id}")
    suspend fun updateCita(
        @Path("id") id: Long,
        @Body cita: CitaVeterinariaRequest
    ): Response<CitaVeterinariaResponse>

    @DELETE("citas/{id}")
    suspend fun deleteCita(@Path("id") id: Long): Response<Map<String, String>>

    @GET("citas/pendientes")
    suspend fun getCitasPendientes(): Response<List<CitaVeterinariaResponse>>

    @GET("citas/confirmadas")
    suspend fun getCitasConfirmadas(): Response<List<CitaVeterinariaResponse>>

    @PATCH("citas/{id}/confirmar")
    suspend fun confirmarCita(@Path("id") id: Long): Response<CitaVeterinariaResponse>

    @PUT("citas/{id}")
    suspend fun actualizarCita(
        @Path("id") id: Long,
        @Body cita: CitaVeterinariaRequest
    ): Response<CitaVeterinariaResponse>

    @GET("citas/usuario/{usuarioId}")
    suspend fun getCitasByUsuario(@Path("usuarioId") usuarioId: Long): Response<List<CitaVeterinariaResponse>>

    @GET("citas/mascota/{mascotaId}")
    suspend fun getCitasByMascota(@Path("mascotaId") mascotaId: Long): Response<List<CitaVeterinariaResponse>>

    @GET("citas/veterinario/{veterinarioId}")
    suspend fun getCitasByVeterinario(@Path("veterinarioId") veterinarioId: Long): Response<List<CitaVeterinariaResponse>>

    @GET("citas/estadisticas")
    suspend fun getEstadisticas(): Response<Map<String, Long>>
}