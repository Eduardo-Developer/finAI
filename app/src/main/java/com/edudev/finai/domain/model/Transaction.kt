package com.edudev.finai.domain.model

import java.util.Date

data class Transaction(
    val id: Long? = null,
    val amount: Double,
    val category: String,
    val description: String,
    val type: TransactionType,
    val date: Date
)

enum class TransactionType {
    INCOME, // Receita
    EXPENSE // Despesa
}
