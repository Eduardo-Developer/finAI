package com.edudev.finai.presentation.components.addTransactionScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.edudev.finai.ui.theme.Emerald
import com.edudev.finai.ui.theme.FinAITheme
import com.edudev.finai.ui.theme.OnSurfaceVariant
import com.edudev.finai.ui.theme.Onyx

@Composable
fun ToggleButton(label: String, isSelected: Boolean, activeColor: Color, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Box(
        modifier =
        modifier
            .height(48.dp)
            .clip(RoundedCornerShape(100.dp))
            .background(if (isSelected) activeColor else Color.Transparent)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            color = if (isSelected) Emerald else OnSurfaceVariant,
            style =
            MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF111412)
@Composable
private fun ToggleButtonPreview() {
    FinAITheme {
        Box(Modifier.background(Onyx)) {
            ToggleButton(
                label = "INCOME",
                isSelected = true,
                activeColor = Color(0xFF323533),
                onClick = {}
            )
        }
    }
}
