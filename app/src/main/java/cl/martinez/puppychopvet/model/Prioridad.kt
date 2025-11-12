package cl.martinez.puppychopvet.model

/**
 * Niveles de prioridad para citas veterinarias
 */
enum class Prioridad(val displayName: String, val colorValue: Long) {
    ALTA("Alta", 0xFFFF6B35),      // Naranja fuerte - Urgente
    MEDIA("Media", 0xFFF7931E),    // Naranja medio - Atención normal
    BAJA("Baja", 0xFF6B8E23);      // Verde oliva - No urgente

    companion object {
        fun fromString(value: String): Prioridad {
            return entries.find { it.name == value } ?: MEDIA
        }

        fun getAllDisplayNames(): List<String> {
            return entries.map { it.displayName }
        }
    }
}