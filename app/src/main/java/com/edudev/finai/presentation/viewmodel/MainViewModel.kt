package com.edudev.finai.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.edudev.finai.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class MainUiState(
    val isUserLoggedIn: Boolean = false
)

@HiltViewModel
class MainViewModel
@Inject
constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(MainUiState(isUserLoggedIn = authRepository.isUserLoggedIn()))
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    fun isUserLoggedIn(): Boolean = authRepository.isUserLoggedIn()

    fun logout() {
        authRepository.logout()
        _uiState.update { it.copy(isUserLoggedIn = false) }
    }
}
