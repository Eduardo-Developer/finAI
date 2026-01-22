package com.edudev.finai.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.edudev.finai.domain.repository.AuthRepository
import com.edudev.finai.presentation.navigation.Screen
import com.edudev.finai.presentation.ui.login.LoginScreen
import com.edudev.finai.presentation.ui.main.MainScreen
import com.edudev.finai.presentation.ui.signup.SignUpScreen
import com.edudev.finai.presentation.ui.transaction.AddTransactionScreen

@Composable
fun RootNavigationGraph (
    authRepository: AuthRepository,
) {
    val navController = rememberNavController()
    val startDestination =
        if (authRepository.getCurrentUser() != null) Screen.Main.route else Screen.Login.route

    NavHost(navController = navController, startDestination = startDestination) {
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.Main.route) {
                        popUpTo(navController.graph.startDestinationId) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onSignUpClick = { navController.navigate(Screen.SignUp.route) }
            )
        }

        composable(Screen.SignUp.route) {
            SignUpScreen(
                onSignUpSuccess = {
                    navController.navigate(Screen.Main.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onNavigateToLogin = { navController.popBackStack() }
            )
        }

        composable(Screen.Main.route) {
            MainScreen(
                rootNavController = navController,
                onLogout = {
                    authRepository.logout()
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Main.route) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(Screen.AddTransaction.route) {
            AddTransactionScreen(
                onBackClick = { navController.popBackStack() },
            )
        }
    }
}