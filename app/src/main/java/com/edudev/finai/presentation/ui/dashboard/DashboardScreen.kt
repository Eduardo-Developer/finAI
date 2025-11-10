package com.edudev.finai.presentation.ui.dashboard

import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.edudev.finai.domain.model.AIInsight
import com.edudev.finai.domain.model.DashboardData
import com.edudev.finai.domain.model.InsightType
import com.edudev.finai.presentation.components.BottomNavGraph
import com.edudev.finai.presentation.components.BottomNavigationBar
import com.edudev.finai.presentation.components.DashboardContent
import com.edudev.finai.presentation.components.DashboardTopBarTitle
import com.edudev.finai.presentation.components.ErrorView
import com.edudev.finai.presentation.components.FinAiTopAppBar
import com.edudev.finai.presentation.components.IncomeExpenseItem
import com.edudev.finai.presentation.navigation.Screen
import com.edudev.finai.presentation.ui.history.HistoryScreen
import com.edudev.finai.presentation.ui.settings.SettingsScreen
import com.edudev.finai.presentation.viewmodel.DashboardViewModel
import java.text.NumberFormat
import java.util.Locale

@Composable
fun DashboardScreen(
    onLogout: () -> Unit,
    onAddTransactionClick: () -> Unit
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController = navController)
        },
        floatingActionButton = {
            if (currentRoute == Screen.Dashboard.route) {
                FloatingActionButton(onClick = onAddTransactionClick) {
                    Icon(Icons.Default.Add, contentDescription = "Adicionar transação")
                }
            }
        }
    ) { innerPadding ->
        BottomNavGraph(
            navController = navController,
            modifier = Modifier.padding(innerPadding),
            onLogout = onLogout
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardRouteContent(
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val isAIEnabled by viewModel.isAIEnabled.collectAsState(initial = true)

    Column {
        FinAiTopAppBar(
            title = {
                DashboardTopBarTitle(
                    name = uiState.userName,
                    base64Image = uiState.userImage
                )
            },
            actions = {
                IconButton(onClick = { viewModel.refresh() }) {
                    Icon(Icons.Default.Refresh, contentDescription = "Atualizar")
                }
            }
        )
        AnimatedContent(
            targetState = uiState.isLoading,
        ) { isLoading ->
            when {
                isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                uiState.error != null -> {
                    ErrorView(message = uiState.error ?: "Erro desconhecido")
                }
                else -> {
                    DashboardContent(
                        dashboardData = uiState.dashboardData,
                        aiInsights = if (isAIEnabled) uiState.aiInsights else emptyList(),
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}
