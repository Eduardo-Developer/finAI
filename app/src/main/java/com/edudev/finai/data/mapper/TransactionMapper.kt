package com.edudev.finai.data.mapper

import com.edudev.finai.data.local.entity.TransactionEntity
import com.edudev.finai.data.remote.dto.InsightDto
import com.edudev.finai.data.remote.dto.TransactionData
import com.edudev.finai.domain.model.AIInsight
import com.edudev.finai.domain.model.InsightType
import com.edudev.finai.domain.model.Transaction
import java.text.SimpleDateFormat
import java.util.Locale

fun TransactionEntity.toDomain(): Transaction {
    return Transaction(
        id = id,
        userId = userId,
        amount = amount,
        category = category,
        description = description,
        type = type,
        date = date
    )
}

fun Transaction.toEntity(): TransactionEntity {
    return TransactionEntity(
        id = id ?: 0,
        userId = userId,
        amount = amount,
        category = category,
        description = description,
        type = type,
        date = date
    )
}

fun Transaction.toDto(): TransactionData {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return TransactionData(
        amount = amount,
        category = category,
        type = type.name,
        date = dateFormat.format(date)
    )
}

fun InsightDto.toDomain(): AIInsight {
    return AIInsight(
        message = message,
        type = when (type.lowercase()) {
            "warning" -> InsightType.WARNING
            "suggestion" -> InsightType.SUGGESTION
            else -> InsightType.POSITIVE
        }
    )
}
