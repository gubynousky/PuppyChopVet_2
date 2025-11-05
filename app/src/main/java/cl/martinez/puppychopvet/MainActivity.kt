package cl.martinez.puppychopvet

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.navigation.compose.rememberNavController
import cl.martinez.puppychopvet.data.AppDatabase
import cl.martinez.puppychopvet.model.CitaVeterinariaRepository
import cl.martinez.puppychopvet.navigation.AppNavigation
import cl.martinez.puppychopvet.ui.theme.PuppyChopTheme
import cl.martinez.puppychopvet.utils.NotificationHelper
import cl.martinez.puppychopvet.viewmodel.CitaVeterinariaViewModel

/**
 * Activity principal de PuppyChop - Sistema de Citas Veterinarias
 */
class MainActivity : ComponentActivity() {

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // Permiso concedido
        } else {
            // Permiso denegado
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        // Crear canal de notificaciones
        NotificationHelper.createNotificationChannel(this)

        // Solicitar permisos de notificación
        requestNotificationPermission()

        // Inicializar base de datos
        val database = AppDatabase.getDatabase(applicationContext)
        val citaVeterinariaDao = database.citaVeterinariaDao()

        // Crear Repository
        val repository = CitaVeterinariaRepository(citaVeterinariaDao)

        // Crear ViewModel
        val viewModel = CitaVeterinariaViewModel(repository)

        setContent {
            PuppyChopTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    AppNavigation(
                        navController = navController,
                        viewModel = viewModel
                    )
                }
            }
        }
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            when {
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED -> {
                    // Ya tiene permisos
                }
                else -> {
                    requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        }
    }
}