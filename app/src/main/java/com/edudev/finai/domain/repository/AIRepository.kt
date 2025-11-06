package com.edudev.finai.domain.repository

import com.edudev.finai.domain.model.AIInsight
import com.edudev.finai.domain.model.Transaction
import java.util.Date

interface AIRepository {
    suspend fun getFinancialInsights(
        transactions: List<Transaction>,
        periodDays: Int
    ): List<AIInsight>
}
