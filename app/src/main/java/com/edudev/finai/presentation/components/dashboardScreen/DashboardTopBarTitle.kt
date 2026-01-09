package com.edudev.finai.presentation.components.dashboardScreen

import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.edudev.finai.ui.theme.FinAITheme

@Composable
fun DashboardTopBarTitle(
    name: String,
    base64Image: String?,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
    ) {
        val bitmap = remember(base64Image) {
            base64Image?.let {
                try {
                    val decodedBytes = Base64.decode(it, Base64.DEFAULT)
                    BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
                } catch (e: IllegalArgumentException) {
                    null // Handle invalid Base64 string
                }
            }
        }

        if (bitmap != null) {
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = "Foto de Perfil",
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        } else {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Foto de Perfil",
                modifier = Modifier.size(40.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        if (name.isNotBlank()) {
            Text(text = "Olá, $name")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DashboardTopBarTitlePreview() {
    FinAITheme {
        DashboardTopBarTitle(
            modifier = Modifier.padding(PaddingValues(16.dp)),
            name = "João",
            base64Image = null
        )
    }
}
