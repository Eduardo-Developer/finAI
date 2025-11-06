package com.edudev.finai.domain.repository

import com.edudev.finai.domain.model.CategorySpending
import com.edudev.finai.domain.model.DashboardData
import com.edudev.finai.domain.model.Transaction
import com.edudev.finai.domain.model.TransactionType
import kotlinx.coroutines.flow.Flow
import java.util.Date

interface TransactionRepository {
    fun getAllTransactions(): Flow<List<Transaction>>
    suspend fun getTransactionById(id: Long): Transaction?
    fun getTransactionsByType(type: TransactionType): Flow<List<Transaction>>
    fun getTransactionsByCategory(category: String): Flow<List<Transaction>>
    fun getTransactionsByDateRange(startDate: Date, endDate: Date): Flow<List<Transaction>>
    suspend fun insertTransaction(transaction: Transaction): Long
    suspend fun updateTransaction(transaction: Transaction)
    suspend fun deleteTransaction(id: Long)
    suspend fun getTotalByTypeAndDateRange(
        type: TransactionType,
        startDate: Date,
        endDate: Date
    ): Double
    suspend fun getCategorySpendings(
        type: TransactionType,
        startDate: Date,
        endDate: Date
    ): List<CategorySpending>
    suspend fun getDashboardData(): DashboardData
}
