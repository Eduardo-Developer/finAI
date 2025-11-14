package com.edudev.finai.presentation.viewmodel

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edudev.finai.data.repository.ThemeRepository
import com.edudev.finai.di.PreferencesKeys
import com.edudev.finai.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val dataStore: DataStore<Preferences>,
    private val themeRepository: ThemeRepository
) : ViewModel() {

    private val _isAIEnabled = MutableStateFlow(true)
    val isAIEnabled = _isAIEnabled.asStateFlow()

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
        _isAIEnabled.value = isEnabled
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
