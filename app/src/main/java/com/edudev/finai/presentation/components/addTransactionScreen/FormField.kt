package com.edudev.finai.presentation.components.addTransactionScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.edudev.finai.ui.theme.FinAITheme

@Composable
fun FormField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    icon: ImageVector,
    placeholder: String
) {

    val PrimaryGreen = Color(0xFF107C57)
    val SecondaryRed = Color(0xFFE11D48)
    val BackgroundSlate = Color(0xFFF8FAFC)
    val TextDark = Color(0xFF1E293B)
    val TextMuted = Color(0xFF94A3B8)
    val BorderColor = Color(0xFFE2E8F0)

    Column {
        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = TextDark,
            modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(placeholder, color = TextMuted) },
            leadingIcon = { Icon(icon, contentDescription = null, tint = TextMuted) },
            shape = RoundedCornerShape(16.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedIndicatorColor = PrimaryGreen,
                unfocusedIndicatorColor = BorderColor
            ),
            singleLine = true
        )
    }
}

@Preview
@Composable
private fun FormFieldPreview() {
    FinAITheme {
        FormField(
            label = "Label",
            value = "Value",
            onValueChange = {},
            icon = Icons.Default.Edit,
            placeholder = "Placeholder"
        )
    }
}