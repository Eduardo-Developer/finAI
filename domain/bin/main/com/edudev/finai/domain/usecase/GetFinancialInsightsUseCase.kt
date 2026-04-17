package com.edudev.finai.domain.usecase

import com.edudev.finai.domain.model.AIInsight
import com.edudev.finai.domain.model.Transaction
import com.edudev.finai.domain.repository.AIRepository
import javax.inject.Inject

class GetFinancialInsightsUseCase @Inject constructor(
    private val repository: AIRepository
) {
    suspend operator fun invoke(
        transactions: List<Transaction>,
        periodDays: Int = 30
    ): List<AIInsight> {
        return repository.getFinancialInsights(transactions, periodDays)
    }
}
