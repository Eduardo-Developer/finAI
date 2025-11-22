package com.edudev.finai.presentation.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edudev.finai.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SignUpUiState(
    val imageUri: Uri? = null,
    val isLoading: Boolean = false,
    val signUpError: String? = null,
    val signUpSuccess: Boolean = false,
    val fullName: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
)

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _signUpState = MutableStateFlow(SignUpUiState())
    val signUpState: StateFlow<SignUpUiState> = _signUpState

    fun onFullNameChanged(fullName: String) {
        _signUpState.update { it.copy(fullName = fullName) }
    }

    fun onEmailChanged(email: String) {
        _signUpState.update { it.copy(email = email) }
    }

    fun onPasswordChanged(password: String) {
        _signUpState.update { it.copy(password = password) }
    }

    fun onConfirmPasswordChanged(confirmPassword: String) {
        _signUpState.update { it.copy(confirmPassword = confirmPassword) }
    }

    fun onPhotoUriChanged(uri: Uri?) {
        _signUpState.update { it.copy(imageUri = uri) }
    }

    fun signUp(
        fullName: String,
        email: String,
        pass: String,
        confirmPass: String,
        imageUri: Uri?
    ) {
        if (pass != confirmPass) {
            _signUpState.value = SignUpUiState(signUpError = "Passwords do not match")
            return
        }

        viewModelScope.launch {
            _signUpState.value = SignUpUiState(isLoading = true)
            try {
                authRepository.signUp(fullName, email, pass, imageUri)
                _signUpState.value = SignUpUiState(signUpSuccess = true)
            } catch (e: Exception) {
                _signUpState.value = SignUpUiState(signUpError = e.message)
            }
        }
    }
}