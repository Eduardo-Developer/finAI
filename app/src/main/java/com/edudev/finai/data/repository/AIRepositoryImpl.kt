package com.edudev.finai.data.repository

import com.edudev.finai.data.mapper.toDto
import com.edudev.finai.data.mapper.toDomain
import com.edudev.finai.data.remote.api.AIApi
import com.edudev.finai.domain.model.AIInsight
import com.edudev.finai.domain.model.InsightType
import com.edudev.finai.domain.model.Transaction
import com.edudev.finai.domain.repository.AIRepository
import javax.inject.Inject

class AIRepositoryImpl @Inject constructor(
    private val aiApi: AIApi
) : AIRepository {

    override suspend fun getFinancialInsights(
        transactions: List<Transaction>,
        periodDays: Int
    ): List<AIInsight> {
        return try {
            val request = com.edudev.finai.data.remote.dto.AIInsightRequest(
                transactions = transactions.map { it.toDto() },
                periodDays = periodDays
            )
            
            val response = aiApi.getFinancialInsights(request)
            response.insights.map { it.toDomain() } + listOfNotNull(
                response.savingsSuggestion?.let { savings ->
                    AIInsight(
                        message = "Você poderia economizar R$ ${String.format("%.2f", savings)} reduzindo gastos desnecessários.",
                        type = InsightType.SUGGESTION,
                        savingsSuggestion = savings
                    )
                }
            )
        } catch (e: Exception) {
            // Fallback para análise local simples se API falhar
            generateLocalInsights(transactions)
        }
    }

    private fun generateLocalInsights(transactions: List<Transaction>): List<AIInsight> {
        val insights = mutableListOf<AIInsight>()
        
        val expenses = transactions.filter { it.type == com.edudev.finai.domain.model.TransactionType.EXPENSE }
        val totalExpense = expenses.sumOf { it.amount }
        
        if (totalExpense > 0) {
            val categoryExpenses = expenses.groupBy { it.category }
                .mapValues { it.value.sumOf { t -> t.amount } }
            
            val maxCategory = categoryExpenses.maxByOrNull { it.value }
            maxCategory?.let { (category, amount) ->
                val percentage = (amount / totalExpense * 100).toInt()
                if (percentage > 30) {
                    insights.add(
                        AIInsight(
                            message = "Você gastou $percentage% dos seus gastos em $category. Considere reduzir.",
                            type = InsightType.WARNING
                        )
                    )
                }
            }
        }
        
        return insights.ifEmpty {
            listOf(
                AIInsight(
                    message = "Continue registrando suas transações para receber insights mais precisos!",
                    type = InsightType.POSITIVE
                )
            )
        }
    }
}

