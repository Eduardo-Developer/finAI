import android.net.Uri
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.edudev.finai.R
import com.edudev.finai.ui.theme.FinAITheme

@Composable
fun ProfileImagePicker(
    imageUri: Uri?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(120.dp)
            .clip(CircleShape)
            .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        // Se uma imagem foi selecionada, mostre-a. Senão, mostre um ícone placeholder.
        if (imageUri != null) {
            AsyncImage(
                model = imageUri,
                contentDescription = "Foto de perfil selecionada",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop // Garante que a imagem preencha o círculo
            )
        } else {
            Icon(
                painter = painterResource(id = R.drawable.add_profile), // Crie este ícone
                contentDescription = "Adicionar foto de perfil",
                modifier = Modifier.size(48.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ProfileImagePickerPreview() {
    FinAITheme {
        ProfileImagePicker(
            modifier = Modifier.padding(PaddingValues(16.dp)),
            imageUri = null, onClick = {})
    }
}