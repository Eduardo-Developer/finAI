package com.edudev.finai.presentation.components.historyTransaction

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.edudev.finai.domain.model.TransactionType
import com.edudev.finai.presentation.viewmodel.TransactionFilter
import com.edudev.finai.ui.theme.FinAITheme

@Composable
fun FilterChips(
    modifier: Modifier = Modifier,
    selectedFilter: TransactionFilter,
    onFilterSelected: (TransactionFilter) -> Unit
) {
    Row(
        modifier = modifier
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FilterChip(
            selected = selectedFilter is TransactionFilter.All,
            onClick = { onFilterSelected(TransactionFilter.All) },
            label = { Text("Todas") }
        )
        FilterChip(
            selected = selectedFilter is TransactionFilter.ByType &&
                    selectedFilter.type == TransactionType.INCOME,
            onClick = { onFilterSelected(TransactionFilter.ByType(TransactionType.INCOME)) },
            label = { Text("Receitas") }
        )
        FilterChip(
            selected = selectedFilter is TransactionFilter.ByType &&
                    selectedFilter.type == TransactionType.EXPENSE,
            onClick = { onFilterSelected(TransactionFilter.ByType(TransactionType.EXPENSE)) },
            label = { Text("Despesas") }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun FilterChipsPreview() {
    FinAITheme {
        FilterChips(
            selectedFilter = TransactionFilter.ByType(TransactionType.EXPENSE),
            onFilterSelected = {}
        )
    }
}