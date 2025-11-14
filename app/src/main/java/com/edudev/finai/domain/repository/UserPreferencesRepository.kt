package com.edudev.finai.domain.repository

import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {
    val isDarkTheme: Flow<Boolean>
    suspend fun setDarkTheme(isDarkTheme: Boolean)
}