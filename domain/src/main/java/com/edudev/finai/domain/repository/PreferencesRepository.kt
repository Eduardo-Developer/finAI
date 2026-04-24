package com.edudev.finai.domain.repository

import kotlinx.coroutines.flow.Flow

interface PreferencesRepository {
    val isAIEnabled: Flow<Boolean>
    val isDarkTheme: Flow<Boolean>
    val isBiometricAuthEnabled: Flow<Boolean>

    suspend fun setAIEnabled(enabled: Boolean)

    suspend fun setDarkTheme(enabled: Boolean)

    suspend fun setBiometricAuthEnabled(enabled: Boolean)

    suspend fun getBiometricCredentials(): Pair<String?, String?>

    suspend fun saveBiometricCredentials(email: String, pass: String)

    suspend fun markBiometricPromptShown()

    suspend fun clearBiometricCredentials()

    suspend fun hasBiometricCredentials(): Boolean
}
