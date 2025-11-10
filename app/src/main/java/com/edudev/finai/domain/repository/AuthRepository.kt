package com.edudev.finai.domain.repository

import android.content.ContentResolver
import android.net.Uri
import android.util.Base64
import com.edudev.finai.domain.model.User
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream
import javax.inject.Inject

interface AuthRepository {
    val currentUser: String?
    suspend fun login(email: String, pass: String): AuthResult
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

class AuthRepositoryImpl @Inject constructor(
    private val auth: com.google.firebase.auth.FirebaseAuth,
    private val database: FirebaseDatabase,
    private val contentResolver: ContentResolver
) : AuthRepository {

    override val currentUser: String?
        get() = auth.currentUser?.uid

    override suspend fun login(email: String, pass: String): AuthResult {
        return auth.signInWithEmailAndPassword(email, pass).await()
    }

    override suspend fun signUp(fullName: String, email: String, pass: String, imageUri: Uri?): AuthResult {
        val authResult = auth.createUserWithEmailAndPassword(email, pass).await()
        val user = authResult.user

        if (user != null) {
            var imageBase64: String? = null
            if (imageUri != null) {
                try {
                    val inputStream = contentResolver.openInputStream(imageUri)
                    val byteArrayOutputStream = ByteArrayOutputStream()
                    inputStream?.use { input ->
                        input.copyTo(byteArrayOutputStream)
                    }
                    val imageBytes = byteArrayOutputStream.toByteArray()
                    imageBase64 = Base64.encodeToString(imageBytes, Base64.DEFAULT)
                } catch (e: Exception) {
                    // Handle image conversion error
                }
            }

            val userData = mapOf(
                "uid" to user.uid,
                "fullName" to fullName,
                "email" to email,
                "imageUrl" to imageBase64
            )
            database.getReference("users").child(user.uid).setValue(userData).await()
        } else {
            throw IllegalStateException("User creation failed, user is null.")
        }

        return authResult
    }


    override fun logout() {
        auth.signOut()
    }

    override fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }

    override suspend fun getUserData(userId: String): Flow<User?> = callbackFlow {
        val userRef = database.getReference("users").child(userId)

        val listener = object : ValueEventListener {
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
}
