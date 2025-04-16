package com.mangaversetest.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mangaversetest.presentation.SharedViewModel
import com.mangaversetest.presentation.ui.dashboard.DashboardScreen
import com.mangaversetest.presentation.ui.login.LoginScreen
import com.mangaversetest.presentation.ui.login.LoginViewModel

sealed class NavigationScreens(val route: String) {
    data object Login : NavigationScreens("login")
    data object Dashboard : NavigationScreens("dashboard")


}

sealed class DashBoardNavigationScreens(val route: String) {
    data object Manga : DashBoardNavigationScreens("dashboard/manga")
    data object Face : DashBoardNavigationScreens("dashboard/face")
    data object MangaDetail : DashBoardNavigationScreens("mangaDetailScreen/{mangaJson}") {
        fun createRoute(mangaJson: String) = "mangaDetailScreen/$mangaJson"
    }
}

@Composable
fun AppNavHost(modifier: Modifier) {

    val sharedViewModel: SharedViewModel = hiltViewModel()
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = if (sharedViewModel.getIsLogin()) NavigationScreens.Dashboard.route else NavigationScreens.Login.route
    ) {
        composable(NavigationScreens.Login.route) {
            val viewModel: LoginViewModel = hiltViewModel()
            LoginScreen(viewModel = viewModel) {
                navController.navigate(NavigationScreens.Dashboard.route) {
                    popUpTo(navController.graph.findStartDestination().id) {
                        inclusive = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        }
        composable(NavigationScreens.Dashboard.route) {
            DashboardScreen()
        }
    }

}
