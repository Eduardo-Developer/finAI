package com.edudev.finai.presentation.components.addTransactionScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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