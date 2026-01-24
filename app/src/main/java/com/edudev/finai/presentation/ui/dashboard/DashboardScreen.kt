package com.edudev.finai.presentation.ui.dashboard
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.edudev.finai.R
import com.edudev.finai.presentation.components.FinAiTopAppBar
import com.edudev.finai.presentation.components.dashboardScreen.DashboardContent
import com.edudev.finai.presentation.components.dashboardScreen.DashboardTopBarTitle
import com.edudev.finai.presentation.components.dashboardScreen.ErrorView
import com.edudev.finai.presentation.components.dashboardScreen.ShimmerTitlePlaceholder
import com.edudev.finai.presentation.viewmodel.DashboardViewModel

@Composable
fun DashboardScreen(
    onAddTransactionClick: () -> Unit,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val isAIEnabled by viewModel.isAIEnabled.collectAsState(initial = true)

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onAddTransactionClick) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = stringResource(id = R.string.add_transaction)
                )
            }
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            FinAiTopAppBar(
                title = {
                    if (uiState.userName.isEmpty() && uiState.error == null) {
                        ShimmerTitlePlaceholder()
                    } else {
                        DashboardTopBarTitle(
                            name = uiState.userName,
                            base64Image = uiState.userImage,
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.refresh() }) {
                        Icon(
                            Icons.Default.Refresh,
                            contentDescription = stringResource(id = R.string.refresh)
                        )
                    }
                }
            )
            if (uiState.error != null) {
                ErrorView(message = uiState.error ?: stringResource(id = R.string.unknown_error))
            } else {
                DashboardContent(
                    dashboardData = uiState.dashboardData,
                    aiInsights = if (isAIEnabled) uiState.aiInsights else emptyList(),
                    isLoadingAI = uiState.isLoadingAI,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}
