package com.edudev.finai.domain.usecase

import com.edudev.finai.domain.repository.TransactionRepository
import javax.inject.Inject

class ExportTransactionsToCsvUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
) {
    suspend operator fun invoke(): Result<String> {
        return transactionRepository.getAllTransactionsForExport()
    }
}
