package com.edudev.finai.presentation.components.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.edudev.finai.presentation.navigation.Screen
import com.edudev.finai.presentation.ui.dashboard.DashboardRouteContent
import com.edudev.finai.presentation.ui.history.HistoryScreen
import com.edudev.finai.presentation.ui.profile_edit.ProfileEditScreen
import com.edudev.finai.presentation.ui.settings.SettingsScreen
import com.edudev.finai.ui.theme.FinAITheme

@Composable
fun BottomNavGraph(
    navController: NavHostController,
    onLogout: () -> Unit,
    modifier: Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Dashboard.route,
        modifier = modifier
    ) {
        composable(route = Screen.Dashboard.route) {
            DashboardRouteContent()
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

@Preview(showBackground = true)
@Composable
private fun BottomNavGraphPreview() {
    FinAITheme {
        val navController = rememberNavController()

        BottomNavGraph(
            navController = navController,
            onLogout = {  },
            modifier = Modifier
        )
    }
}

