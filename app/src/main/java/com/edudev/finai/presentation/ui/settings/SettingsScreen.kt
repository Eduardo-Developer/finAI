package com.edudev.finai.presentation.ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.edudev.finai.R
import com.edudev.finai.presentation.components.settings.ProfileHeader
import com.edudev.finai.presentation.components.settings.SettingsItemButton
import com.edudev.finai.presentation.components.settings.SettingsItemSwitch
import com.edudev.finai.presentation.components.settings.SettingsSection
import com.edudev.finai.presentation.viewmodel.SettingsViewModel
import com.edudev.finai.ui.theme.FinAITheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onLogout: () -> Unit,
    onNavigateToProfile: () -> Unit,
    settingsViewModel: SettingsViewModel = hiltViewModel()
) {
    val uiState by settingsViewModel.uiState.collectAsStateWithLifecycle()

    if (uiState.showLogoffConfirmDialog) {
        AlertDialog(
            onDismissRequest = { settingsViewModel.onDismissLogofftDialog() },
            title = { Text(stringResource(R.string.logout_confirm_title)) },
            text = { Text(stringResource(R.string.logout_confirm_message)) },
            confirmButton = {
                TextButton(
                    onClick = {
                        settingsViewModel.logout()
                        onLogout()
                    }
                ) {
                    Text(stringResource(R.string.btn_logout), color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { settingsViewModel.onDismissLogofftDialog() }) {
                    Text(stringResource(R.string.btn_cancel))
                }
            }
        )
    }

    Scaffold { innerPadding ->
        SettingsScreenContent(
            modifier = Modifier.padding(innerPadding),
            userName = uiState.userName,
            userImage = uiState.userImage,
            isAIEnabled = uiState.isAIEnabled,
            isBiometricEnabled = uiState.isBiometricAuthEnabled,
            onLogout = { settingsViewModel.onLogoffIntent() },
            onNavigateToProfile = onNavigateToProfile,
            onToggleAI = { settingsViewModel.setAIEnabled(it) },
            onToggleBiometric = { settingsViewModel.setBiometricAuthEnabled(it) }
        )
    }
}

@Composable
fun SettingsScreenContent(
    modifier: Modifier,
    userName: String,
    userImage: String,
    isAIEnabled: Boolean,
    isBiometricEnabled: Boolean,
    onLogout: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onToggleAI: (Boolean) -> Unit,
    onToggleBiometric: (Boolean) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ProfileHeader(
            userName = userName,
            userImage = userImage,
            onProfileClick = onNavigateToProfile
        )

        SettingsSection(title = stringResource(R.string.settings_section_ai)) {
            SettingsItemSwitch(
                title = stringResource(R.string.settings_item_ai_title),
                description = stringResource(R.string.settings_item_ai_desc),
                checked = isAIEnabled,
                onCheckedChange = onToggleAI
            )
        }


        SettingsSection(title = stringResource(R.string.settings_section_security)) {
            SettingsItemSwitch(
                title = stringResource(R.string.settings_item_biometric_title),
                description = stringResource(R.string.settings_item_biometric_desc),
                checked = isBiometricEnabled,
                onCheckedChange = onToggleBiometric
            )
        }

        SettingsSection(title = stringResource(R.string.settings_section_account)) {
            SettingsItemButton(
                title = stringResource(R.string.btn_logout),
                description = stringResource(R.string.settings_item_logout_desc),
                onClick = onLogout
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = stringResource(R.string.app_version),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}

@androidx.compose.ui.tooling.preview.Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    FinAITheme {
        SettingsScreenContent(
            modifier = Modifier,
            userName = "Eduardo Oliveira",
            userImage = "",
            isAIEnabled = true,
            isBiometricEnabled = true,
            onLogout = {},
            onNavigateToProfile = {},
            onToggleAI = {},
            onToggleBiometric = {}
        )
    }
}
