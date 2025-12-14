package cl.martinez.puppychopvet.model

import org.junit.Assert.*
import org.junit.Test

class TipoServicioTest {

    @Test
    fun `fromString devuelve el tipo correcto`() {
        assertEquals(TipoServicio.CONSULTA, TipoServicio.fromString("CONSULTA"))
        assertEquals(TipoServicio.VACUNACION, TipoServicio.fromString("VACUNACION"))
        assertEquals(TipoServicio.CIRUGIA, TipoServicio.fromString("CIRUGIA"))
    }

    @Test
    fun `fromString devuelve CONSULTA para string inválido`() {
        assertEquals(TipoServicio.CONSULTA, TipoServicio.fromString("INVALIDO"))
        assertEquals(TipoServicio.CONSULTA, TipoServicio.fromString(""))
    }

    @Test
    fun `getAllDisplayNames devuelve todos los nombres`() {
        val names = TipoServicio.getAllDisplayNames()

        assertEquals(7, names.size)
        assertTrue(names.contains("Consulta General"))
        assertTrue(names.contains("Vacunación"))
        assertTrue(names.contains("Cirugía"))
    }

    @Test
    fun `displayName tiene el formato correcto`() {
        assertEquals("Consulta General", TipoServicio.CONSULTA.displayName)
        assertEquals("Vacunación", TipoServicio.VACUNACION.displayName)
        assertEquals("Emergencia", TipoServicio.EMERGENCIA.displayName)
    }
}