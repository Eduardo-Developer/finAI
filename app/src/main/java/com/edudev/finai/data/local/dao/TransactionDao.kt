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
    @Query("SELECT * FROM transactions ORDER BY date DESC")
    fun getAllTransactions(): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions ORDER BY date DESC")
    suspend fun getAllTransactionsOnce(): List<TransactionEntity>

    @Query("SELECT * FROM transactions WHERE id = :id")
    suspend fun getTransactionById(id: Long): TransactionEntity?

    @Query("SELECT * FROM transactions WHERE type = :type ORDER BY date DESC")
    fun getTransactionsByType(type: TransactionType): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions WHERE category = :category ORDER BY date DESC")
    fun getTransactionsByCategory(category: String): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions WHERE date BETWEEN :startDate AND :endDate ORDER BY date DESC")
    fun getTransactionsByDateRange(startDate: Date, endDate: Date): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions WHERE type = :type AND date BETWEEN :startDate AND :endDate")
    suspend fun getTransactionsByTypeAndDateRange(
        type: TransactionType,
        startDate: Date,
        endDate: Date
    ): List<TransactionEntity>

    @Query("SELECT SUM(amount) FROM transactions WHERE type = :type AND date BETWEEN :startDate AND :endDate")
    suspend fun getTotalByTypeAndDateRange(
        type: TransactionType,
        startDate: Date,
        endDate: Date
    ): Double?

    @Query("SELECT category, SUM(amount) as total FROM transactions WHERE type = :type AND date BETWEEN :startDate AND :endDate GROUP BY category")
    suspend fun getCategorySpendings(
        type: TransactionType,
        startDate: Date,
        endDate: Date
    ): List<CategorySpendingResult>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: TransactionEntity): Long

    @Update
    suspend fun updateTransaction(transaction: TransactionEntity)

    @Query("DELETE FROM transactions WHERE id = :id")
    suspend fun deleteTransaction(id: Long)

    @Query("DELETE FROM transactions")
    suspend fun deleteAllTransactions()
}

data class CategorySpendingResult(
    val category: String,
    val total: Double
)
