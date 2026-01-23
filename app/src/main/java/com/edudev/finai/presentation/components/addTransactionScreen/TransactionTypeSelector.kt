package com.edudev.finai.presentation.components.addTransactionScreen

import android.widget.ToggleButton
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.edudev.finai.domain.model.TransactionType
import com.edudev.finai.ui.theme.FinAITheme

@Composable
fun TransactionTypeSelector(
    modifier: Modifier = Modifier,
    selectedType: TransactionType,
    onTypeSelected: (TransactionType) -> Unit
) {
    Column {
        Text(
            text = "Tipo",
            style = MaterialTheme.typography.titleMedium,
            modifier = modifier.padding(bottom = 8.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TransactionType.entries.forEach { type ->
                FilterChip(
                    selected = selectedType == type,
                    onClick = { onTypeSelected(type) },
                    label = { Text(if (type == TransactionType.INCOME) "Receita" else "Despesa") },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun TransactionTypeToggle(
    selectedType: TransactionType,
    onTypeSelected: (TransactionType) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF1F5F9), RoundedCornerShape(16.dp))
            .padding(6.dp)
    ) {
        val modifier = Modifier.weight(1f)

        ToggleButton(
            label = "Receita",
            isSelected = selectedType == TransactionType.INCOME,
            activeColor = MaterialTheme.colorScheme.primary,
            modifier = modifier,
            onClick = { onTypeSelected(TransactionType.INCOME) }
        )

        ToggleButton(
            label = "Despesa",
            isSelected = selectedType == TransactionType.EXPENSE,
            activeColor = MaterialTheme.colorScheme.surfaceDim,
            modifier = modifier,
            onClick = { onTypeSelected(TransactionType.EXPENSE) }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TransactionTypeSelectorPreview() {
    FinAITheme {
        TransactionTypeSelector(
            modifier = Modifier.padding(PaddingValues(8.dp)),
            selectedType = TransactionType.INCOME,
            onTypeSelected = {}
        )
    }
}

@Preview
@Composable
private fun TransactionTypeTogglePreview() {
    FinAITheme {
        TransactionTypeToggle(
            selectedType = TransactionType.INCOME,
            onTypeSelected = {}
        )
    }
}