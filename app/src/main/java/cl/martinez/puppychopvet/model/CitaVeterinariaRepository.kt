package cl.martinez.puppychopvet.model

import cl.martinez.puppychopvet.data.CitaVeterinaria
import cl.martinez.puppychopvet.data.CitaVeterinariaDao
import kotlinx.coroutines.flow.Flow

/**
 * Repository para gestionar citas veterinarias de PuppyChop
 */
class CitaVeterinariaRepository(
    private val citaVeterinariaDao: CitaVeterinariaDao
) {

    val allCitas: Flow<List<CitaVeterinaria>> = citaVeterinariaDao.getAllCitas()
    val citasPendientes: Flow<List<CitaVeterinaria>> = citaVeterinariaDao.getCitasPendientes()
    val citasConfirmadas: Flow<List<CitaVeterinaria>> = citaVeterinariaDao.getCitasConfirmadas()

    suspend fun insertCita(cita: CitaVeterinaria) {
        citaVeterinariaDao.insertCita(cita)
    }

    suspend fun updateCita(cita: CitaVeterinaria) {
        citaVeterinariaDao.updateCita(cita)
    }

    suspend fun deleteCita(cita: CitaVeterinaria) {
        citaVeterinariaDao.deleteCita(cita)
    }

    suspend fun getCitaById(id: Int): CitaVeterinaria? {
        return citaVeterinariaDao.getCitaById(id)
    }

    fun getCitasByTipo(tipo: String): Flow<List<CitaVeterinaria>> {
        return citaVeterinariaDao.getCitasByTipo(tipo)
    }

    fun getCitasByVeterinario(veterinario: String): Flow<List<CitaVeterinaria>> {
        return citaVeterinariaDao.getCitasByVeterinario(veterinario)
    }

    suspend fun updateConfirmada(id: Int, confirmada: Boolean) {
        citaVeterinariaDao.updateConfirmada(id, confirmada)
    }

    suspend fun deleteAllConfirmadas() {
        citaVeterinariaDao.deleteAllConfirmadas()
    }

    suspend fun getCountPendientes(): Int {
        return citaVeterinariaDao.getCountPendientes()
    }

    suspend fun getCountConfirmadas(): Int {
        return citaVeterinariaDao.getCountConfirmadas()
    }
}