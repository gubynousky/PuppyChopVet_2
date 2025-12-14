package cl.martinez.puppychopvet.utils

import org.junit.Assert.*
import org.junit.Test

class ValidationUtilsTest {

    // ========== TESTS DE NOMBRE ==========
    @Test
    fun `isValidNombre acepta nombre válido`() {
        assertTrue(ValidationUtils.isValidNombre("María González"))
        assertTrue(ValidationUtils.isValidNombre("José"))
        assertTrue(ValidationUtils.isValidNombre("Ana María"))
    }

    @Test
    fun `isValidNombre rechaza nombre vacío`() {
        assertFalse(ValidationUtils.isValidNombre(""))
        assertFalse(ValidationUtils.isValidNombre("   "))
    }

    @Test
    fun `isValidNombre rechaza nombre muy corto`() {
        assertFalse(ValidationUtils.isValidNombre("A"))
    }

    @Test
    fun `isValidNombre rechaza nombre con números`() {
        assertFalse(ValidationUtils.isValidNombre("María123"))
    }

    @Test
    fun `isValidNombre rechaza nombre con caracteres especiales`() {
        assertFalse(ValidationUtils.isValidNombre("María@González"))
    }

    // ========== TESTS DE TELÉFONO ==========
    @Test
    fun `isValidTelefono acepta teléfono válido`() {
        assertTrue(ValidationUtils.isValidTelefono("+56987654321"))
        assertTrue(ValidationUtils.isValidTelefono("987654321"))
    }

    @Test
    fun `isValidTelefono rechaza teléfono vacío`() {
        assertFalse(ValidationUtils.isValidTelefono(""))
    }

    @Test
    fun `isValidTelefono rechaza teléfono con letras`() {
        assertFalse(ValidationUtils.isValidTelefono("+5698765abc1"))
    }

    @Test
    fun `isValidTelefono rechaza teléfono muy corto`() {
        assertFalse(ValidationUtils.isValidTelefono("123"))
    }

    // ========== TESTS DE EMAIL ==========
    @Test
    fun `isValidEmail acepta email válido`() {
        assertTrue(ValidationUtils.isValidEmail("test@example.com"))
        assertTrue(ValidationUtils.isValidEmail("user.name@domain.co"))
    }

    @Test
    fun `isValidEmail rechaza email sin arroba`() {
        assertFalse(ValidationUtils.isValidEmail("testexample.com"))
    }

    @Test
    fun `isValidEmail rechaza email sin dominio`() {
        assertFalse(ValidationUtils.isValidEmail("test@"))
    }

    @Test
    fun `isValidEmail rechaza email vacío`() {
        assertFalse(ValidationUtils.isValidEmail(""))
    }

    // ========== TESTS DE EDAD ==========
    @Test
    fun `isValidEdad acepta edad válida`() {
        assertTrue(ValidationUtils.isValidEdad("5"))
        assertTrue(ValidationUtils.isValidEdad("0"))
        assertTrue(ValidationUtils.isValidEdad("30"))
    }

    @Test
    fun `isValidEdad rechaza edad negativa`() {
        assertFalse(ValidationUtils.isValidEdad("-1"))
    }

    @Test
    fun `isValidEdad rechaza edad mayor a 30`() {
        assertFalse(ValidationUtils.isValidEdad("31"))
    }

    @Test
    fun `isValidEdad rechaza edad no numérica`() {
        assertFalse(ValidationUtils.isValidEdad("abc"))
    }

    // ========== TESTS DE MOTIVO ==========
    @Test
    fun `isValidMotivo acepta motivo válido`() {
        assertTrue(ValidationUtils.isValidMotivo("Control de rutina para verificar estado"))
    }

    @Test
    fun `isValidMotivo rechaza motivo muy corto`() {
        assertFalse(ValidationUtils.isValidMotivo("Control"))
    }

    @Test
    fun `isValidMotivo rechaza motivo vacío`() {
        assertFalse(ValidationUtils.isValidMotivo(""))
    }

    // ========== TESTS DE MENSAJES DE ERROR ==========
    @Test
    fun `getNombreDuenoErrorMessage devuelve mensaje correcto`() {
        assertEquals("El nombre es requerido", ValidationUtils.getNombreDuenoErrorMessage(""))
        assertEquals("El nombre debe tener al menos 2 caracteres", ValidationUtils.getNombreDuenoErrorMessage("A"))
        assertEquals("Solo se permiten letras", ValidationUtils.getNombreDuenoErrorMessage("María123"))
        assertNull(ValidationUtils.getNombreDuenoErrorMessage("María González"))
    }

    @Test
    fun `getTelefonoErrorMessage devuelve mensaje correcto`() {
        assertEquals("El teléfono es requerido", ValidationUtils.getTelefonoErrorMessage(""))
        assertEquals("Formato de teléfono inválido", ValidationUtils.getTelefonoErrorMessage("123"))
        assertNull(ValidationUtils.getTelefonoErrorMessage("+56987654321"))
    }

    @Test
    fun `getEmailErrorMessage devuelve mensaje correcto`() {
        assertEquals("El email es requerido", ValidationUtils.getEmailErrorMessage(""))
        assertEquals("Formato de email inválido", ValidationUtils.getEmailErrorMessage("invalido"))
        assertNull(ValidationUtils.getEmailErrorMessage("test@example.com"))
    }
}