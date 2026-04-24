package com.edudev.finai.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

/**
 * A card that follows the "No-Line" rule of the design system.
 * Uses background tonal shifts instead of borders or shadows.
 */
@Composable
fun FinAICard(
    modifier: Modifier = Modifier,
    containerColor: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.surfaceContainer,
    shape: androidx.compose.ui.graphics.Shape = RoundedCornerShape(16.dp),
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier =
        modifier
            .clip(shape)
            .background(containerColor)
            .padding(16.dp),
        content = content
    )
}

/**
 * A sectioning container that uses surface-container-low to group items visually.
 */
@Composable
fun FinAISection(modifier: Modifier = Modifier, content: @Composable BoxScope.() -> Unit) {
    Box(
        modifier =
        modifier
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceContainerLow)
            .padding(12.dp),
        content = content
    )
}

@androidx.compose.ui.tooling.preview.Preview
@Composable
private fun FinAICardPreview() {
    com.edudev.finai.ui.theme.FinAITheme {
        androidx.compose.foundation.layout.Column(
            modifier =
            Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp),
            verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(16.dp)
        ) {
            FinAISection(modifier = Modifier.fillMaxWidth()) {
                FinAICard(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "This is a card inside a section",
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            FinAISection(modifier = Modifier.fillMaxWidth()) {
                FinAICard(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "This is a card inside a section",
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}
