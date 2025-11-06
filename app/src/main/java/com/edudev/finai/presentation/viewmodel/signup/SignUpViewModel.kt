package com.edudev.finai.presentation.viewmodel.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edudev.finai.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SignUpState(
    val isLoading: Boolean = false,
    val signUpError: String? = null,
    val signUpSuccess: Boolean = false
)

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _signUpState = MutableStateFlow(SignUpState())
    val signUpState: StateFlow<SignUpState> = _signUpState

    fun signUp(fullName: String, email: String, pass: String, confirmPass: String) {
        if (pass != confirmPass) {
            _signUpState.value = SignUpState(signUpError = "Passwords do not match")
            return
        }

        viewModelScope.launch {
            _signUpState.value = SignUpState(isLoading = true)
            try {
                authRepository.signUp(fullName, email, pass)
                _signUpState.value = SignUpState(signUpSuccess = true)
            } catch (e: Exception) {
                _signUpState.value = SignUpState(signUpError = e.message)
            }
        }
    }
}