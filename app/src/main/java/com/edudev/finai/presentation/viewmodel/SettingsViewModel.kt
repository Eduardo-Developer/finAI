package com.edudev.finai.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.edudev.finai.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _isAIEnabled = MutableStateFlow(true)
    val isAIEnabled = _isAIEnabled.asStateFlow()

    private val _showLogoffConfirmDialog = MutableStateFlow(false)
    val showLogoffConfirmDialog = _showLogoffConfirmDialog.asStateFlow()

    fun setAIEnabled(isEnabled: Boolean) {
        _isAIEnabled.value = isEnabled
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
