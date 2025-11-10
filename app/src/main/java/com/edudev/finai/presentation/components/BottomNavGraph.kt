package com.edudev.finai.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.edudev.finai.presentation.navigation.Screen
import com.edudev.finai.presentation.ui.dashboard.DashboardRouteContent
import com.edudev.finai.presentation.ui.history.HistoryScreen
import com.edudev.finai.presentation.ui.settings.SettingsScreen

@Composable
fun BottomNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    onLogout: () -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Dashboard.route,
        modifier = modifier
    ) {
        composable(Screen.Dashboard.route) {
            DashboardRouteContent()
        }
        composable(Screen.History.route) {
            HistoryScreen()
        }
        composable(Screen.Settings.route) {
            SettingsScreen(
                onLogout = onLogout
            )
        }
    }
}