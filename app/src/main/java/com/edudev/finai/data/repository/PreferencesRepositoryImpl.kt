package com.edudev.finai.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.edudev.finai.domain.repository.PreferencesRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class PreferencesRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : PreferencesRepository {

    private val dataStore = context.dataStore

    companion object {
        private val AI_ENABLED_KEY = booleanPreferencesKey("ai_enabled")
        private val DARK_THEME_KEY = booleanPreferencesKey("dark_theme")
    }

    override val isAIEnabled: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[AI_ENABLED_KEY] ?: true
    }

    override val isDarkTheme: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[DARK_THEME_KEY] ?: false
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
}
