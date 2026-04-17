package com.edudev.finai.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edudev.finai.domain.repository.AuthRepository
import com.edudev.finai.domain.repository.PreferencesRepository
import com.edudev.finai.domain.usecase.GetUserDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SettingsUiState(
    val userName: String = "",
    val userImage: String = "",
    val isAIEnabled: Boolean = false,
    val isBiometricAuthEnabled: Boolean = false,
    val showLogoffConfirmDialog: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val preferencesRepository: PreferencesRepository,
    private val getUserDataUseCase: GetUserDataUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    private val _showLogoffConfirmDialog = savedStateHandle.getStateFlow("show_logoff_dialog", false)

    val uiState: StateFlow<SettingsUiState> = combine(
        _uiState,
        preferencesRepository.isAIEnabled,
        preferencesRepository.isBiometricAuthEnabled,
        _showLogoffConfirmDialog
    ) { state, aiEnabled, biometricEnabled, showDialog ->
        state.copy(
            isAIEnabled = aiEnabled,
            isBiometricAuthEnabled = biometricEnabled,
            showLogoffConfirmDialog = showDialog
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = SettingsUiState()
    )

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


    fun setAIEnabled(isEnabled: Boolean) {
        viewModelScope.launch {
            preferencesRepository.setAIEnabled(isEnabled)
        }
    }

    fun setBiometricAuthEnabled(isEnabled: Boolean) {
        viewModelScope.launch {
            preferencesRepository.setBiometricAuthEnabled(isEnabled)
            if (!isEnabled) {
                preferencesRepository.clearBiometricCredentials()
            }
        }
    }

    fun isUserLoggedIn(): Boolean = authRepository.isUserLoggedIn()

    fun logout() {
        authRepository.logout()
    }

    fun onLogoffIntent() {
        savedStateHandle["show_logoff_dialog"] = true
    }

    fun onDismissLogofftDialog() {
        savedStateHandle["show_logoff_dialog"] = false
    }
}
