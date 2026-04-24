package com.edudev.finai.presentation.components.addTransactionScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.edudev.finai.ui.theme.FinAITheme
import com.edudev.finai.ui.theme.MintEmerald
import com.edudev.finai.ui.theme.OnSurfaceVariant
import com.edudev.finai.ui.theme.OnSurfaceWhite
import com.edudev.finai.ui.theme.Onyx
import com.edudev.finai.ui.theme.SurfaceContainerHighest

@Composable
fun FormField(label: String, value: String, onValueChange: (String) -> Unit, icon: ImageVector, placeholder: String) {
    Column {
        Text(
            text = label.uppercase(),
            style =
            MaterialTheme.typography.labelSmall.copy(
                color = MintEmerald,
                letterSpacing = 1.sp,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
        )
        Row(
            modifier =
            Modifier
                .fillMaxWidth()
                .background(SurfaceContainerHighest, RoundedCornerShape(24.dp))
                .padding(horizontal = 16.dp, vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = OnSurfaceVariant,
                modifier = Modifier.size(24.dp)
            )

            Box(Modifier.padding(start = 12.dp)) {
                if (value.isEmpty()) {
                    Text(
                        text = placeholder,
                        style =
                        MaterialTheme.typography.bodyMedium.copy(
                            color = OnSurfaceVariant.copy(alpha = 0.5f)
                        )
                    )
                }
                BasicTextField(
                    value = value,
                    onValueChange = onValueChange,
                    modifier = Modifier.fillMaxWidth(),
                    textStyle =
                    MaterialTheme.typography.bodyMedium.copy(
                        color = OnSurfaceWhite,
                        fontWeight = FontWeight.Medium
                    ),
                    cursorBrush = SolidColor(MintEmerald),
                    singleLine = true
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF111412)
@Composable
private fun FormFieldPreview() {
    FinAITheme {
        Box(Modifier.background(Onyx).padding(16.dp)) {
            FormField(
                label = "Description",
                value = "",
                onValueChange = {},
                icon = Icons.Default.Edit,
                placeholder = "What was this for?"
            )
        }
    }
}
