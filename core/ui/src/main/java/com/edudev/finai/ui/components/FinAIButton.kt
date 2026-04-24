package com.edudev.finai.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.edudev.finai.ui.theme.Emerald
import com.edudev.finai.ui.theme.Jade
import com.edudev.finai.ui.theme.MintEmerald

@Composable
fun FinAIPrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    shape: androidx.compose.ui.graphics.Shape = androidx.compose.foundation.shape.CircleShape,
    content: (@Composable () -> Unit)? = null
) {
    val gradient =
        Brush.linearGradient(
            colors = listOf(Emerald, Jade),
            start = androidx.compose.ui.geometry.Offset(0f, 0f),
            end = androidx.compose.ui.geometry.Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
        )

    Box(
        modifier =
        modifier
            .drawBehind {
                drawIntoCanvas { canvas ->
                    val paint =
                        android.graphics.Paint().apply {
                            color = Emerald.copy(alpha = 0.04f).toArgb()
                            setShadowLayer(12.dp.toPx(), 0f, 8.dp.toPx(), Emerald.copy(alpha = 0.04f).toArgb())
                        }
                    canvas.nativeCanvas.drawRoundRect(
                        0f,
                        0f,
                        size.width,
                        size.height,
                        12.dp.toPx(),
                        // Closest approximation
                        12.dp.toPx(),
                        paint
                    )
                }
            }
            .clip(shape)
            .background(gradient)
            .clickable(onClick = onClick)
            .padding(vertical = 16.dp, horizontal = 24.dp),
        contentAlignment = Alignment.Center
    ) {
        androidx.compose.foundation.layout.Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = androidx.compose.foundation.layout.Arrangement.Center
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onPrimary,
                fontWeight = FontWeight.Bold
            )
            if (content != null) {
                Box(modifier = Modifier.padding(start = 8.dp)) {
                    content()
                }
            }
        }
    }
}

@Composable
fun FinAISecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    shape: androidx.compose.ui.graphics.Shape = androidx.compose.foundation.shape.CircleShape
) {
    Box(
        modifier =
        modifier
            .clip(shape)
            .border(
                width = 1.dp,
                color = MintEmerald.copy(alpha = 0.2f),
                shape = shape
            )
            .clickable(onClick = onClick)
            .padding(vertical = 14.dp, horizontal = 24.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            color = Emerald,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@androidx.compose.ui.tooling.preview.Preview
@Composable
private fun FinAIButtonPreview() {
    com.edudev.finai.ui.theme.FinAITheme {
        androidx.compose.foundation.layout.Column(
            modifier =
            Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp),
            verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(16.dp)
        ) {
            FinAIPrimaryButton(text = "Primary Action", onClick = {})
            FinAISecondaryButton(text = "Secondary Action", onClick = {})
        }
    }
}
