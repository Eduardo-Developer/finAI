package com.edudev.finai.presentation.ui.settings

import android.util.Base64
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.edudev.finai.presentation.viewmodel.SettingsViewModel
import android.graphics.BitmapFactory
import com.edudev.finai.ui.theme.FinAITheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onLogout: () -> Unit,
    onNavigateToProfile: () -> Unit,
    settingsViewModel: SettingsViewModel = hiltViewModel()
) {

    val uiState by settingsViewModel.uiState.collectAsState()
    val isAIEnabled by settingsViewModel.isAIEnabled.collectAsState()
    val isDarkTheme by settingsViewModel.isDarkTheme.collectAsState()
    val isBiometricAuthEnabled by settingsViewModel.isBiometricAuthEnabled.collectAsState()

    Scaffold {
        SettingsScreenContent(
            userName = uiState.userName,
            userImage = uiState.userImage,
            isAIEnabled = isAIEnabled,
            isDarkTheme = isDarkTheme,
            isBiometricEnabled = isBiometricAuthEnabled,
            onLogout = {
                settingsViewModel.onLogoffIntent()
                onLogout()
            },
            onNavigateToProfile = onNavigateToProfile,
            onToggleAI = { settingsViewModel.setAIEnabled(it) },
            onToggleTheme = { settingsViewModel.setDarkTheme(it) },
            onToggleBiometric = { settingsViewModel.setBiometricAuthEnabled(it) }
        )
    }
}

@Composable
fun SettingsScreenContent(
    userName: String,
    userImage: String,
    isAIEnabled: Boolean,
    isDarkTheme: Boolean,
    isBiometricEnabled: Boolean,
    onLogout: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onToggleAI: (Boolean) -> Unit,
    onToggleTheme: (Boolean) -> Unit,
    onToggleBiometric: (Boolean) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ProfileHeader(
            userName = userName,
            userImage = userImage,
            onProfileClick = onNavigateToProfile
        )

        SettingsSection(title = "IA e Análise") {
            SettingsItemSwitch(
                title = "Sugestões da IA",
                description = "Ativar análises inteligentes de gastos",
                checked = isAIEnabled,
                onCheckedChange = onToggleAI
            )
        }

        SettingsSection(title = "Aparência") {
            SettingsItemSwitch(
                title = "Tema Escuro",
                description = "Usar tema escuro",
                checked = isDarkTheme,
                onCheckedChange = onToggleTheme
            )
        }

        SettingsSection(title = "Segurança") {
            SettingsItemSwitch(
                title = "Login com Biometria",
                description = "Acessar sua conta com impressão digital ou rosto",
                checked = isBiometricEnabled,
                onCheckedChange = onToggleBiometric
            )
        }

        SettingsSection(title = "Conta") {
            SettingsItemButton(
                title = "Sair",
                description = "Desconectar sua conta do aplicativo",
                onClick = onLogout
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = "FinAI v1.0",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}

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

@Composable
fun SettingsSection(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            content()
        }
    }
}

@Composable
fun SettingsItemSwitch(
    title: String,
    description: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}

@Composable
fun SettingsItemButton(
    title: String,
    description: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
            contentDescription = title,
            tint = MaterialTheme.colorScheme.primary
        )
    }
}

@androidx.compose.ui.tooling.preview.Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    FinAITheme {
        SettingsScreenContent(
            userName = "Eduardo Oliveira",
            userImage = "",
            isAIEnabled = true,
            isDarkTheme = false,
            isBiometricEnabled = true,
            onLogout = {},
            onNavigateToProfile = {},
            onToggleAI = {},
            onToggleTheme = {},
            onToggleBiometric = {}
        )
    }
}
