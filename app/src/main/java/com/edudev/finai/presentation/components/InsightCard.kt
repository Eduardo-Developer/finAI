package com.edudev.finai.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.edudev.finai.domain.model.AIInsight
import com.edudev.finai.domain.model.InsightType

@Composable
fun InsightCard(insight: AIInsight) {
    val backgroundColor = when (insight.type) {
        InsightType.WARNING -> MaterialTheme.colorScheme.errorContainer
        InsightType.SUGGESTION -> MaterialTheme.colorScheme.primaryContainer
        InsightType.POSITIVE -> MaterialTheme.colorScheme.tertiaryContainer
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Text(
            text = insight.message,
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}