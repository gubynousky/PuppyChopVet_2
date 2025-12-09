package cl.martinez.puppychopvet.network

import cl.martinez.puppychopvet.network.models.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CitaRepository {

    private val api = RetrofitClient.apiService

    // ========== USUARIOS ==========
    suspend fun getAllUsuarios(): Result<List<Usuario>> = withContext(Dispatchers.IO) {
        try {
            val response = api.getAllUsuarios()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createUsuario(usuario: Usuario): Result<Usuario> = withContext(Dispatchers.IO) {
        try {
            val response = api.createUsuario(usuario)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // ========== MASCOTAS ==========
    suspend fun getAllMascotas(): Result<List<Mascota>> = withContext(Dispatchers.IO) {
        try {
            val response = api.getAllMascotas()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getMascotasByDueno(duenoId: Long): Result<List<Mascota>> = withContext(Dispatchers.IO) {
        try {
            val response = api.getMascotasByDueno(duenoId)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createMascota(mascota: Mascota): Result<Mascota> = withContext(Dispatchers.IO) {
        try {
            val response = api.createMascota(mascota)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // ========== VETERINARIOS ==========
    suspend fun getAllVeterinarios(): Result<List<Veterinario>> = withContext(Dispatchers.IO) {
        try {
            val response = api.getAllVeterinarios()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createVeterinario(veterinario: Veterinario): Result<Veterinario> = withContext(Dispatchers.IO) {
        try {
            val response = api.createVeterinario(veterinario)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // ========== CITAS ==========
    suspend fun getAllCitas(): Result<List<CitaVeterinariaResponse>> = withContext(Dispatchers.IO) {
        try {
            val response = api.getAllCitas()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getCitasPendientes(): Result<List<CitaVeterinariaResponse>> = withContext(Dispatchers.IO) {
        try {
            val response = api.getCitasPendientes()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getCitasConfirmadas(): Result<List<CitaVeterinariaResponse>> = withContext(Dispatchers.IO) {
        try {
            val response = api.getCitasConfirmadas()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createCita(cita: CitaVeterinariaRequest): Result<CitaVeterinariaResponse> = withContext(Dispatchers.IO) {
        try {
            val response = api.createCita(cita)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun confirmarCita(citaId: Long): Result<CitaVeterinariaResponse> = withContext(Dispatchers.IO) {
        try {
            val response = api.confirmarCita(citaId)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun actualizarCita(citaId: Long, cita: CitaVeterinariaRequest): Result<CitaVeterinariaResponse> = withContext(Dispatchers.IO) {
        try {
            val response = api.actualizarCita(citaId, cita)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteCita(citaId: Long): Result<Boolean> = withContext(Dispatchers.IO) {
        try {
            val response = api.deleteCita(citaId)
            if (response.isSuccessful) {
                Result.success(true)
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}