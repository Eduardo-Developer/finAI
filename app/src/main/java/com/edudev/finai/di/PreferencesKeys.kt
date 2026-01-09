package com.edudev.finai.di

import androidx.datastore.preferences.core.booleanPreferencesKey

object PreferencesKeys {
    val IS_BIOMETRIC_AUTH_ENABLED = booleanPreferencesKey("is_biometric_auth_enabled")
    val BIOMETRIC_AUTH_PROMPT_SHOWN = booleanPreferencesKey("biometric_auth_prompt_shown")
    
    const val KEY_USER_EMAIL = "biometric_user_email"
    const val KEY_USER_PASS = "biometric_user_pass"
}
