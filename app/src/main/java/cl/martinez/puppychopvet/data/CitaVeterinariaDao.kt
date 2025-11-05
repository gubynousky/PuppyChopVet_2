package cl.martinez.puppychopvet.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/**
 * DAO para operaciones de citas veterinarias
 */
@Dao
interface CitaVeterinariaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCita(cita: CitaVeterinaria)

    @Update
    suspend fun updateCita(cita: CitaVeterinaria)

    @Delete
    suspend fun deleteCita(cita: CitaVeterinaria)

    /** Obtiene todas las citas ordenadas por fecha */
    @Query("SELECT * FROM citas_veterinarias ORDER BY fechaCita ASC, horaCita ASC")
    fun getAllCitas(): Flow<List<CitaVeterinaria>>

    /** Obtiene citas pendientes de confirmar */
    @Query("SELECT * FROM citas_veterinarias WHERE confirmada = 0 ORDER BY fechaCita ASC")
    fun getCitasPendientes(): Flow<List<CitaVeterinaria>>

    /** Obtiene citas confirmadas */
    @Query("SELECT * FROM citas_veterinarias WHERE confirmada = 1 ORDER BY fechaCita ASC")
    fun getCitasConfirmadas(): Flow<List<CitaVeterinaria>>

    /** Obtiene una cita por ID */
    @Query("SELECT * FROM citas_veterinarias WHERE id = :id")
    suspend fun getCitaById(id: Int): CitaVeterinaria?

    /** Obtiene citas por tipo de servicio */
    @Query("SELECT * FROM citas_veterinarias WHERE tipoServicio = :tipo ORDER BY fechaCita ASC")
    fun getCitasByTipo(tipo: String): Flow<List<CitaVeterinaria>>

    /** Obtiene citas por veterinario */
    @Query("SELECT * FROM citas_veterinarias WHERE veterinario = :veterinario ORDER BY fechaCita ASC")
    fun getCitasByVeterinario(veterinario: String): Flow<List<CitaVeterinaria>>

    /** Actualiza estado de confirmación */
    @Query("UPDATE citas_veterinarias SET confirmada = :confirmada WHERE id = :id")
    suspend fun updateConfirmada(id: Int, confirmada: Boolean)

    /** Elimina todas las citas confirmadas */
    @Query("DELETE FROM citas_veterinarias WHERE confirmada = 1")
    suspend fun deleteAllConfirmadas()

    /** Cuenta citas pendientes */
    @Query("SELECT COUNT(*) FROM citas_veterinarias WHERE confirmada = 0")
    suspend fun getCountPendientes(): Int

    /** Cuenta citas confirmadas */
    @Query("SELECT COUNT(*) FROM citas_veterinarias WHERE confirmada = 1")
    suspend fun getCountConfirmadas(): Int
}