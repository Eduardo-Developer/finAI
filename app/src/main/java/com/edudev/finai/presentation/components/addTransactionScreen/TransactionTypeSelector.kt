package com.edudev.finai.presentation.components.addTransactionScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.edudev.finai.domain.model.TransactionType
import com.edudev.finai.ui.theme.Emerald
import com.edudev.finai.ui.theme.FinAITheme
import com.edudev.finai.ui.theme.Onyx
import com.edudev.finai.ui.theme.SurfaceContainerHighest
import com.edudev.finai.ui.theme.SurfaceContainerLow

@Composable
fun TransactionTypeToggle(
    modifier: Modifier = Modifier,
    selectedType: TransactionType,
    onTypeSelected: (TransactionType) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier
            .fillMaxWidth()
            .background(SurfaceContainerLow, RoundedCornerShape(100.dp))
            .padding(6.dp)
    ) {
        val options = listOf(
            TransactionType.INCOME to "INCOME",
            TransactionType.EXPENSE to "EXPENSE"
        )

        options.forEach { (type, label) ->
            val isSelected = selectedType == type
            ToggleButton(
                label = label,
                isSelected = isSelected,
                activeColor = SurfaceContainerHighest,
                modifier = Modifier.weight(1f),
                onClick = { onTypeSelected(type) }
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF111412)
@Composable
private fun TransactionTypeTogglePreview() {
    FinAITheme {
        Column(Modifier.background(Onyx).padding(16.dp)) {
            TransactionTypeToggle(
                selectedType = TransactionType.INCOME,
                onTypeSelected = {}
            )
        }
    }
}