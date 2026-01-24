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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.edudev.finai.R
import com.edudev.finai.presentation.biometric.BiometricAuthenticator
import com.edudev.finai.presentation.biometric.BiometricResult
import com.edudev.finai.presentation.components.FinAiButton
import com.edudev.finai.presentation.components.loginScreen.FinAiTextField
import com.edudev.finai.presentation.viewmodel.LoginViewModel
import com.edudev.finai.presentation.viewmodel.LoginUiState
import com.edudev.finai.ui.theme.FinAITheme

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

    LaunchedEffect(Unit) {
        viewModel.checkBiometricLogin()
    }

    LaunchedEffect(uiState.promptBiometric) {
        if (uiState.promptBiometric) {
            biometricAuthenticator.prompt { result ->
                when (result) {
                    is BiometricResult.Success -> viewModel.loginWithBiometrics()
                    else -> {
                    }
                }
                viewModel.onBiometricPromptShown()
            }
        }
    }

    LoginScreenContent(
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        biometricAuthAvailable = biometricAuthenticator.isBiometricAuthAvailable(),
        onEmailChange = viewModel::onEmailChange,
        onPasswordChange = viewModel::onPasswordChange,
        onTogglePasswordVisibility = viewModel::togglePasswordVisibility,
        onLoginClick = viewModel::login,
        onSignUpClick = onSignUpClick,
        onBiometricDialogConfirm = viewModel::enableBiometricsAndSaveCredentials,
        onBiometricDialogDismiss = viewModel::declineBiometricOnboarding,
        onBiometricOnboardingDeclined = viewModel::declineBiometricOnboarding
    )
}

@Composable
fun LoginScreenContent(
    uiState: LoginUiState,
    snackbarHostState: SnackbarHostState,
    biometricAuthAvailable: Boolean,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onTogglePasswordVisibility: () -> Unit,
    onLoginClick: () -> Unit,
    onSignUpClick: () -> Unit,
    onBiometricDialogConfirm: () -> Unit,
    onBiometricDialogDismiss: () -> Unit,
    onBiometricOnboardingDeclined: () -> Unit
) {
    if (uiState.showBiometricOnboardingDialog) {
        if (biometricAuthAvailable) {
            AlertDialog(
                onDismissRequest = onBiometricDialogDismiss,
                title = { Text("Login com Biometria") },
                text = { Text("Deseja usar sua impressão digital para entrar mais rápido da próxima vez?") },
                confirmButton = {
                    TextButton(
                        onClick = onBiometricDialogConfirm
                    ) {
                        Text("Sim")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = onBiometricDialogDismiss
                    ) {
                        Text("Não")
                    }
                }
            )
        } else {
            LaunchedEffect(Unit) {
                onBiometricOnboardingDeclined()
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

            FinAiTextField(
                modifier = Modifier.fillMaxWidth(),
                value = uiState.email,
                onValueChange = onEmailChange,
                label = "Email",
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = "E-mail"
                    )
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            FinAiTextField(
                modifier = Modifier.fillMaxWidth(),
                value = uiState.pass,
                onValueChange = onPasswordChange,
                label = "Senha",
                visualTransformation =
                if (uiState.isPasswordVisible) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                },
                trailingIcon = {
                    val imageResId = if (uiState.isPasswordVisible) {
                        R.drawable.visibility_off
                    } else {
                        R.drawable.visibility_on
                    }
                    IconButton(onClick = onTogglePasswordVisibility) {
                        Icon(
                            painter = painterResource(id = imageResId),
                            contentDescription = "Visibilidade da senha"
                        )
                    }
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Email"
                    )
                }
            )

            Spacer(modifier = Modifier.height(32.dp))

            FinAiButton(
                text = "Entrar",
                onClick = onLoginClick,
                isLoading = uiState.isLoading,
            )

            Spacer(modifier = Modifier.height(8.dp))
            TextButton(onClick = onSignUpClick) {
                Text("Não tem uma conta? Cadastre-se")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LoginScreenPreview() {
    FinAITheme {
        val dummyUiState = LoginUiState(
            email = "test@example.com",
            pass = "password123",
            isPasswordVisible = false,
            isLoading = false,
            error = null,
            navigateToHome = false,
            promptBiometric = false,
            showBiometricOnboardingDialog = false
        )
        LoginScreenContent(
            uiState = dummyUiState,
            snackbarHostState = remember { SnackbarHostState() },
            biometricAuthAvailable = true,
            onEmailChange = {},
            onPasswordChange = {},
            onTogglePasswordVisibility = {},
            onLoginClick = {},
            onSignUpClick = {},
            onBiometricDialogConfirm = {},
            onBiometricDialogDismiss = {},
            onBiometricOnboardingDeclined = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun LoginScreenLoadingPreview() {
    FinAITheme {
        val dummyUiState = LoginUiState(
            email = "test@example.com",
            pass = "password123",
            isPasswordVisible = false,
            isLoading = true,
            error = null,
            navigateToHome = false,
            promptBiometric = false,
            showBiometricOnboardingDialog = false
        )
        LoginScreenContent(
            uiState = dummyUiState,
            snackbarHostState = remember { SnackbarHostState() },
            biometricAuthAvailable = true,
            onEmailChange = {},
            onPasswordChange = {},
            onTogglePasswordVisibility = {},
            onLoginClick = {},
            onSignUpClick = {},
            onBiometricDialogConfirm = {},
            onBiometricDialogDismiss = {},
            onBiometricOnboardingDeclined = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun LoginScreenBiometricDialogPreview() {
    FinAITheme {
        val dummyUiState = LoginUiState(
            email = "test@example.com",
            pass = "password123",
            isPasswordVisible = false,
            isLoading = false,
            error = null,
            navigateToHome = false,
            promptBiometric = false,
            showBiometricOnboardingDialog = true
        )
        LoginScreenContent(
            uiState = dummyUiState,
            snackbarHostState = remember { SnackbarHostState() },
            biometricAuthAvailable = true,
            onEmailChange = {},
            onPasswordChange = {},
            onTogglePasswordVisibility = {},
            onLoginClick = {},
            onSignUpClick = {},
            onBiometricDialogConfirm = {},
            onBiometricDialogDismiss = {},
            onBiometricOnboardingDeclined = {}
        )
    }
}
