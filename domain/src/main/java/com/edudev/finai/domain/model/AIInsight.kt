package com.edudev.finai.domain.model

data class AIInsight(
    val message: String,
    val type: InsightType,
    val savingsSuggestion: Double? = null
)

enum class InsightType {
    WARNING,
    SUGGESTION,
    POSITIVE
}
