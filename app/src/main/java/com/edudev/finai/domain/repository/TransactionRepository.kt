package com.edudev.finai.domain.repository

import com.edudev.finai.domain.model.CategorySpending
import com.edudev.finai.domain.model.DashboardData
import com.edudev.finai.domain.model.Transaction
import com.edudev.finai.domain.model.TransactionType
import kotlinx.coroutines.flow.Flow
import java.util.Date

interface TransactionRepository {
    fun getAllTransactions(userId: String): Flow<List<Transaction>>
    suspend fun getTransactionById(userId: String, id: Long): Transaction?

    suspend fun getAllTransactionsForExport(userId: String): Result<String>
    fun getTransactionsByType(userId: String, type: TransactionType): Flow<List<Transaction>>
    fun getTransactionsByCategory(userId: String, category: String): Flow<List<Transaction>>
    fun getTransactionsByDateRange(userId: String, startDate: Date, endDate: Date): Flow<List<Transaction>>
    suspend fun insertTransaction(transaction: Transaction): Long
    suspend fun updateTransaction(transaction: Transaction)
    suspend fun deleteTransaction(userId: String, id: Long)
    suspend fun getTotalByTypeAndDateRange(
        userId: String,
        type: TransactionType,
        startDate: Date,
        endDate: Date
    ): Double
    suspend fun getCategorySpendings(
        userId: String,
        type: TransactionType,
        startDate: Date,
        endDate: Date
    ): List<CategorySpending>
    suspend fun getDashboardData(userId: String): DashboardData
}
