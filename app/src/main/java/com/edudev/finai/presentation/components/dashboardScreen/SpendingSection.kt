package com.edudev.finai.presentation.components.dashboardScreen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.edudev.finai.domain.model.CategorySpending
import com.edudev.finai.ui.theme.FinAITheme
import java.text.NumberFormat
import java.util.Locale
import androidx.compose.material.icons.filled.MoreHoriz

@Composable
fun SpendingSection(
    categorySpendings: List<CategorySpending>,
    currencyFormat: NumberFormat,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(32.dp)),
        color = MaterialTheme.colorScheme.surfaceContainer
    ) {
        Column(
            modifier = Modifier.padding(24.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Spending",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Icon(
                    androidx.compose.material.icons.Icons.Default.MoreHoriz,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(20.dp)
                )
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                categorySpendings.take(3).forEach { spending ->
                    SpendingProgressBar(
                        category = spending.category,
                        spent = spending.total,
                        percentage = spending.percentage,
                        currencyFormat = currencyFormat,
                        color = when(spending.category.lowercase()) {
                            "dining", "alimentação" -> MaterialTheme.colorScheme.primary
                            "transport", "transporte" -> MaterialTheme.colorScheme.secondary
                            else -> MaterialTheme.colorScheme.error
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun SpendingProgressBar(
    category: String,
    spent: Double,
    percentage: Float,
    currencyFormat: NumberFormat,
    color: Color
) {
    // Estimate budget for display: spent / fraction
    val fraction = percentage / 100f
    val budget = if (fraction > 0) spent / fraction else spent * 2

    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = category,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "${currencyFormat.format(spent)} / ${currencyFormat.format(budget)}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        LinearProgressIndicator(
            progress = { fraction },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp)),
            color = color,
            trackColor = MaterialTheme.colorScheme.surfaceContainerHighest,
            strokeCap = androidx.compose.ui.graphics.StrokeCap.Round
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF111412)
@Composable
private fun SpendingSectionPreview() {
    FinAITheme {
        Box(modifier = Modifier.padding(16.dp)) {
            SpendingSection(
                categorySpendings = listOf(
                    CategorySpending("Dining", 450.0, 75f),
                    CategorySpending("Transport", 120.0, 60f),
                    CategorySpending("Entertainment", 280.0, 93f)
                ),
                currencyFormat = NumberFormat.getCurrencyInstance(Locale.US)
            )
        }
    }
}
