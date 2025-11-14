package com.edudev.finai.di

import androidx.datastore.preferences.core.booleanPreferencesKey

object PreferencesKeys {
    val IS_BIOMETRIC_AUTH_ENABLED = booleanPreferencesKey("is_biometric_auth_enabled")
    val BIOMETRIC_AUTH_PROMPT_SHOWN = booleanPreferencesKey("biometric_auth_prompt_shown")
}
