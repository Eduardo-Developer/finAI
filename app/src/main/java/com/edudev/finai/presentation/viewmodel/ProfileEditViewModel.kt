package com.edudev.finai.presentation.viewmodel

import android.content.ContentResolver
import android.net.Uri
import android.util.Base64
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edudev.finai.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.ByteArrayOutputStream
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ProfileEditUiState(
    val fullName: String = "",
    val email: String = "",
    val phoneNumber: String = "",
    val imageUrl: String? = null,
    val selectedImageUri: Uri? = null,
    val newImageBase64: String? = null,
    val error: String? = null,
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false
)

@HiltViewModel
class ProfileEditViewModel
@Inject
constructor(
    private val authRepository: AuthRepository,
    private val contentResolver: ContentResolver
) : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileEditUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadUserData()
    }

    private fun loadUserData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val userId = authRepository.currentUser
            if (userId != null) {
                authRepository.getUserData(userId).collect { user ->
                    user?.let {
                        _uiState.update { state ->
                            state.copy(
                                fullName = it.fullName,
                                email = it.email,
                                phoneNumber = it.phoneNumber,
                                imageUrl = it.imageUrl,
                                isLoading = false
                            )
                        }
                    }
                }
            } else {
                _uiState.update { it.copy(isLoading = false, error = "Usuário não autenticado") }
            }
        }
    }

    fun onNameChange(newName: String) {
        _uiState.update { it.copy(fullName = newName) }
    }

    fun onEmailChange(newEmail: String) {
        _uiState.update { it.copy(email = newEmail) }
    }

    fun onPhoneChange(newPhone: String) {
        _uiState.update { it.copy(phoneNumber = newPhone) }
    }

    fun onImageSelected(uri: Uri?) {
        viewModelScope.launch {
            uri?.let {
                val base64 = uriToBase64(it)
                _uiState.update { state ->
                    state.copy(
                        selectedImageUri = it,
                        newImageBase64 = base64
                    )
                }
            }
        }
    }

    fun saveProfile() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            val result =
                authRepository.updateUserProfile(
                    fullName = uiState.value.fullName,
                    phoneNumber = uiState.value.phoneNumber,
                    imageBase64 = uiState.value.newImageBase64
                )

            result.fold(
                onSuccess = {
                    _uiState.update { it.copy(isLoading = false, isSuccess = true) }
                },
                onFailure = { error ->
                    _uiState.update { it.copy(isLoading = false, error = error.message) }
                }
            )
        }
    }

    private fun uriToBase64(uri: Uri): String? {
        return try {
            val inputStream = contentResolver.openInputStream(uri)
            val byteArrayOutputStream = ByteArrayOutputStream()
            inputStream?.use { input ->
                input.copyTo(byteArrayOutputStream)
            }
            val imageBytes = byteArrayOutputStream.toByteArray()
            Base64.encodeToString(imageBytes, Base64.DEFAULT)
        } catch (e: Exception) {
            null
        }
    }
}
