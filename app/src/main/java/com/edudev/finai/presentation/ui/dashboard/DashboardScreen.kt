package com.edudev.finai.presentation.ui.dashboard
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.edudev.finai.R
import com.edudev.finai.domain.model.AIInsight
import com.edudev.finai.domain.model.CategorySpending
import com.edudev.finai.domain.model.DashboardData
import com.edudev.finai.domain.model.InsightType
import com.edudev.finai.domain.model.MonthlyChartData
import com.edudev.finai.domain.model.Transaction
import com.edudev.finai.domain.model.TransactionType
import com.edudev.finai.presentation.components.FinAiTopAppBar
import com.edudev.finai.presentation.components.dashboardScreen.DashboardContent
import com.edudev.finai.presentation.components.dashboardScreen.DashboardTopBarTitle
import com.edudev.finai.presentation.components.dashboardScreen.DateRangePickerModal
import com.edudev.finai.presentation.components.dashboardScreen.ErrorView
import com.edudev.finai.presentation.components.dashboardScreen.ShimmerTitlePlaceholder
import com.edudev.finai.presentation.viewmodel.DashboardUiState
import com.edudev.finai.presentation.viewmodel.DashboardViewModel
import com.edudev.finai.ui.theme.FinAITheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun DashboardScreen(onNavigateToHistory: () -> Unit, viewModel: DashboardViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    DashboardScreenContent(
        uiState = uiState,
        onNavigateToHistory = onNavigateToHistory,
        onDateRangeSelected = { startDate, endDate ->
            viewModel.onDateFilterChanged(startDate, endDate)
        },
        onDismissDatePicker = { viewModel.onDismissDatePicker() },
        onShowDatePicker = { viewModel.onShowDatePicker() },
        onRefresh = { viewModel.refresh() },
        onClearDateFilter = { viewModel.onDateFilterChanged(null, null) }
    )
}

@Composable
fun DashboardScreenContent(
    uiState: DashboardUiState,
    onNavigateToHistory: () -> Unit,
    onDateRangeSelected: (Date?, Date?) -> Unit,
    onDismissDatePicker: () -> Unit,
    onShowDatePicker: () -> Unit,
    onRefresh: () -> Unit,
    onClearDateFilter: () -> Unit
) {
    if (uiState.showDateRangePicker) {
        DateRangePickerModal(
            onDateRangeSelected = { dateRange ->
                val startDate = dateRange.first?.let { Date(it) }
                val endDate = dateRange.second?.let { Date(it) }
                onDateRangeSelected(startDate, endDate)
            },
            onDismiss = onDismissDatePicker
        )
    }

    Scaffold { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
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
                    IconButton(onClick = onShowDatePicker) {
                        Icon(
                            Icons.Default.DateRange,
                            contentDescription = "Filtrar por data"
                        )
                    }
                    IconButton(onClick = onRefresh) {
                        Icon(
                            Icons.Default.Refresh,
                            contentDescription = stringResource(id = R.string.refresh)
                        )
                    }
                }
            )

            // Filter Indicator
            Box(
                modifier =
                Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                val dateFormat = SimpleDateFormat("dd MMM, yyyy", Locale.getDefault())
                val filterText =
                    if (uiState.filterStartDate != null && uiState.filterEndDate != null) {
                        "${dateFormat.format(uiState.filterStartDate)} - ${dateFormat.format(uiState.filterEndDate)}"
                    } else {
                        "Mês Atual"
                    }

                AssistChip(
                    onClick = onShowDatePicker,
                    label = { Text(filterText) },
                    trailingIcon =
                    if (uiState.filterStartDate != null) {
                        {
                            IconButton(
                                onClick = onClearDateFilter,
                                modifier = Modifier.padding(start = 4.dp)
                            ) {
                                Icon(Icons.Default.Clear, contentDescription = "Limpar Filtro")
                            }
                        }
                    } else {
                        null
                    }
                )
            }

            if (uiState.error != null) {
                ErrorView(message = uiState.error)
            } else {
                DashboardContent(
                    dashboardData = uiState.dashboardData,
                    transactions = uiState.transactions,
                    aiInsights = if (uiState.isAIEnabled) uiState.aiInsights else emptyList(),
                    isLoadingAI = uiState.isLoadingAI,
                    onViewAllTransactions = onNavigateToHistory,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DashboardScreenPreview() {
    val dummyInsights =
        listOf(
            AIInsight("Você economizou 15% a mais que no mês passado!", InsightType.POSITIVE),
            AIInsight("Seu gasto com alimentação subiu consideravelmente.", InsightType.WARNING)
        )

    val dummyTransactions =
        listOf(
            Transaction(1, "u1", 150.0, "Alimentação", "Restaurante", TransactionType.EXPENSE, Date()),
            Transaction(2, "u1", 2500.0, "Salário", "Emprego", TransactionType.INCOME, Date()),
            Transaction(3, "u1", 50.0, "Transporte", "Uber", TransactionType.EXPENSE, Date())
        )

    val dummyDashboardData =
        DashboardData(
            totalBalance = 2300.0,
            monthlyIncome = 2500.0,
            monthlyExpense = 200.0,
            categorySpendings =
            listOf(
                CategorySpending("Alimentação", 150.0, 0.75f),
                CategorySpending("Transporte", 50.0, 0.25f)
            ),
            aiInsights = dummyInsights,
            monthlyChartData =
            listOf(
                MonthlyChartData("Jan", 2000.0, 1500.0),
                MonthlyChartData("Feb", 2200.0, 1800.0),
                MonthlyChartData("Mar", 2500.0, 200.0)
            )
        )

    FinAITheme {
        DashboardScreenContent(
            uiState =
            DashboardUiState(
                userName = "Eduardo",
                dashboardData = dummyDashboardData,
                transactions = dummyTransactions,
                aiInsights = dummyInsights
            ),
            onNavigateToHistory = {},
            onDateRangeSelected = { _, _ -> },
            onDismissDatePicker = {},
            onShowDatePicker = {},
            onRefresh = {},
            onClearDateFilter = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DashboardScreenLoadingPreview() {
    FinAITheme {
        DashboardScreenContent(
            uiState =
            DashboardUiState(
                isLoading = true,
                isLoadingAI = true
            ),
            onNavigateToHistory = {},
            onDateRangeSelected = { _, _ -> },
            onDismissDatePicker = {},
            onShowDatePicker = {},
            onRefresh = {},
            onClearDateFilter = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DashboardScreenErrorPreview() {
    FinAITheme {
        DashboardScreenContent(
            uiState =
            DashboardUiState(
                error = "Ocorreu um erro ao carregar os dados."
            ),
            onNavigateToHistory = {},
            onDateRangeSelected = { _, _ -> },
            onDismissDatePicker = {},
            onShowDatePicker = {},
            onRefresh = {},
            onClearDateFilter = {}
        )
    }
}
