package com.edudev.finai.presentation.viewmodel

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edudev.finai.data.repository.ThemeRepository
import com.edudev.finai.di.PreferencesKeys
import com.edudev.finai.domain.repository.AuthRepository
import com.edudev.finai.domain.repository.PreferencesRepository
import com.edudev.finai.domain.usecase.GetUserDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SettingsUiState(
    val userName: String = "",
    val userImage: String = "",
    val error: String? = null
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val dataStore: DataStore<Preferences>,
    private val themeRepository: ThemeRepository,
    private val preferencesRepository: PreferencesRepository,
    private val getUserDataUseCase: GetUserDataUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadUserData()
    }

    private fun loadUserData() {
        viewModelScope.launch {
            try {
                val userId = authRepository.currentUser
                if (userId != null) {
                    getUserDataUseCase(userId).collect { user ->
                        user?.let {
                            _uiState.update {
                                it.copy(
                                    userName = user.fullName,
                                    userImage = user.imageUrl ?: ""
                                )
                            }
                        }
                    }
                } else {
                    _uiState.update { it.copy(error = "Usuário não autenticado.") }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message ?: "Erro ao carregar dados do usuário") }
            }
        }
    }

    val isAIEnabled: StateFlow<Boolean> = preferencesRepository.isAIEnabled
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false
        )

    private val _showLogoffConfirmDialog = MutableStateFlow(false)
    val showLogoffConfirmDialog = _showLogoffConfirmDialog.asStateFlow()

    val isBiometricAuthEnabled: StateFlow<Boolean> = dataStore.data.map {
        it[PreferencesKeys.IS_BIOMETRIC_AUTH_ENABLED] ?: false
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = false
    )

    val isDarkTheme: StateFlow<Boolean> = themeRepository.isDarkTheme
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false
        )

    fun setDarkTheme(isDark: Boolean) {
        viewModelScope.launch {
            themeRepository.setDarkTheme(isDark)
        }
    }

    fun setAIEnabled(isEnabled: Boolean) {
        viewModelScope.launch {
            preferencesRepository.setAIEnabled(isEnabled)
        }
    }

    fun setBiometricAuthEnabled(isEnabled: Boolean) {
        viewModelScope.launch {
            dataStore.edit {
                it[PreferencesKeys.IS_BIOMETRIC_AUTH_ENABLED] = isEnabled
            }
        }
    }

    fun logout() {
        authRepository.logout()
    }

    fun onLogoffIntent() {
        _showLogoffConfirmDialog.value = true
    }

    fun onDismissLogofftDialog() {
        _showLogoffConfirmDialog.value = false
    }
}
