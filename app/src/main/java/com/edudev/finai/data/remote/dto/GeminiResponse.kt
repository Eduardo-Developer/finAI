package com.edudev.finai.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GeminiInsightResponse(
    @Json(name = "insights")
    val insights: List<GeminiInsightDto>,
    @Json(name = "savings_suggestion")
    val savingsSuggestion: Double?
)

@JsonClass(generateAdapter = true)
data class GeminiInsightDto(
    @Json(name = "message")
    val message: String,
    @Json(name = "type")
    val type: String
)
