package com.edudev.finai.presentation.ui.dashboard

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.edudev.finai.R
import com.edudev.finai.core.shimmerEffect
import com.edudev.finai.presentation.components.BottomNavGraph
import com.edudev.finai.presentation.components.BottomNavigationBar
import com.edudev.finai.presentation.components.DashboardContent
import com.edudev.finai.presentation.components.DashboardTopBarTitle
import com.edudev.finai.presentation.components.ErrorView
import com.edudev.finai.presentation.components.FinAiTopAppBar
import com.edudev.finai.presentation.navigation.Screen
import com.edudev.finai.presentation.viewmodel.DashboardViewModel

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
                    Icon(Icons.Default.Add, contentDescription = stringResource(id = R.string.add_transaction))
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
                if (uiState.userName.isEmpty() && uiState.error == null) {
                    ShimmerTitlePlaceholder()
                } else {
                    DashboardTopBarTitle(
                        name = uiState.userName,
                        base64Image = uiState.userImage
                    )
                }
            },
            actions = {
                IconButton(onClick = { viewModel.refresh() }) {
                    Icon(Icons.Default.Refresh, contentDescription = stringResource(id = R.string.refresh))
                }
            }
        )
        AnimatedContent(
            targetState = uiState.isLoading,
        ) { isLoading ->
            when {
                isLoading && uiState.dashboardData == null -> { // Show full screen loading only if there's no data yet
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                uiState.error != null -> {
                    ErrorView(message = uiState.error ?: stringResource(id = R.string.unknown_error))
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

@Composable
fun ShimmerTitlePlaceholder() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
                .shimmerEffect()
        )
        Box(
            modifier = Modifier
                .height(20.dp)
                .fillMaxWidth(0.5f)
                .shimmerEffect()
        )
    }
}