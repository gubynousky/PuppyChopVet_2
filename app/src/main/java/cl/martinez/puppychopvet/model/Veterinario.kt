package cl.martinez.puppychopvet.model

/**
 * Veterinarios disponibles en PuppyChop
 */
enum class Veterinario(val displayName: String, val especialidad: String) {
    DR_MARTINEZ("Dr. Carlos Martínez", "Medicina General"),
    DRA_LOPEZ("Dra. Ana López", "Cirugía"),
    DR_GONZALEZ("Dr. Pedro González", "Pediatría Veterinaria"),
    DRA_SILVA("Dra. María Silva", "Dermatología"),
    DRA_SALAS("Dra. Estrella Salas", "Estética Canina"),
    DR_ROJAS("Dr. Juan Rojas", "Emergencias");

    companion object {
        fun fromString(value: String): Veterinario {
            return entries.find { it.name == value } ?: DR_MARTINEZ
        }

        fun getAllDisplayNames(): List<String> {
            return entries.map { it.displayName }
        }
    }
}