package com.edudev.finai.data.remote.api

import com.edudev.finai.data.remote.dto.AIInsightRequest
import com.edudev.finai.data.remote.dto.AIInsightResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AIApi {
    @POST("analyze")
    suspend fun getFinancialInsights(
        @Body request: AIInsightRequest
    ): AIInsightResponse
}
