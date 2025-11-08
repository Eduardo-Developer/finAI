package com.edudev.finai.data.repository

import com.edudev.finai.data.local.dao.TransactionDao
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
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class TransactionRepositoryImpl @Inject constructor(
    private val transactionDao: TransactionDao
) : TransactionRepository {

    override fun getAllTransactions(userId: String): Flow<List<Transaction>> {
        return transactionDao.getAllTransactions(userId = userId).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getTransactionById(userId: String, id: Long): Transaction? {
        return transactionDao.getTransactionById(userId, id)?.toDomain()
    }

    override suspend fun getAllTransactionsForExport(userId: String): Result<String> {
        return try {
            val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())

            val transactions = transactionDao.getAllTransactionsOnce(userId)
            if (transactions.isEmpty()) {
                return Result.failure(IOException("Nenhuma transação para exportar."))
            }

            val csvBuilder = StringBuilder()
            csvBuilder.append("Data,Valor,Tipo,Descrição\n")

            // 2. Linhas de dados
            transactions.forEach { transaction ->
                val type = if (transaction.type == TransactionType.INCOME) "Receita" else "Despesa"
                // Formata a data e o valor para o padrão local
                val date = dateFormat.format(transaction.date)
                val amount = String.format(Locale.getDefault(), "%.2f", transaction.amount)

                csvBuilder.append("$date,$amount,$type,\"${transaction.description}\"\n")
            }

            Result.success(csvBuilder.toString())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getTransactionsByType(userId: String, type: TransactionType): Flow<List<Transaction>> {
        return transactionDao.getTransactionsByType(userId, type).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getTransactionsByCategory(userId: String, category: String): Flow<List<Transaction>> {
        return transactionDao.getTransactionsByCategory(userId, category).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getTransactionsByDateRange(
        userId: String,
        startDate: Date,
        endDate: Date
    ): Flow<List<Transaction>> {
        return transactionDao.getTransactionsByDateRange(userId,startDate, endDate).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun insertTransaction(transaction: Transaction): Long {
        return transactionDao.insertTransaction(transaction.toEntity())
    }

    override suspend fun updateTransaction(transaction: Transaction) {
        transactionDao.updateTransaction(transaction.toEntity())
    }

    override suspend fun deleteTransaction(userId: String, id: Long) {
        transactionDao.deleteTransaction(userId, id)
    }

    override suspend fun getTotalByTypeAndDateRange(
        userId: String,
        type: TransactionType,
        startDate: Date,
        endDate: Date
    ): Double {
        return transactionDao.getTotalByTypeAndDateRange(userId, type, startDate, endDate) ?: 0.0
    }

    override suspend fun getCategorySpendings(
        userId: String,
        type: TransactionType,
        startDate: Date,
        endDate: Date
    ): List<CategorySpending> {
        val results = transactionDao.getCategorySpendings(userId,type, startDate, endDate)
        val total = results.sumOf { it.total }

        return results.map { result ->
            CategorySpending(
                category = result.category,
                total = result.total,
                percentage = if (total > 0) ((result.total / total) * 100).toFloat() else 0f
            )
        }
    }

    override suspend fun getDashboardData(userId: String): DashboardData {
        val calendar = Calendar.getInstance()
        val endDate = calendar.time

        calendar.set(Calendar.DAY_OF_MONTH, 1)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val startDate = calendar.time

        val monthlyIncome = getTotalByTypeAndDateRange(userId,TransactionType.INCOME, startDate, endDate)
        val monthlyExpense = getTotalByTypeAndDateRange(userId,TransactionType.EXPENSE, startDate, endDate)
        val totalBalance = monthlyIncome - monthlyExpense

        val categorySpendings = getCategorySpendings(userId,TransactionType.EXPENSE, startDate, endDate)

        // Generate monthly chart data (last 6 months)
        val monthlyChartData = generateMonthlyChartData(userId = userId)

        return DashboardData(
            totalBalance = totalBalance,
            monthlyIncome = monthlyIncome,
            monthlyExpense = monthlyExpense,
            categorySpendings = categorySpendings,
            aiInsights = emptyList(), // Will be populated by ViewModel
            monthlyChartData = monthlyChartData
        )
    }

    private suspend fun generateMonthlyChartData(userId: String): List<MonthlyChartData> {
        val calendar = Calendar.getInstance()
        val months = mutableListOf<MonthlyChartData>()
        val monthNames = arrayOf(
            "Jan",
            "Fev",
            "Mar",
            "Abr",
            "Mai",
            "Jun",
            "Jul",
            "Ago",
            "Set",
            "Out",
            "Nov",
            "Dez"
        )

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

            val income = getTotalByTypeAndDateRange(userId,TransactionType.INCOME, monthStart, monthEnd)
            val expense = getTotalByTypeAndDateRange(userId,TransactionType.EXPENSE, monthStart, monthEnd)

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
