package cl.martinez.puppychopvet.model

import org.junit.Assert.*
import org.junit.Test

class PrioridadTest {

    @Test
    fun `fromString devuelve la prioridad correcta`() {
        assertEquals(Prioridad.ALTA, Prioridad.fromString("ALTA"))
        assertEquals(Prioridad.MEDIA, Prioridad.fromString("MEDIA"))
        assertEquals(Prioridad.BAJA, Prioridad.fromString("BAJA"))
    }

    @Test
    fun `fromString devuelve MEDIA para string inválido`() {
        assertEquals(Prioridad.MEDIA, Prioridad.fromString("INVALIDO"))
        assertEquals(Prioridad.MEDIA, Prioridad.fromString(""))
    }

    @Test
    fun `getAllDisplayNames devuelve todos los nombres`() {
        val names = Prioridad.getAllDisplayNames()

        assertEquals(3, names.size)
        assertTrue(names.contains("Alta"))
        assertTrue(names.contains("Media"))
        assertTrue(names.contains("Baja"))
    }

    @Test
    fun `colorValue tiene valores correctos`() {
        assertEquals(0xFFFF6B35, Prioridad.ALTA.colorValue)
        assertEquals(0xFFF7931E, Prioridad.MEDIA.colorValue)
        assertEquals(0xFF6B8E23, Prioridad.BAJA.colorValue)
    }

    @Test
    fun `displayName tiene el formato correcto`() {
        assertEquals("Alta", Prioridad.ALTA.displayName)
        assertEquals("Media", Prioridad.MEDIA.displayName)
        assertEquals("Baja", Prioridad.BAJA.displayName)
    }
}