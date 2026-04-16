package com.edudev.finai.data.repository

import android.content.Context
import android.content.SharedPreferences
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.edudev.finai.domain.repository.PreferencesRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class PreferencesRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val encryptedPrefs: SharedPreferences
) : PreferencesRepository {
    private val dataStore = context.dataStore

    companion object {
        private val AI_ENABLED_KEY = booleanPreferencesKey("ai_enabled")
        private val DARK_THEME_KEY = booleanPreferencesKey("dark_theme")
        private val IS_BIOMETRIC_AUTH_ENABLED = booleanPreferencesKey("is_biometric_auth_enabled")
        private val BIOMETRIC_AUTH_PROMPT_SHOWN = booleanPreferencesKey("biometric_auth_prompt_shown")
        private const val KEY_USER_EMAIL = "biometric_user_email"
        private const val KEY_USER_PASS = "biometric_user_pass"
    }

    override val isAIEnabled: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[AI_ENABLED_KEY] ?: true
    }

    override val isDarkTheme: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[DARK_THEME_KEY] ?: false
    }

    override val isBiometricAuthEnabled: Flow<Boolean> = dataStore.data.map {
        it[IS_BIOMETRIC_AUTH_ENABLED] ?: false
    }

    override suspend fun setAIEnabled(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[AI_ENABLED_KEY] = enabled
        }
    }

    override suspend fun setDarkTheme(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[DARK_THEME_KEY] = enabled
        }
    }

    override suspend fun setBiometricAuthEnabled(enabled: Boolean) {
        dataStore.edit {
            it[IS_BIOMETRIC_AUTH_ENABLED] = enabled
            if (enabled) {
                it[BIOMETRIC_AUTH_PROMPT_SHOWN] = true
            }
        }
    }

    override suspend fun getBiometricCredentials(): Pair<String?, String?> = withContext(kotlinx.coroutines.Dispatchers.IO) {
        Pair(encryptedPrefs.getString(KEY_USER_EMAIL, null), encryptedPrefs.getString(KEY_USER_PASS, null))
    }

    override suspend fun saveBiometricCredentials(email: String, pass: String) {
        withContext(kotlinx.coroutines.Dispatchers.IO) {
            encryptedPrefs.edit()
                .putString(KEY_USER_EMAIL, email)
                .putString(KEY_USER_PASS, pass)
                .apply()
        }
        dataStore.edit {
            it[IS_BIOMETRIC_AUTH_ENABLED] = true
            it[BIOMETRIC_AUTH_PROMPT_SHOWN] = true
        }
    }

    override suspend fun markBiometricPromptShown() {
        dataStore.edit {
            it[BIOMETRIC_AUTH_PROMPT_SHOWN] = true
            it[IS_BIOMETRIC_AUTH_ENABLED] = false
        }
    }

    override suspend fun clearBiometricCredentials() {
        withContext(kotlinx.coroutines.Dispatchers.IO) {
            encryptedPrefs.edit()
                .remove(KEY_USER_EMAIL)
                .remove(KEY_USER_PASS)
                .apply()
        }
    }

    override suspend fun hasBiometricCredentials(): Boolean = withContext(kotlinx.coroutines.Dispatchers.IO) {
        val email = encryptedPrefs.getString(KEY_USER_EMAIL, null)
        val pass = encryptedPrefs.getString(KEY_USER_PASS, null)
        !email.isNullOrBlank() && !pass.isNullOrBlank()
    }
}
