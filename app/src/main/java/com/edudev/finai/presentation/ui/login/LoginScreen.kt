package com.edudev.finai.presentation.ui.login

import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.edudev.finai.R
import com.edudev.finai.presentation.biometric.BiometricAuthenticator
import com.edudev.finai.presentation.biometric.BiometricResult
import com.edudev.finai.presentation.components.FinAiTextField
import com.edudev.finai.presentation.viewmodel.login.LoginViewModel

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onSignUpClick: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val currentOnLoginSuccess by rememberUpdatedState(onLoginSuccess)

    val biometricAuthenticator = remember(context) {
        BiometricAuthenticator(context as AppCompatActivity)
    }

    LaunchedEffect(uiState.navigateToHome) {
        if (uiState.navigateToHome) {
            currentOnLoginSuccess()
            viewModel.onNavigatedToHome()
        }
    }

    LaunchedEffect(uiState.error) {
        uiState.error?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.onErrorShown()
        }
    }

    LaunchedEffect(uiState.promptBiometric) {
        if (uiState.promptBiometric) {
            biometricAuthenticator.prompt { result ->
                when (result) {
                    is BiometricResult.Success -> viewModel.loginWithBiometrics()
                    else -> { }
                }
                viewModel.onBiometricPromptShown()
            }
        }
    }

    if (uiState.showBiometricOnboardingDialog) {
        if (biometricAuthenticator.isBiometricAuthAvailable()) {
            AlertDialog(
                onDismissRequest = {
                    viewModel.declineBiometricOnboarding()
                },
                title = { Text("Login com Biometria") },
                text = { Text("Deseja usar sua impressão digital para entrar mais rápido da próxima vez?") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            viewModel.enableBiometricsAndSaveCredentials()
                        }
                    ) {
                        Text("Sim")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            viewModel.declineBiometricOnboarding()
                        }
                    ) {
                        Text("Não")
                    }
                }
            )
        } else {
            LaunchedEffect(Unit) {
                viewModel.declineBiometricOnboarding()
            }
        }
    }


    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Image(
                painter = painterResource(R.drawable.finaisvg),
                contentDescription = "logo"
            )

            Spacer(modifier = Modifier.height(32.dp))

            FinAiTextField(
                value = uiState.email,
                onValueChange = viewModel::onEmailChange,
                label = "Email",
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            FinAiTextField(
                value = uiState.pass,
                onValueChange = viewModel::onPasswordChange,
                label = "Senha",
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(32.dp))

            if (uiState.isLoading) {
                CircularProgressIndicator()
            } else {
                Button(
                    onClick = { viewModel.login() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Entrar")
                }
                Spacer(modifier = Modifier.height(8.dp))
                TextButton(onClick = onSignUpClick) {
                    Text("Não tem uma conta? Cadastre-se")
                }
            }
        }
    }
}
