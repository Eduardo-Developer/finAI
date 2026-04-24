package com.edudev.finai.domain.usecase

import com.edudev.finai.domain.model.Transaction
import com.edudev.finai.domain.repository.TransactionRepository
import java.util.Date
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetTransactionsByDateRangeUseCase
@Inject
constructor(
    private val repository: TransactionRepository
) {
    operator fun invoke(userId: String, startDate: Date, endDate: Date): Flow<List<Transaction>> {
        return repository.getTransactionsByDateRange(userId, startDate, endDate)
    }
}
