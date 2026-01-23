package com.edudev.finai.presentation.components.addTransactionScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.edudev.finai.ui.theme.FinAITheme

@Composable
fun ToggleButton(
    label: String,
    isSelected: Boolean,
    activeColor: androidx.compose.ui.graphics.Color,
    modifier: Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .height(48.dp)
            .background(
                if (isSelected) activeColor else Color.White,
                RoundedCornerShape(12.dp)
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            color = if (isSelected) Color.White else Color(0xFF64748B),
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        )
    }
}

@Preview
@Composable
private fun ToggleButtonPreview() {
    FinAITheme {
        ToggleButton(
            label = "Label",
            isSelected = true,
            activeColor = Color(0xFF4CAF50),
            modifier = Modifier,
            onClick = {}
        )
    }
}