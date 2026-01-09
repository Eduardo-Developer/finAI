package com.edudev.finai.presentation.components.dashboardScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
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
fun IncomeExpenseItem(
    modifier: Modifier = Modifier,
    label: String,
    value: Double,
    currencyFormat: NumberFormat,
    isIncome: Boolean
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = modifier.height(4.dp))
        Text(
            text = currencyFormat.format(value),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = if (isIncome) MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.error
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun IncomeExpenseItemPreview() {
    FinAITheme {
        IncomeExpenseItem(
            label = "Receitas",
            value = 1000.0,
            currencyFormat = NumberFormat.getCurrencyInstance(),
            isIncome = true
        )
    }
}
