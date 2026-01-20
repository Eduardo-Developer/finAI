package com.edudev.finai.presentation.components.dashboardScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.edudev.finai.domain.model.AIInsight
import com.edudev.finai.domain.model.DashboardData
import com.edudev.finai.ui.theme.FinAITheme
import java.text.NumberFormat
import java.util.Locale

@Composable
fun DashboardContent(
    dashboardData: DashboardData?,
    aiInsights: List<AIInsight>,
    isLoadingAI: Boolean,
    modifier: Modifier = Modifier
) {
    val currencyFormat = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))

    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            BalanceCard(
                balance = dashboardData?.totalBalance ?: 0.0,
                income = dashboardData?.monthlyIncome ?: 0.0,
                expense = dashboardData?.monthlyExpense ?: 0.0,
                currencyFormat = currencyFormat
            )
        }

        item {
            Text(
                text = "Insights da IA",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }
        if (isLoadingAI) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        } else {
            items(aiInsights) { insight ->
                InsightCard(insight = insight)
            }
        }

        if (dashboardData?.categorySpendings?.isNotEmpty() == true) {
            item {
                Text(
                    text = "Gastos por Categoria",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }
            items(dashboardData.categorySpendings) { category ->
                CategorySpendingCard(
                    category = category.category,
                    total = category.total,
                    percentage = category.percentage,
                    currencyFormat = currencyFormat
                )
            }
        }
    }
}

@Preview
@Composable
private fun DashboardContentPreview() {
    FinAITheme {
        DashboardContent(
            dashboardData = DashboardData(
                totalBalance = 1000.0,
                monthlyIncome = 500.0,
                monthlyExpense = 500.0,
                categorySpendings = emptyList(),
                aiInsights = emptyList(),
                monthlyChartData = emptyList()
            ),
            aiInsights = TODO(),
            modifier = TODO(),
            isLoadingAI = false
        )
    }
}