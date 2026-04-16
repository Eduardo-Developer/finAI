package com.edudev.finai.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edudev.finai.domain.repository.AuthRepository
import com.edudev.finai.domain.repository.PreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val preferencesRepository: PreferencesRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()

    fun checkBiometricLogin() {
        viewModelScope.launch {
            val isBiometricEnabled = preferencesRepository.isBiometricAuthEnabled.first()
            val hasCredentials = preferencesRepository.hasBiometricCredentials()

            if (isBiometricEnabled && hasCredentials) {
                _uiState.update { it.copy(promptBiometric = true) }
            }
        }
    }

    fun onEmailChange(email: String) {
        _uiState.update { it.copy(email = email) }
    }

    fun onPasswordChange(password: String) {
        _uiState.update { it.copy(pass = password) }
    }

    fun login() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val loginResult = authRepository.login(uiState.value.email, uiState.value.pass)

                loginResult.onSuccess {
                    val isBiometricEnabled = preferencesRepository.isBiometricAuthEnabled.first()
                    val hasCredentials = preferencesRepository.hasBiometricCredentials()

                    if (isBiometricEnabled && !hasCredentials) {
                        preferencesRepository.saveBiometricCredentials(uiState.value.email, uiState.value.pass)
                        _uiState.update { it.copy(isLoading = false, navigateToHome = true) }
                    } else {
                        _uiState.update { it.copy(isLoading = false, navigateToHome = true) }
                    }
                }

                loginResult.onFailure { exception ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = exception.message ?: "Erro desconhecido"
                        )
                    }
                }

            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Erro desconhecido"
                    )
                }
            }
        }
    }

    fun loginWithBiometrics() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val (email, pass) = preferencesRepository.getBiometricCredentials()

                if (!email.isNullOrBlank() && !pass.isNullOrBlank()) {
                    authRepository.login(email, pass)
                    _uiState.update { it.copy(isLoading = false, navigateToHome = true) }
                } else {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = "Credenciais biométricas não encontradas."
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Erro no login biométrico"
                    )
                }
            }
        }
    }

    fun enableBiometricsAndSaveCredentials() {
        val currentEmail = uiState.value.email
        val currentPass = uiState.value.pass

        if (currentEmail.isBlank() || currentPass.isBlank()) {
            _uiState.update {
                it.copy(
                    showBiometricOnboardingDialog = false,
                    navigateToHome = true
                )
            }
            return
        }

        viewModelScope.launch {
            preferencesRepository.saveBiometricCredentials(currentEmail, currentPass)
            _uiState.update {
                it.copy(
                    showBiometricOnboardingDialog = false,
                    navigateToHome = true
                )
            }
        }
    }

    fun declineBiometricOnboarding() {
        viewModelScope.launch {
            preferencesRepository.markBiometricPromptShown()
            _uiState.update {
                it.copy(
                    showBiometricOnboardingDialog = false,
                    navigateToHome = true
                )
            }
        }
    }

    fun onErrorShown() {
        _uiState.update { it.copy(error = null) }
    }

    fun onBiometricPromptShown() {
        _uiState.update { it.copy(promptBiometric = false) }
    }

    fun onNavigatedToHome() {
        _uiState.update { it.copy(navigateToHome = false) }
    }

    fun togglePasswordVisibility() {
        _uiState.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
    }
}

data class LoginUiState(
    val email: String = "",
    val pass: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val promptBiometric: Boolean = false,
    val showBiometricOnboardingDialog: Boolean = false,
    val navigateToHome: Boolean = false,
    val isPasswordVisible: Boolean = false
)
