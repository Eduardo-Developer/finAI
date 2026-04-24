package com.edudev.finai.data.repository

import com.edudev.finai.domain.model.User
import com.edudev.finai.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class AuthRepositoryImpl
@Inject
constructor(
    private val auth: FirebaseAuth,
    private val database: FirebaseDatabase
) : AuthRepository {
    override val currentUser: String?
        get() = auth.currentUser?.uid

    override suspend fun login(email: String, pass: String): Result<Unit> = withContext(Dispatchers.IO) {
        val authResult = auth.signInWithEmailAndPassword(email, pass).await()

        if (authResult.user != null) {
            Result.success(Unit)
        } else {
            Result.failure(Exception("Login Falhou Inesperadamente"))
        }
    }

    override suspend fun signUp(fullName: String, email: String, pass: String, imageBase64: String?): Result<Unit> =
        withContext(Dispatchers.IO) {
            val authResult = auth.createUserWithEmailAndPassword(email, pass).await()
            val user = authResult.user

            if (user != null) {
                val userData =
                    mapOf(
                        "uid" to user.uid,
                        "fullName" to fullName,
                        "email" to email,
                        "imageUrl" to imageBase64
                    )
                database.getReference("users").child(user.uid).setValue(userData).await()
            } else {
                throw IllegalStateException("User creation failed, user is null.")
            }

            Result.success(Unit)
        }

    override fun logout() {
        auth.signOut()
    }

    override fun isUserLoggedIn(): Boolean {
        return auth.currentUser != null
    }

    override suspend fun getUserData(userId: String): Flow<User?> = callbackFlow {
        val userRef = database.getReference("users").child(userId)

        val listener =
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val user = snapshot.getValue(User::class.java)
                    trySend(user)
                }

                override fun onCancelled(error: DatabaseError) {
                    close(error.toException())
                }
            }

        userRef.addValueEventListener(listener)

        awaitClose { userRef.removeEventListener(listener) }
    }

    override suspend fun updateUserProfile(fullName: String, phoneNumber: String, imageBase64: String?): Result<Unit> =
        withContext(Dispatchers.IO) {
            try {
                val userId = auth.currentUser?.uid ?: return@withContext Result.failure(Exception("Usuário não logado"))
                val updates =
                    mutableMapOf<String, Any>(
                        "fullName" to fullName,
                        "phoneNumber" to phoneNumber
                    )
                imageBase64?.let { updates["imageUrl"] = it }

                database.getReference("users").child(userId).updateChildren(updates).await()
                Result.success(Unit)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
}
