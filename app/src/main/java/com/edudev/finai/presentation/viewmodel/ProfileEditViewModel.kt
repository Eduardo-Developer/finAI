package com.edudev.finai.presentation.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class ProfileEditUiState(
    val fullName: String = "",
    val email: String = "",
    val error: String? = null,
    val isLoading: Boolean = false
)

@HiltViewModel
class ProfileEditViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileEditUiState())
    val uiState = _uiState.asStateFlow()

    fun onNameChange(newName: String) {
        _uiState.update { it.copy(fullName = newName) }
    }

    fun onEmailChange(newEmail: String) {
        _uiState.update { it.copy(email = newEmail) }
    }

    fun saveProfile() {
        // TODO: Implement profile update logic
    }
}
