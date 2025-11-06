package com.edudev.finai.data.repository

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

interface AuthRepository {
    suspend fun signUp(
        fullName: String,
        email: String,
        pass: String
    ): AuthResult

    suspend fun login(email: String, pass: String): AuthResult

    fun getCurrentUser(): FirebaseUser?
    
    fun logout()
}

class AuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : AuthRepository {
    override suspend fun signUp(
        fullName: String,
        email: String,
        pass: String
    ): AuthResult {
        val authResult = auth.createUserWithEmailAndPassword(email, pass).await()
        val user = authResult.user
        if (user != null) {
            val userData = hashMapOf(
                "uid" to user.uid,
                "fullName" to fullName,
                "email" to email
            )
            firestore.collection("users").document(user.uid).set(userData).await()
        }
        return authResult
    }

    override suspend fun login(email: String, pass: String): AuthResult {
        return auth.signInWithEmailAndPassword(email, pass).await()
    }

    override fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }
    
    override fun logout() {
        auth.signOut()
    }
}
