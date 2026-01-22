package com.edudev.finai.presentation.components.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.edudev.finai.presentation.navigation.Screen
import com.edudev.finai.presentation.ui.dashboard.DashboardScreen
import com.edudev.finai.presentation.ui.history.HistoryScreen
import com.edudev.finai.presentation.ui.profile_edit.ProfileEditScreen
import com.edudev.finai.presentation.ui.settings.SettingsScreen

@Composable
fun BottomNavGraph(
    navController: NavHostController,
    rootNavController: NavHostController,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Dashboard.route,
        modifier = modifier
    ) {
        composable(route = Screen.Dashboard.route) {
            DashboardScreen(
                onAddTransactionClick = { rootNavController.navigate(Screen.AddTransaction.route) }
            )
        }

        composable(route = Screen.History.route) {
            HistoryScreen()
        }

        composable(route = Screen.Settings.route) {
            SettingsScreen(
                onLogout = onLogout,
                onNavigateToProfile = { navController.navigate(Screen.ProfileEdit.route) }
            )
        }

        composable(route = Screen.ProfileEdit.route) {
            ProfileEditScreen(onNavigateBack = { navController.popBackStack() })
        }
    }
}
