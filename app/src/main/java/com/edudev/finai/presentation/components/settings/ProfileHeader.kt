package com.edudev.finai.presentation.components.settings

import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.edudev.finai.ui.theme.FinAITheme

@Composable
fun ProfileHeader(
    userName: String,
    userImage: String,
    onProfileClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        val imageBitmap = try {
            val decodedString = Base64.decode(userImage, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size).asImageBitmap()
        } catch (e: Exception) {
            null
        }

        if (imageBitmap != null) {
            Image(
                bitmap = imageBitmap,
                contentDescription = "Foto de perfil",
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        } else {
            Spacer(modifier = Modifier.size(60.dp))
        }

        Column {
            Text(
                text = userName.split(" ").firstOrNull() ?: "",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Meu Perfil",
                modifier = Modifier.clickable(onClick = onProfileClick),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ProfileHeaderPreview() {
    FinAITheme {
        ProfileHeader(
            userName = "Eduardo Oliveira",
            userImage = "",
            onProfileClick = {}
        )
    }
}