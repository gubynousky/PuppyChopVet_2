package cl.martinez.puppychopvet.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import cl.martinez.puppychopvet.view.DetailScreen
import cl.martinez.puppychopvet.view.FormScreen
import cl.martinez.puppychopvet.view.HomeScreen
import cl.martinez.puppychopvet.viewmodel.CitaVeterinariaViewModel

/**
 * Rutas de navegación de PuppyChop
 */
sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Form : Screen("form")
    object Detail : Screen("detail/{citaId}") {
        fun createRoute(citaId: Int) = "detail/$citaId"
    }
}

/**
 * Configuración de navegación de PuppyChop
 */
@Composable
fun AppNavigation(
    navController: NavHostController,
    viewModel: CitaVeterinariaViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        // Pantalla principal
        composable(route = Screen.Home.route) {
            HomeScreen(
                viewModel = viewModel,
                onNavigateToForm = {
                    navController.navigate(Screen.Form.route)
                },
                onNavigateToDetail = { citaId ->
                    navController.navigate(Screen.Detail.createRoute(citaId))
                }
            )
        }

        // Pantalla de formulario
        composable(route = Screen.Form.route) {
            FormScreen(
                viewModel = viewModel,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        // Pantalla de detalle
        composable(
            route = Screen.Detail.route,
            arguments = listOf(
                navArgument("citaId") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->
            val citaId = backStackEntry.arguments?.getInt("citaId") ?: 0
            DetailScreen(
                citaId = citaId,
                viewModel = viewModel,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}