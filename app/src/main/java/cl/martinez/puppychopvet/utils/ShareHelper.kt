package cl.martinez.puppychopvet.utils

import android.content.Context
import android.content.Intent
import cl.martinez.puppychopvet.data.CitaVeterinaria
import cl.martinez.puppychopvet.model.Prioridad
import cl.martinez.puppychopvet.model.TipoServicio
import cl.martinez.puppychopvet.model.Veterinario
import java.text.SimpleDateFormat
import java.util.*

/**
 * Helper para compartir citas veterinarias de PuppyChop
 */
object ShareHelper {

    /**
     * Comparte una cita veterinaria usando el Intent de Android
     */
    fun compartirCita(context: Context, cita: CitaVeterinaria) {
        val tipoServicio = TipoServicio.fromString(cita.tipoServicio)
        val prioridad = Prioridad.fromString(cita.prioridad)
        val veterinario = Veterinario.fromString(cita.veterinario)

        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val fechaFormateada = dateFormat.format(Date(cita.fechaCita))

        val textoCompartir = buildString {
            appendLine("🐶 CITA VETERINARIA PUPPYCHOP")
            appendLine("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
            appendLine()
            appendLine("👤 DATOS DEL DUEÑO")
            appendLine("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
            appendLine("Nombre: ${cita.nombreDueno}")
            appendLine("Teléfono: ${cita.telefono}")
            appendLine("Email: ${cita.email}")
            appendLine()
            appendLine("🐕 DATOS DE LA MASCOTA")
            appendLine("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
            appendLine("Nombre: ${cita.nombreMascota}")
            appendLine("Raza: ${cita.raza}")
            appendLine("Edad: ${cita.edad} años")
            appendLine()
            appendLine("📋 INFORMACIÓN DE LA CITA")
            appendLine("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
            appendLine("Servicio: ${tipoServicio.displayName}")
            appendLine("Fecha: $fechaFormateada")
            appendLine("Hora: ${cita.horaCita}")
            appendLine("Veterinario: ${veterinario.displayName}")
            appendLine("Prioridad: ${prioridad.displayName}")
            appendLine()
            appendLine("📝 MOTIVO DE LA CONSULTA")
            appendLine("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
            appendLine(cita.motivo)
            appendLine()
            if (cita.notas.isNotEmpty()) {
                appendLine("📌 NOTAS ADICIONALES")
                appendLine("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
                appendLine(cita.notas)
                appendLine()
            }
            if (cita.notificacionActiva) {
                appendLine("🔔 Recordatorio activado")
            } else {
                appendLine("🔕 Recordatorio desactivado")
            }
            appendLine()
            if (cita.confirmada) {
                appendLine("✅ Cita Confirmada")
            } else {
                appendLine("⏳ Cita Pendiente de Confirmar")
            }
            appendLine()
            appendLine("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
            appendLine("🐾 PuppyChop - Cuidamos a tu mejor amigo")
        }

        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, textoCompartir)
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, "Compartir cita")
        context.startActivity(shareIntent)
    }

    /**
     * Comparte múltiples citas a la vez
     */
    fun compartirListaCitas(context: Context, citas: List<CitaVeterinaria>) {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

        val textoCompartir = buildString {
            appendLine("🐶 LISTA DE CITAS PUPPYCHOP")
            appendLine("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
            appendLine()

            citas.forEachIndexed { index, cita ->
                val tipoServicio = TipoServicio.fromString(cita.tipoServicio)
                val fechaFormateada = dateFormat.format(Date(cita.fechaCita))

                appendLine("${index + 1}. 🐕 ${cita.nombreMascota}")
                appendLine("   Dueño: ${cita.nombreDueno}")
                appendLine("   Servicio: ${tipoServicio.displayName}")
                appendLine("   Fecha: $fechaFormateada • ${cita.horaCita}")
                if (cita.confirmada) {
                    appendLine("   ✅ Confirmada")
                } else {
                    appendLine("   ⏳ Pendiente")
                }
                appendLine()
            }

            appendLine("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
            appendLine("Total: ${citas.size} citas")
            appendLine("🐾 PuppyChop - Cuidamos a tu mejor amigo")
        }

        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, textoCompartir)
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, "Compartir citas")
        context.startActivity(shareIntent)
    }
}