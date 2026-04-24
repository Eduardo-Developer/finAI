package com.edudev.finai.presentation.components.dashboardScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.edudev.finai.ui.theme.FinAITheme

@Composable
fun AIInsightsBentoCard(modifier: Modifier = Modifier) {
    Box(
        modifier =
        modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(32.dp))
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .background(
                brush =
                Brush.linearGradient(
                    colors =
                    listOf(
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.05f),
                        Color.Transparent
                    )
                )
            )
            .padding(24.dp)
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(bottom = 24.dp)
            ) {
                Icon(
                    Icons.Default.AutoAwesome,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = "AI Insights",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                InsightSubCard(
                    icon = Icons.Default.Savings,
                    title = "Optimization Found",
                    description = "Moving $5k to high-yield savings could earn an extra $240/yr."
                )

                InsightSubCard(
                    icon = Icons.Default.Warning,
                    title = "Subscription Alert",
                    description = "You have 3 unused subscriptions this month totaling $45.97."
                )
            }
        }
    }
}

@Composable
private fun InsightSubCard(icon: ImageVector, title: String, description: String) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceContainerLow,
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier.fillMaxWidth(),
        border =
        ButtonDefaults.outlinedButtonBorder.copy(
            brush =
            Brush.linearGradient(
                listOf(
                    MaterialTheme.colorScheme.outline.copy(alpha = 0.1f),
                    Color.Transparent
                )
            )
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier =
                Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
            }

            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 18.sp
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF111412)
@Composable
private fun AIInsightsBentoCardPreview() {
    FinAITheme {
        Box(modifier = Modifier.padding(16.dp)) {
            AIInsightsBentoCard()
        }
    }
}
