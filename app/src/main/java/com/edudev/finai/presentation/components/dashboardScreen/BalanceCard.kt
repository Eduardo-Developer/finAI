package com.edudev.finai.presentation.components.dashboardScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.edudev.finai.ui.theme.FinAITheme
import java.text.NumberFormat

@Composable
fun BalanceCard(
    balance: Double,
    income: Double,
    expense: Double,
    currencyFormat: NumberFormat
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Saldo Total",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = currencyFormat.format(balance),
                style = MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.Bold,
                color = if (balance >= 0) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.error
            )
            Spacer(modifier = Modifier.height(24.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                IncomeExpenseItem(
                    label = "Receitas",
                    value = income,
                    currencyFormat = currencyFormat,
                    isIncome = true
                )
                IncomeExpenseItem(
                    label = "Despesas",
                    value = expense,
                    currencyFormat = currencyFormat,
                    isIncome = false
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BalanceCardPreview() {
    FinAITheme {
        BalanceCard(
            balance = 1000.0,
            income = 500.0,
            expense = 500.0,
            currencyFormat = NumberFormat.getCurrencyInstance()
        )
    }
}