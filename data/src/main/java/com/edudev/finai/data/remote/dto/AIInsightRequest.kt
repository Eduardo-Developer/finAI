package com.edudev.finai.data.remote.dto

import com.squareup.moshi.Json

data class AIInsightRequest(
    @Json(name = "transactions")
    val transactions: List<TransactionData>,
    @Json(name = "period_days")
    val periodDays: Int
)

data class TransactionData(
    @Json(name = "amount")
    val amount: Double,
    @Json(name = "category")
    val category: String,
    @Json(name = "type")
    val type: String,
    @Json(name = "date")
    val date: String
)
