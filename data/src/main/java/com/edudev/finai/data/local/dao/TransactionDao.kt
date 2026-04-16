package com.edudev.finai.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.edudev.finai.data.local.entity.TransactionEntity
import com.edudev.finai.domain.model.TransactionType
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface TransactionDao {
    @Query("SELECT * FROM transactions WHERE userId = :userId ORDER BY date DESC")
    fun getAllTransactions(
        userId: String
    ): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions WHERE userId = :userId ORDER BY date DESC")
    suspend fun getAllTransactionsOnce(
        userId: String
    ): List<TransactionEntity>

    @Query("SELECT * FROM transactions WHERE userId = :userId AND id = :id")
    suspend fun getTransactionById(
        userId: String,
        id: Long
    ): TransactionEntity?

    @Query("SELECT * FROM transactions WHERE userId = :userId AND type = :type ORDER BY date DESC")
    fun getTransactionsByType(
        userId: String,
        type: TransactionType
    ): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions WHERE userId = :userId AND category = :category ORDER BY date DESC")
    fun getTransactionsByCategory(
        userId: String,category: String
    ): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions WHERE userId = :userId AND date BETWEEN :startDate AND :endDate ORDER BY date DESC")
    fun getTransactionsByDateRange(
        userId: String,
        startDate: Date, endDate: Date
    ): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions WHERE userId = :userId AND type = :type AND date BETWEEN :startDate AND :endDate")
    suspend fun getTransactionsByTypeAndDateRange(
        userId: String,
        type: TransactionType,
        startDate: Date,
        endDate: Date
    ): List<TransactionEntity>

    @Query("SELECT SUM(amount) FROM transactions WHERE userId = :userId AND type = :type AND date BETWEEN :startDate AND :endDate")
    suspend fun getTotalByTypeAndDateRange(
        userId: String,
        type: TransactionType,
        startDate: Date,
        endDate: Date
    ): Double?

    @Query("SELECT category, SUM(amount) as total FROM transactions WHERE userId = :userId AND type = :type AND date BETWEEN :startDate AND :endDate GROUP BY category")
    suspend fun getCategorySpendings(
        userId: String,
        type: TransactionType,
        startDate: Date,
        endDate: Date
    ): List<CategorySpendingResult>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: TransactionEntity): Long

    @Update
    suspend fun updateTransaction(transaction: TransactionEntity)

    @Query("DELETE FROM transactions WHERE userId = :userId AND id = :id")
    suspend fun deleteTransaction(
        userId: String,
        id: Long)

    @Query("DELETE FROM transactions")
    suspend fun deleteAllTransactions()
}

data class CategorySpendingResult(
    val category: String,
    val total: Double
)
