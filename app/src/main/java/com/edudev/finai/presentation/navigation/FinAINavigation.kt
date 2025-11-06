package com.edudev.finai.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val title: String? = null, val icon: ImageVector? = null) {
    object Dashboard : Screen("dashboard", "Dashboard", Icons.Default.Home)
    object History : Screen("history", "Histórico", Icons.Default.List)
    object Settings : Screen("settings", "Configurações", Icons.Default.Settings)
    object AddTransaction : Screen("add_transaction", "Nova Transação", Icons.Default.Add)
    object SignUp : Screen("signup")
    object Login : Screen("login")
}
