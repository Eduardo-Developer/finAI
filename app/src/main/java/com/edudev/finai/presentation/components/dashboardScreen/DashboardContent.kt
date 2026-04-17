package com.edudev.finai.presentation.components.dashboardScreen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.edudev.finai.domain.model.AIInsight
import com.edudev.finai.domain.model.DashboardData
import com.edudev.finai.domain.model.Transaction
import com.edudev.finai.ui.theme.FinAITheme
import java.text.NumberFormat
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
