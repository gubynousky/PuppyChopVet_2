package cl.martinez.puppychopvet.view

import org.junit.Assert.*
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.*

class FormScreenHelpersTest {

    @Test
    fun `formatFecha formatea correctamente`() {
        val calendar = Calendar.getInstance()
        calendar.set(2025, Calendar.DECEMBER, 15, 0, 0, 0)
        val millis = calendar.timeInMillis

        val formatted = formatFecha(millis)

        assertEquals("15/12/2025", formatted)
    }

    @Test
    fun `formatFechaParaBackend formatea correctamente`() {
        val calendar = Calendar.getInstance()
        calendar.set(2025, Calendar.DECEMBER, 15, 0, 0, 0)
        val millis = calendar.timeInMillis

        val formatted = formatFechaParaBackend(millis)

        assertEquals("2025-12-15", formatted)
    }

    @Test
    fun `formatFecha maneja diferentes fechas`() {
        val calendar = Calendar.getInstance()

        // 1 de enero
        calendar.set(2025, Calendar.JANUARY, 1, 0, 0, 0)
        assertEquals("01/01/2025", formatFecha(calendar.timeInMillis))

        // 31 de diciembre
        calendar.set(2025, Calendar.DECEMBER, 31, 0, 0, 0)
        assertEquals("31/12/2025", formatFecha(calendar.timeInMillis))
    }
}