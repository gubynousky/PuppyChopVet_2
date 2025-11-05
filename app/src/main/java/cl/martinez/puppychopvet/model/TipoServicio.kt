package cl.martinez.puppychopvet.model

/**
 * Tipos de servicio veterinario disponibles en PuppyChop
 */
enum class TipoServicio(val displayName: String) {
    CONSULTA("Consulta General"),
    VACUNACION("Vacunación"),
    CIRUGIA("Cirugía"),
    CONTROL("Control Médico"),
    EMERGENCIA("Emergencia"),
    DESPARASITACION("Desparasitación"),
    ESTETICA("Estética y Peluquería");

    companion object {
        fun fromString(value: String): TipoServicio {
            return entries.find { it.name == value } ?: CONSULTA
        }

        fun getAllDisplayNames(): List<String> {
            return entries.map { it.displayName }
        }
    }
}