package com.edudev.finai.presentation.ui.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.edudev.finai.presentation.components.navigation.BottomNavGraph
import com.edudev.finai.presentation.components.navigation.BottomNavigationBar

@Composable
fun MainScreen(
    rootNavController: NavHostController,
    onLogout: () -> Unit
) {
    val bottomNavController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController = bottomNavController)
        }
    ) { innerPadding ->
        BottomNavGraph(
            navController = bottomNavController,
            rootNavController = rootNavController,
            onLogout = onLogout,
            modifier = Modifier.padding(innerPadding)
        )
    }
}
