package com.edudev.finai.domain.repository

import kotlinx.coroutines.flow.Flow

interface ThemeRepository {
    val isDarkTheme: Flow<Boolean>
    suspend fun setDarkTheme(isDarkTheme: Boolean)
}