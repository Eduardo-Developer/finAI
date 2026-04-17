package com.edudev.finai.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.edudev.finai.domain.repository.ThemeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ThemeRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : ThemeRepository {

    private object PreferencesKeys {
        val IS_DARK_THEME = booleanPreferencesKey("is_dark_theme")
    }

    override val isDarkTheme: Flow<Boolean> = dataStore.data.map {
        it[PreferencesKeys.IS_DARK_THEME] ?: false
    }

    override suspend fun setDarkTheme(isDarkTheme: Boolean) {
        withContext(Dispatchers.IO) {
            dataStore.edit {
                it[PreferencesKeys.IS_DARK_THEME] = isDarkTheme
            }
        }
    }
}
