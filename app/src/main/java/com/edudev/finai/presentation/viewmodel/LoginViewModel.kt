package com.edudev.finai.presentation.viewmodel

import android.content.SharedPreferences
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edudev.finai.di.PreferencesKeys
import com.edudev.finai.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val dataStore: DataStore<Preferences>,
    private val encryptedPrefs: SharedPreferences
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()

    init {
        checkBiometricLogin()
    }

    private fun checkBiometricLogin() {
        viewModelScope.launch {
            val preferences = dataStore.data.first()
            val isBiometricEnabled = preferences[PreferencesKeys.IS_BIOMETRIC_AUTH_ENABLED] ?: false

            val hasCredentials = withContext(Dispatchers.IO) {
                encryptedPrefs.contains(KEY_USER_EMAIL)
            }

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
                authRepository.login(uiState.value.email, uiState.value.pass)

                val preferences = dataStore.data.first()
                val promptShown = preferences[PreferencesKeys.BIOMETRIC_AUTH_PROMPT_SHOWN] ?: false

                if (!promptShown) {
                    _uiState.update { it.copy(isLoading = false, showBiometricOnboardingDialog = true) }
                } else {
                    _uiState.update { it.copy(isLoading = false, navigateToHome = true) }
                }

            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.message ?: "Erro desconhecido") }
            }
        }
    }

    fun loginWithBiometrics() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val email: String?
                val pass: String?

                withContext(Dispatchers.IO) {
                    email = encryptedPrefs.getString(KEY_USER_EMAIL, null)
                    pass = encryptedPrefs.getString(KEY_USER_PASS, null)
                }

                if (email != null && pass != null) {
                    authRepository.login(email, pass)
                    _uiState.update { it.copy(isLoading = false, navigateToHome = true) }
                } else {
                    throw IllegalStateException("Credenciais não encontradas.")
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.message ?: "Erro no login biométrico") }
            }
        }
    }

    fun enableBiometricsAndSaveCredentials() {
        viewModelScope.launch {
            dataStore.edit {
                it[PreferencesKeys.IS_BIOMETRIC_AUTH_ENABLED] = true
                it[PreferencesKeys.BIOMETRIC_AUTH_PROMPT_SHOWN] = true
            }
            withContext(Dispatchers.IO) {
                encryptedPrefs.edit()
                    .putString(KEY_USER_EMAIL, uiState.value.email)
                    .putString(KEY_USER_PASS, uiState.value.pass)
                    .commit()
            }

            _uiState.update { it.copy(showBiometricOnboardingDialog = false, navigateToHome = true, promptBiometric = true) }
        }
    }

    fun declineBiometricOnboarding() {
        viewModelScope.launch {
            dataStore.edit {
                it[PreferencesKeys.BIOMETRIC_AUTH_PROMPT_SHOWN] = true
            }
            _uiState.update { it.copy(showBiometricOnboardingDialog = false, navigateToHome = true) }
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

    companion object {
        private const val KEY_USER_EMAIL = "biometric_user_email"
        private const val KEY_USER_PASS = "biometric_user_pass"
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
