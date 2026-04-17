package com.edudev.finai.presentation.ui.dashboard
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.AssistChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.edudev.finai.R
import com.edudev.finai.presentation.components.FinAiTopAppBar
import com.edudev.finai.presentation.components.dashboardScreen.DashboardContent
import com.edudev.finai.presentation.components.dashboardScreen.DashboardTopBarTitle
import com.edudev.finai.presentation.components.dashboardScreen.DateRangePickerModal
import com.edudev.finai.presentation.components.dashboardScreen.ErrorView
import com.edudev.finai.presentation.components.dashboardScreen.ShimmerTitlePlaceholder
import com.edudev.finai.presentation.viewmodel.DashboardViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun DashboardScreen(
    onAddTransactionClick: () -> Unit,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    if (uiState.showDateRangePicker) {
        DateRangePickerModal(
            onDateRangeSelected = { dateRange ->
                val startDate = dateRange.first?.let { Date(it) }
                val endDate = dateRange.second?.let { Date(it) }
                viewModel.onDateFilterChanged(startDate, endDate)
            },
            onDismiss = { viewModel.onDismissDatePicker() }
        )
    }

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
                    IconButton(onClick = { viewModel.onShowDatePicker() }) {
                        Icon(
                            Icons.Default.DateRange,
                            contentDescription = "Filtrar por data"
                        )
                    }
                    IconButton(onClick = { viewModel.refresh() }) {
                        Icon(
                            Icons.Default.Refresh,
                            contentDescription = stringResource(id = R.string.refresh)
                        )
                    }
                }
            )
            
            // Filter Indicator
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                val dateFormat = SimpleDateFormat("dd MMM, yyyy", Locale.getDefault())
                val filterText = if (uiState.filterStartDate != null && uiState.filterEndDate != null) {
                    "${dateFormat.format(uiState.filterStartDate!!)} - ${dateFormat.format(uiState.filterEndDate!!)}"
                } else {
                    "Mês Atual"
                }

                AssistChip(
                    onClick = { viewModel.onShowDatePicker() },
                    label = { Text(filterText) },
                    trailingIcon = if (uiState.filterStartDate != null) {
                        {
                            IconButton(
                                onClick = { viewModel.onDateFilterChanged(null, null) },
                                modifier = Modifier.padding(start = 4.dp)
                            ) {
                                Icon(Icons.Default.Clear, contentDescription = "Limpar Filtro")
                            }
                        }
                    } else null
                )
            }

            if (uiState.error != null) {
                ErrorView(message = uiState.error ?: stringResource(id = R.string.unknown_error))
            } else {
                DashboardContent(
                    dashboardData = uiState.dashboardData,
                    aiInsights = if (uiState.isAIEnabled) uiState.aiInsights else emptyList(),
                    isLoadingAI = uiState.isLoadingAI,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}
