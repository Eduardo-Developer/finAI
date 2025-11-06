package com.edudev.finai.domain.repository

import kotlinx.coroutines.flow.Flow

interface PreferencesRepository {
    val isAIEnabled: Flow<Boolean>
    val isDarkTheme: Flow<Boolean>
    suspend fun setAIEnabled(enabled: Boolean)
    suspend fun setDarkTheme(enabled: Boolean)
}
