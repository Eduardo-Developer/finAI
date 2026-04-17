package com.edudev.finai.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edudev.finai.domain.repository.AuthRepository
import com.edudev.finai.domain.repository.ThemeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

data class MainUiState(
    val isDarkTheme: Boolean = false,
    val isUserLoggedIn: Boolean = false
)

@HiltViewModel
class MainViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    themeRepository: ThemeRepository
) : ViewModel() {

    val uiState: StateFlow<MainUiState> = themeRepository.isDarkTheme
        .map { isDark ->
            MainUiState(
                isDarkTheme = isDark,
                isUserLoggedIn = authRepository.isUserLoggedIn()
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = MainUiState(isUserLoggedIn = authRepository.isUserLoggedIn())
        )

    fun isUserLoggedIn(): Boolean = authRepository.isUserLoggedIn()
    
    fun logout() {
        authRepository.logout()
    }
}
