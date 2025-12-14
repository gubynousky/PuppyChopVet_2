package cl.martinez.puppychopvet.network

import cl.martinez.puppychopvet.network.models.*
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Test

class CitaRepositoryTest {

    // Tests que NO requieren conexión al backend

    @Test
    fun `UsuarioRef se crea correctamente`() {
        val usuarioRef = UsuarioRef(1L)
        assertEquals(1L, usuarioRef.id)
    }

    @Test
    fun `MascotaRef se crea correctamente`() {
        val mascotaRef = MascotaRef(2L)
        assertEquals(2L, mascotaRef.id)
    }

    @Test
    fun `VeterinarioRef se crea correctamente`() {
        val veterinarioRef = VeterinarioRef(3L)
        assertEquals(3L, veterinarioRef.id)
    }

    @Test
    fun `CitaVeterinariaRequest se crea con valores correctos`() {
        val request = CitaVeterinariaRequest(
            usuario = UsuarioRef(1L),
            mascota = MascotaRef(2L),
            veterinario = VeterinarioRef(3L),
            fechaCita = "2025-12-15",
            horaCita = "10:00:00",
            tipoServicio = "CONSULTA",
            motivo = "Control rutinario",
            prioridad = "MEDIA",
            confirmada = false,
            notificacionActiva = true,
            notas = "Traer cartilla"
        )

        assertEquals(1L, request.usuario.id)
        assertEquals(2L, request.mascota.id)
        assertEquals(3L, request.veterinario.id)
        assertEquals("2025-12-15", request.fechaCita)
        assertEquals("10:00:00", request.horaCita)
        assertEquals("CONSULTA", request.tipoServicio)
        assertEquals("Control rutinario", request.motivo)
        assertEquals("MEDIA", request.prioridad)
        assertFalse(request.confirmada)
        assertTrue(request.notificacionActiva)
        assertEquals("Traer cartilla", request.notas)
    }

    @Test
    fun `Usuario se crea correctamente`() {
        val usuario = Usuario(
            id = 1L,
            nombre = "María González",
            telefono = "+56987654321",
            email = "maria@email.com",
            direccion = "Av. Providencia 123"
        )

        assertEquals(1L, usuario.id)
        assertEquals("María González", usuario.nombre)
        assertEquals("+56987654321", usuario.telefono)
        assertEquals("maria@email.com", usuario.email)
        assertEquals("Av. Providencia 123", usuario.direccion)
    }

    @Test
    fun `Mascota se crea correctamente`() {
        val mascota = Mascota(
            id = 1L,
            nombre = "Max",
            raza = "Golden Retriever",
            edad = 5,
            dueno = UsuarioRef(1L)
        )

        assertEquals(1L, mascota.id)
        assertEquals("Max", mascota.nombre)
        assertEquals("Golden Retriever", mascota.raza)
        assertEquals(5, mascota.edad)
        assertEquals(1L, mascota.dueno.id)
    }

    @Test
    fun `Veterinario se crea correctamente`() {
        val vet = Veterinario(
            id = 1L,
            nombre = "Dr. Carlos Martínez",
            especialidad = "Medicina General",
            telefono = "+56912345678",
            email = "carlos@vet.cl"
        )

        assertEquals(1L, vet.id)
        assertEquals("Dr. Carlos Martínez", vet.nombre)
        assertEquals("Medicina General", vet.especialidad)
        assertEquals("+56912345678", vet.telefono)
        assertEquals("carlos@vet.cl", vet.email)
    }

    @Test
    fun `CitaVeterinariaResponse se crea correctamente`() {
        val usuario = Usuario(1L, "María", "+56987654321", "maria@email.com", "Av. Providencia 123")
        val mascota = Mascota(1L, "Max", "Golden", 5, UsuarioRef(1L))
        val veterinario = Veterinario(1L, "Dr. Carlos", "General", "+56912345678", "carlos@vet.cl")

        val cita = CitaVeterinariaResponse(
            id = 1L,
            usuario = usuario,
            mascota = mascota,
            veterinario = veterinario,
            fechaCita = "2025-12-15",
            horaCita = "10:00:00",
            tipoServicio = "CONSULTA",
            motivo = "Control",
            prioridad = "MEDIA",
            confirmada = false,
            notificacionActiva = true,
            notas = ""
        )

        assertEquals(1L, cita.id)
        assertEquals("María", cita.usuario.nombre)
        assertEquals("Max", cita.mascota.nombre)
        assertEquals("Dr. Carlos", cita.veterinario.nombre)
        assertEquals("2025-12-15", cita.fechaCita)
        assertEquals("10:00:00", cita.horaCita)
        assertEquals("CONSULTA", cita.tipoServicio)
        assertEquals("Control", cita.motivo)
        assertEquals("MEDIA", cita.prioridad)
        assertFalse(cita.confirmada)
        assertTrue(cita.notificacionActiva)
    }

    @Test
    fun `CitaVeterinariaRequest con valores por defecto`() {
        val request = CitaVeterinariaRequest(
            usuario = UsuarioRef(1L),
            mascota = MascotaRef(1L),
            veterinario = VeterinarioRef(1L),
            fechaCita = "2025-12-15",
            horaCita = "10:00:00",
            tipoServicio = "CONSULTA",
            motivo = "Control",
            prioridad = "MEDIA"
        )

        assertFalse(request.confirmada)
        assertTrue(request.notificacionActiva)
        assertEquals("", request.notas)
    }

    @Test
    fun `Multiples usuarios tienen IDs diferentes`() {
        val usuario1 = Usuario(1L, "María", "+56987654321", "maria@email.com", "Av. 1")
        val usuario2 = Usuario(2L, "Pedro", "+56987654322", "pedro@email.com", "Av. 2")

        assertNotEquals(usuario1.id, usuario2.id)
        assertNotEquals(usuario1.nombre, usuario2.nombre)
    }
}