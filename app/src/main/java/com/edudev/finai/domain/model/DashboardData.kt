package com.edudev.finai.domain.model

data class DashboardData(
    val totalBalance: Double,
    val monthlyIncome: Double,
    val monthlyExpense: Double,
    val categorySpendings: List<CategorySpending>,
    val aiInsights: List<AIInsight>,
    val monthlyChartData: List<MonthlyChartData>
)

data class MonthlyChartData(
    val month: String,
    val income: Double,
    val expense: Double
)
