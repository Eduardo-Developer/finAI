package com.edudev.finai.data.remote.dto

import com.squareup.moshi.Json

data class AIInsightResponse(
    @Json(name = "insights")
    val insights: List<InsightDto>,
    @Json(name = "savings_suggestion")
    val savingsSuggestion: Double?
)

data class InsightDto(
    @Json(name = "message")
    val message: String,
    @Json(name = "type")
    val type: String
)
