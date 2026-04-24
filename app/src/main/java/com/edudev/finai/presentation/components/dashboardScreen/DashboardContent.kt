package com.edudev.finai.presentation.components.dashboardScreen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.edudev.finai.domain.model.AIInsight
import com.edudev.finai.domain.model.CategorySpending
import com.edudev.finai.domain.model.DashboardData
import com.edudev.finai.domain.model.InsightType
import com.edudev.finai.domain.model.MonthlyChartData
import com.edudev.finai.domain.model.Transaction
import com.edudev.finai.domain.model.TransactionType
import com.edudev.finai.ui.theme.FinAITheme
import java.text.NumberFormat
import java.util.Date
import java.util.Locale

@Composable
fun DashboardContent(
    dashboardData: DashboardData?,
    transactions: List<Transaction>,
    aiInsights: List<AIInsight>,
    isLoadingAI: Boolean,
    onViewAllTransactions: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val currencyFormat = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))

    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Section: Balance
        item {
            BalanceSection(
                balance = dashboardData?.totalBalance ?: 0.0,
                currencyFormat = currencyFormat
            )
        }

        // Section: Bento Grid (AI Insights & Spending)
        item {
            Column(verticalArrangement = Arrangement.spacedBy(24.dp)) {
                if (dashboardData?.categorySpendings?.isNotEmpty() == true) {
                    SpendingSection(
                        categorySpendings = dashboardData.categorySpendings,
                        currencyFormat = currencyFormat
                    )
                }

                if (isLoadingAI) {
                    Box(modifier = Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                    }
                } else if (aiInsights.isNotEmpty()) {
                    AIInsightsBentoCard()
                }
            }
        }

        // Section: Recent Activity
        item {
            RecentActivitySection(
                transactions = transactions,
                onViewAllClick = onViewAllTransactions
            )
        }

        // Bottom Spacer for Nav
        item {
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF5F5F5)
@Composable
fun DashboardContentPreview() {
    val mockCategorySpendings =
        listOf(
            CategorySpending("Alimentação", 1200.0, 0.4f),
            CategorySpending("Transporte", 500.0, 0.2f),
            CategorySpending("Lazer", 300.0, 0.1f),
            CategorySpending("Saúde", 400.0, 0.15f),
            CategorySpending("Outros", 150.0, 0.15f)
        )

    val mockMonthlyChartData =
        listOf(
            MonthlyChartData("Jan", 5000.0, 4000.0),
            MonthlyChartData("Fev", 5500.0, 3500.0),
            MonthlyChartData("Mar", 6000.0, 4500.0)
        )

    val mockDashboardData =
        DashboardData(
            totalBalance = 15450.50,
            monthlyIncome = 6000.0,
            monthlyExpense = 4500.0,
            categorySpendings = mockCategorySpendings,
            // Passing empty list to focus on spending
            aiInsights = emptyList(),
            monthlyChartData = mockMonthlyChartData
        )

    val mockTransactions =
        listOf(
            Transaction(
                id = 1,
                userId = "user1",
                amount = 150.0,
                category = "Alimentação",
                description = "Supermercado",
                type = TransactionType.EXPENSE,
                date = Date()
            ),
            Transaction(
                id = 2,
                userId = "user1",
                amount = 50.0,
                category = "Transporte",
                description = "Uber",
                type = TransactionType.EXPENSE,
                date = Date()
            ),
            Transaction(
                id = 3,
                userId = "user1",
                amount = 3000.0,
                category = "Salário",
                description = "Pagamento Mensal",
                type = TransactionType.INCOME,
                date = Date()
            )
        )

    val mockAIInsights =
        listOf(
            AIInsight(
                message = "Você gastou 15% a mais em alimentação este mês.",
                type = InsightType.WARNING,
                savingsSuggestion = 200.0
            )
        )

    FinAITheme {
        DashboardContent(
            dashboardData = mockDashboardData,
            transactions = mockTransactions,
            aiInsights = mockAIInsights,
            isLoadingAI = false,
            onViewAllTransactions = {}
        )
    }
}
