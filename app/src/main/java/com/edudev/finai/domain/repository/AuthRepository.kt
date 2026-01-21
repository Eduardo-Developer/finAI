package com.edudev.finai.domain.repository

import android.net.Uri
import com.edudev.finai.domain.model.User
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val currentUser: String?
    suspend fun login(email: String, pass: String): Result<Unit>
    suspend fun signUp(
        fullName: String,
        email: String,
        pass: String,
        imageUri: Uri?
    ): AuthResult
    fun logout()
    fun getCurrentUser(): FirebaseUser?
    suspend fun getUserData(userId: String): Flow<User?>
}