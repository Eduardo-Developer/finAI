package com.edudev.finai.domain.usecase

import com.edudev.finai.domain.model.User
import com.edudev.finai.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserDataUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(userId: String): Flow<User?> {
        return repository.getUserData(userId)
    }
}
