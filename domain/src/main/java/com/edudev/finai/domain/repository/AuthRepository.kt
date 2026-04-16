package com.edudev.finai.domain.repository

import com.edudev.finai.domain.model.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val currentUser: String?
    suspend fun login(email: String, pass: String): Result<Unit>
    suspend fun signUp(
        fullName: String,
        email: String,
        pass: String,
        imageBase64: String?
    ): Result<Unit>
    fun logout()
    fun isUserLoggedIn(): Boolean
    suspend fun getUserData(userId: String): Flow<User?>
}
