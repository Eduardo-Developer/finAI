package com.edudev.finai.domain.usecase

import com.edudev.finai.domain.model.Transaction
import com.edudev.finai.domain.repository.TransactionRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetAllTransactionsUseCase
@Inject
constructor(
    private val repository: TransactionRepository
) {
    operator fun invoke(userId: String): Flow<List<Transaction>> = repository.getAllTransactions(userId = userId)
}
