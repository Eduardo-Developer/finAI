
package com.edudev.finai.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.edudev.finai.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserPreferencesRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : UserPreferencesRepository {

    private object PreferencesKeys {
        val IS_DARK_THEME = booleanPreferencesKey("is_dark_theme")
    }

    override val isDarkTheme: Flow<Boolean> = dataStore.data.map {
        it[PreferencesKeys.IS_DARK_THEME] ?: false
    }

    override suspend fun setDarkTheme(isDarkTheme: Boolean) {
        dataStore.edit {
            it[PreferencesKeys.IS_DARK_THEME] = isDarkTheme
        }
    }
}
