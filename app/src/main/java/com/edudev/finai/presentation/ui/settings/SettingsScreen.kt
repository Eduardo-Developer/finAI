package com.edudev.finai.presentation.ui.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.edudev.finai.presentation.components.FinAiTopAppBar
import com.edudev.finai.presentation.viewmodel.MainViewModel
import com.edudev.finai.presentation.viewmodel.SettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onLogout: () -> Unit,
    mainViewModel: MainViewModel = hiltViewModel(),
    settingsViewModel: SettingsViewModel = hiltViewModel()
) {
    val isAIEnabled by settingsViewModel.isAIEnabled.collectAsState()
    val isDarkTheme by mainViewModel.isDarkTheme.collectAsState()

    val showLogoffDialog by settingsViewModel.showLogoffConfirmDialog.collectAsState()

    Scaffold(
        topBar = {
            FinAiTopAppBar(title = { Text("Configurações") })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SettingsSection(title = "IA e Análise") {
                SettingsItemSwitch(
                    title = "Sugestões da IA",
                    description = "Ativar análises inteligentes de gastos",
                    checked = isAIEnabled,
                    onCheckedChange = { settingsViewModel.setAIEnabled(it) }
                )
            }

            SettingsSection(title = "Aparência") {
                SettingsItemSwitch(
                    title = "Tema Escuro",
                    description = "Usar tema escuro",
                    checked = isDarkTheme,
                    onCheckedChange = { mainViewModel.setDarkTheme(it) }
                )
            }

            SettingsSection(title = "Conta") {
                SettingsItemButton(
                    title = "Sair",
                    description = "Desconectar sua conta do aplicativo",
                    onClick = {
                        settingsViewModel.onLogoffIntent()
                    }
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

        //Dialog de confirmação de Logoff
        if (showLogoffDialog) {
            AlertDialog(
                onDismissRequest = { settingsViewModel.onDismissLogofftDialog() },
                title = { Text("Confirmação") },
                text = { Text("Deseja realmente deslogar sua conta?") },
                confirmButton = {
                    TextButton(onClick = {
                        settingsViewModel.onLogoffIntent()
                        onLogout()
                    }) {
                        Text("Sim")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { settingsViewModel.onDismissLogofftDialog() }) {
                        Text("Não")
                    }
                }
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
