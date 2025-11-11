package com.edudev.finai.presentation.viewmodel.login

data class LoginUiState(
    val email: String = "",
    val pass: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val promptBiometric: Boolean = false,
    val showBiometricOnboardingDialog: Boolean = false,
    val navigateToHome: Boolean = false
)
