package com.edudev.finai.presentation.components.dashboardScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.edudev.finai.core.shimmerEffect
import com.edudev.finai.ui.theme.FinAITheme

@Composable
fun ShimmerTitlePlaceholder() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
                .shimmerEffect()
        )
        Box(
            modifier = Modifier
                .height(20.dp)
                .fillMaxWidth(0.5f)
                .shimmerEffect()
        )
    }
}

@Preview
@Composable
private fun ShimmerTitlePlaceholderPreview() {
    FinAITheme {
        ShimmerTitlePlaceholder()
    }
}