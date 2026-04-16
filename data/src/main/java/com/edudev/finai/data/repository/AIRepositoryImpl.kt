package com.edudev.finai.data.repository

import com.edudev.finai.data.remote.dto.GeminiInsightResponse
import com.edudev.finai.domain.model.AIInsight
import com.edudev.finai.domain.model.InsightType
import com.edudev.finai.domain.model.Transaction
import com.edudev.finai.domain.model.TransactionType
import com.edudev.finai.domain.repository.AIRepository
import com.google.ai.client.generativeai.GenerativeModel
import com.squareup.moshi.Moshi
import javax.inject.Inject

class AIRepositoryImpl @Inject constructor(
    private val generativeModel: GenerativeModel,
    private val moshi: Moshi
) : AIRepository {

    override suspend fun getFinancialInsights(
        transactions: List<Transaction>,
        periodDays: Int
    ): List<AIInsight> {
        return try {
            // 1. Otimização de Tokens: Não envie o DTO completo, apenas o essencial
            val transactionsSummary = transactions.joinToString("\n") {
                "${it.date}: ${it.category} - R$ ${it.amount} (${it.type})"
            }
            val prompt = """
                Analise estas transações dos últimos $periodDays dias e forneça insights.
                Retorne estritamente um JSON puro, sem markdown:
                {
                  "insights": [
                    { "message": "string", "type": "warning|suggestion|positive" }
                  ],
                  "savings_suggestion": double
                }
                
                Transações:
                $transactionsSummary
            """.trimIndent()

            val response = generativeModel.generateContent(prompt)
            val responseText = response.text ?: throw Exception("Resposta da IA vazia")

            val jsonString = responseText
                .replace("```json", "")
                .replace("```", "")
                .trim()

            val adapter = moshi.adapter(GeminiInsightResponse::class.java)
            val result = adapter.fromJson(jsonString)

            val insights = result?.insights?.map { dto ->
                AIInsight(
                    message = dto.message,
                    type = when (dto.type.lowercase()) {
                        "warning" -> InsightType.WARNING
                        "suggestion" -> InsightType.SUGGESTION
                        else -> InsightType.POSITIVE
                    }
                )
            } ?: emptyList()

            insights + listOfNotNull(
                result?.savingsSuggestion?.takeIf { it > 0 }?.let { savings ->
                    AIInsight(
                        message = "Potencial de economia: R$ ${String.format("%.2f", savings)}",
                        type = InsightType.SUGGESTION,
                        savingsSuggestion = savings
                    )
                }
            )
        } catch (e: Exception) {
            println("GEMINI_ERROR: ${e.message}")
            generateLocalInsights(transactions)
        }
    }

    private fun generateLocalInsights(transactions: List<Transaction>): List<AIInsight> {
        val insights = mutableListOf<AIInsight>()
        val expenses = transactions.filter { it.type == TransactionType.EXPENSE }
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
