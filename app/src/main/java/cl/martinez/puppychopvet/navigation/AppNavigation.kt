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

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Form : Screen("form")
    object Detail : Screen("detail/{citaId}") {
        fun createRoute(citaId: Long) = "detail/$citaId"
    }
}

@Composable
fun AppNavigation(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(route = Screen.Home.route) {
            HomeScreen(
                onNavigateToForm = {
                    navController.navigate(Screen.Form.route)
                },
                onNavigateToDetail = { citaId ->
                    navController.navigate(Screen.Detail.createRoute(citaId))
                }
            )
        }

        composable(route = Screen.Form.route) {
            FormScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = Screen.Detail.route,
            arguments = listOf(
                navArgument("citaId") {
                    type = NavType.LongType
                }
            )
        ) { backStackEntry ->
            val citaId = backStackEntry.arguments?.getLong("citaId") ?: 0L
            DetailScreen(
                citaId = citaId,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}