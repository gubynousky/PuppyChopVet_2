package cl.martinez.puppychopvet.utils

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import cl.martinez.puppychopvet.MainActivity
import cl.martinez.puppychopvet.R
import cl.martinez.puppychopvet.data.CitaVeterinaria

/**
 * Helper para gestionar notificaciones de citas veterinarias PuppyChop
 */
object NotificationHelper {

    private const val CHANNEL_ID = "citas_puppychop"
    private const val CHANNEL_NAME = "Citas Veterinarias"
    private const val CHANNEL_DESCRIPTION = "Notificaciones de citas veterinarias PuppyChop"

    /**
     * Crea el canal de notificación (requerido en Android 8.0+)
     */
    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance).apply {
                description = CHANNEL_DESCRIPTION
                enableLights(true)
                enableVibration(true)
            }
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    /**
     * Muestra una notificación inmediata para una cita veterinaria
     */
    fun showNotification(context: Context, cita: CitaVeterinaria) {
        // Verificar permisos en Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
        }

        // Intent para abrir la app
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("citaId", cita.id)
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            cita.id,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Construir notificación
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("🐶 Recordatorio de Cita - ${cita.nombreMascota}")
            .setContentText("Cita con el veterinario hoy a las ${cita.horaCita}")
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(
                        "🐕 Mascota: ${cita.nombreMascota}\n" +
                                "👤 Dueño: ${cita.nombreDueno}\n" +
                                "⏰ Hora: ${cita.horaCita}\n" +
                                "📋 Motivo: ${cita.motivo}"
                    )
            )
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_REMINDER)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setVibrate(longArrayOf(0, 500, 250, 500))
            .build()

        // Mostrar notificación
        with(NotificationManagerCompat.from(context)) {
            notify(cita.id, notification)
        }
    }

    /**
     * Cancela una notificación específica
     */
    fun cancelNotification(context: Context, citaId: Int) {
        with(NotificationManagerCompat.from(context)) {
            cancel(citaId)
        }
    }

    /**
     * Verifica si la app tiene permisos de notificación
     */
    fun hasNotificationPermission(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
    }

    /**
     * Programa una notificación para una fecha y hora específicas
     * TODO: Implementar con AlarmManager o WorkManager
     */
    fun scheduleNotification(context: Context, cita: CitaVeterinaria) {
        // Implementación futura con AlarmManager o WorkManager
    }

    /**
     * Cancela una notificación programada
     */
    fun cancelScheduledNotification(context: Context, citaId: Int) {
        // Implementación futura
    }
}