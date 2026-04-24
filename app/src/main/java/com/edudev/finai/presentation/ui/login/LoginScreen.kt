package com.edudev.finai.presentation.ui.login

import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.edudev.finai.R
import com.edudev.finai.presentation.biometric.BiometricAuthenticator
import com.edudev.finai.presentation.biometric.BiometricResult
import com.edudev.finai.presentation.viewmodel.LoginUiState
import com.edudev.finai.presentation.viewmodel.LoginViewModel
import com.edudev.finai.ui.components.FinAICard
import com.edudev.finai.ui.components.FinAIPrimaryButton
import com.edudev.finai.ui.components.FinAITextField
import com.edudev.finai.ui.modifiers.noiseBackground
import com.edudev.finai.ui.theme.Emerald
import com.edudev.finai.ui.theme.FinAITheme

@Composable
fun LoginScreen(onLoginSuccess: () -> Unit, onSignUpClick: () -> Unit, viewModel: LoginViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val currentOnLoginSuccess by rememberUpdatedState(onLoginSuccess)

    val biometricAuthenticator =
        remember(context) {
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
                    else -> {}
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
                title = { Text(stringResource(R.string.biometric_login_title)) },
                text = { Text(stringResource(R.string.biometric_login_message)) },
                confirmButton = {
                    TextButton(onClick = onBiometricDialogConfirm) { Text(stringResource(R.string.btn_yes)) }
                },
                dismissButton = {
                    TextButton(onClick = onBiometricDialogDismiss) { Text(stringResource(R.string.btn_no)) }
                }
            )
        } else {
            LaunchedEffect(Unit) {
                onBiometricOnboardingDeclined()
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.background,
        modifier = Modifier.noiseBackground(0.05f)
    ) { paddingValues ->
        Box(
            modifier =
            Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            // Background Atmospheric Glow
            Box(
                modifier =
                Modifier
                    .size(400.dp)
                    .background(Emerald.copy(alpha = 0.02f), CircleShape)
                    .blur(100.dp)
            )

            Column(
                modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column(
                    // Level 3 spacing
                    modifier = Modifier.padding(bottom = 64.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text =
                        buildAnnotatedString {
                            append("Fin")
                            withStyle(style = SpanStyle(color = Emerald)) {
                                append("AI")
                            }
                        },
                        style = MaterialTheme.typography.displayMedium,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = (-2).sp
                    )
                    Text(
                        text = stringResource(R.string.app_tagline),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 4.dp),
                        letterSpacing = 1.sp
                    )
                }

                FinAICard(
                    modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(bottom = 48.dp),
                    // Increased for airier feel
                    containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                    // Larger corner radius for container
                    shape = RoundedCornerShape(32.dp)
                ) {
                    Column(
                        modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        // More internal breathing room
                        // Level 3 internal spacing
                        verticalArrangement = Arrangement.spacedBy(32.dp)
                    ) {
                        // Email Field
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(
                                text = stringResource(R.string.label_email_address),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontWeight = FontWeight.Bold
                            )
                            FinAITextField(
                                value = uiState.email,
                                onValueChange = onEmailChange,
                                placeholder = "your@email.com",
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Email,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            )
                        }

                        // Password Field
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(
                                text = stringResource(R.string.label_password_caps),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontWeight = FontWeight.Bold
                            )
                            FinAITextField(
                                value = uiState.pass,
                                onValueChange = onPasswordChange,
                                placeholder = "••••••••",
                                visualTransformation = if (uiState.isPasswordVisible) {
                                    VisualTransformation.None
                                } else {
                                    PasswordVisualTransformation()
                                },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Lock,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.size(20.dp)
                                    )
                                },
                                trailingIcon = {
                                    val imageResId =
                                        if (uiState.isPasswordVisible) {
                                            R.drawable.visibility_off
                                        } else {
                                            R.drawable.visibility_on
                                        }
                                    IconButton(onClick = onTogglePasswordVisibility) {
                                        Icon(
                                            painter = painterResource(id = imageResId),
                                            contentDescription = "Toggle visibility",
                                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                }
                            )
                            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
                                TextButton(onClick = { /* TODO */ }) {
                                    Text(
                                        text = stringResource(R.string.btn_forgot_password),
                                        style = MaterialTheme.typography.labelLarge,
                                        color = Emerald
                                    )
                                }
                            }
                        }

                        // Submit Button
                        FinAIPrimaryButton(
                            text = stringResource(R.string.btn_login),
                            onClick = onLoginClick,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }

                // Footer
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.msg_dont_have_account),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    TextButton(onClick = onSignUpClick) {
                        Text(
                            text = stringResource(R.string.btn_sign_up),
                            style = MaterialTheme.typography.bodySmall,
                            color = Emerald,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LoginScreenPreview() {
    FinAITheme {
        val dummyUiState =
            LoginUiState(
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
