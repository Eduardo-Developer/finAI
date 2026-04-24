package com.edudev.finai.presentation.components.historyTransaction

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.edudev.finai.domain.model.TransactionType
import com.edudev.finai.presentation.viewmodel.TransactionFilter
import com.edudev.finai.ui.theme.FinAITheme

@Composable
fun FilterChips(modifier: Modifier = Modifier, selectedFilter: TransactionFilter, onFilterSelected: (TransactionFilter) -> Unit) {
    Row(
        modifier =
        modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        FilterPill(
            text = "Todas",
            isSelected = selectedFilter is TransactionFilter.All,
            onClick = { onFilterSelected(TransactionFilter.All) }
        )
        FilterPill(
            text = "Receitas",
            isSelected =
            selectedFilter is TransactionFilter.ByType &&
                selectedFilter.type == TransactionType.INCOME,
            onClick = { onFilterSelected(TransactionFilter.ByType(TransactionType.INCOME)) }
        )
        FilterPill(
            text = "Despesas",
            isSelected =
            selectedFilter is TransactionFilter.ByType &&
                selectedFilter.type == TransactionType.EXPENSE,
            onClick = { onFilterSelected(TransactionFilter.ByType(TransactionType.EXPENSE)) }
        )
    }
}

@Composable
private fun FilterPill(text: String, isSelected: Boolean, onClick: () -> Unit) {
    val backgroundColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceContainer
    val contentColor = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
    val borderColor = if (isSelected) Color.Transparent else MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)

    Box(
        modifier =
        Modifier
            .clip(CircleShape)
            .background(backgroundColor)
            .then(
                if (!isSelected) Modifier.border(1.dp, borderColor, CircleShape) else Modifier
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 20.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style =
            MaterialTheme.typography.labelLarge.copy(
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                letterSpacing = 0.5.sp
            ),
            color = contentColor
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun FilterChipsPreview() {
    FinAITheme {
        Box(modifier = Modifier.background(MaterialTheme.colorScheme.background).padding(16.dp)) {
            FilterChips(
                selectedFilter = TransactionFilter.ByType(TransactionType.EXPENSE),
                onFilterSelected = {}
            )
        }
    }
}
