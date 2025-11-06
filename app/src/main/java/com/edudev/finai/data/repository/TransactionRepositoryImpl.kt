package com.edudev.finai.data.repository

import com.edudev.finai.data.local.dao.TransactionDao
import com.edudev.finai.data.local.entity.TransactionEntity
import com.edudev.finai.data.mapper.toDomain
import com.edudev.finai.data.mapper.toEntity
import com.edudev.finai.domain.model.CategorySpending
import com.edudev.finai.domain.model.DashboardData
import com.edudev.finai.domain.model.MonthlyChartData
import com.edudev.finai.domain.model.Transaction
import com.edudev.finai.domain.model.TransactionType
import com.edudev.finai.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

class TransactionRepositoryImpl @Inject constructor(
    private val transactionDao: TransactionDao
) : TransactionRepository {

    override fun getAllTransactions(): Flow<List<Transaction>> {
        return transactionDao.getAllTransactions().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getTransactionById(id: Long): Transaction? {
        return transactionDao.getTransactionById(id)?.toDomain()
    }

    override fun getTransactionsByType(type: TransactionType): Flow<List<Transaction>> {
        return transactionDao.getTransactionsByType(type).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getTransactionsByCategory(category: String): Flow<List<Transaction>> {
        return transactionDao.getTransactionsByCategory(category).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getTransactionsByDateRange(
        startDate: Date,
        endDate: Date
    ): Flow<List<Transaction>> {
        return transactionDao.getTransactionsByDateRange(startDate, endDate).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun insertTransaction(transaction: Transaction): Long {
        return transactionDao.insertTransaction(transaction.toEntity())
    }

    override suspend fun updateTransaction(transaction: Transaction) {
        transactionDao.updateTransaction(transaction.toEntity())
    }

    override suspend fun deleteTransaction(id: Long) {
        transactionDao.deleteTransaction(id)
    }

    override suspend fun getTotalByTypeAndDateRange(
        type: TransactionType,
        startDate: Date,
        endDate: Date
    ): Double {
        return transactionDao.getTotalByTypeAndDateRange(type, startDate, endDate) ?: 0.0
    }

    override suspend fun getCategorySpendings(
        type: TransactionType,
        startDate: Date,
        endDate: Date
    ): List<CategorySpending> {
        val results = transactionDao.getCategorySpendings(type, startDate, endDate)
        val total = results.sumOf { it.total }
        
        return results.map { result ->
            CategorySpending(
                category = result.category,
                total = result.total,
                percentage = if (total > 0) ((result.total / total) * 100).toFloat() else 0f
            )
        }
    }

    override suspend fun getDashboardData(): DashboardData {
        val calendar = Calendar.getInstance()
        val endDate = calendar.time
        
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val startDate = calendar.time
        
        val monthlyIncome = getTotalByTypeAndDateRange(TransactionType.INCOME, startDate, endDate)
        val monthlyExpense = getTotalByTypeAndDateRange(TransactionType.EXPENSE, startDate, endDate)
        val totalBalance = monthlyIncome - monthlyExpense
        
        val categorySpendings = getCategorySpendings(TransactionType.EXPENSE, startDate, endDate)
        
        // Generate monthly chart data (last 6 months)
        val monthlyChartData = generateMonthlyChartData()
        
        return DashboardData(
            totalBalance = totalBalance,
            monthlyIncome = monthlyIncome,
            monthlyExpense = monthlyExpense,
            categorySpendings = categorySpendings,
            aiInsights = emptyList(), // Will be populated by ViewModel
            monthlyChartData = monthlyChartData
        )
    }
    
    private suspend fun generateMonthlyChartData(): List<MonthlyChartData> {
        val calendar = Calendar.getInstance()
        val months = mutableListOf<MonthlyChartData>()
        val monthNames = arrayOf("Jan", "Fev", "Mar", "Abr", "Mai", "Jun", "Jul", "Ago", "Set", "Out", "Nov", "Dez")
        
        for (i in 5 downTo 0) {
            calendar.add(Calendar.MONTH, -i)
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            
            calendar.set(Calendar.DAY_OF_MONTH, 1)
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)
            val monthStart = calendar.time
            
            calendar.add(Calendar.MONTH, 1)
            calendar.add(Calendar.MILLISECOND, -1)
            val monthEnd = calendar.time
            
            val income = getTotalByTypeAndDateRange(TransactionType.INCOME, monthStart, monthEnd)
            val expense = getTotalByTypeAndDateRange(TransactionType.EXPENSE, monthStart, monthEnd)
            
            months.add(
                MonthlyChartData(
                    month = "${monthNames[month]}\n$year",
                    income = income,
                    expense = expense
                )
            )
            
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.YEAR, year)
        }
        
        return months.reversed()
    }
}
