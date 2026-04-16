package com.edudev.finai.domain.usecase

import com.edudev.finai.domain.repository.TransactionRepository
import javax.inject.Inject

class DeleteTransactionUseCase @Inject constructor(
    private val repository: TransactionRepository
) {
    suspend operator fun invoke(userId: String, id: Long) {
        repository.deleteTransaction(userId,id)
    }
}
