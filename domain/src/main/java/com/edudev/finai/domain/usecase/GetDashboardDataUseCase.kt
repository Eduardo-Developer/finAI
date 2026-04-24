package com.edudev.finai.domain.usecase

import com.edudev.finai.domain.model.DashboardData
import com.edudev.finai.domain.repository.TransactionRepository
import java.util.Date
import javax.inject.Inject

class GetDashboardDataUseCase
@Inject
constructor(
    private val repository: TransactionRepository
) {
    suspend operator fun invoke(userId: String, startDate: Date? = null, endDate: Date? = null): DashboardData =
        repository.getDashboardData(userId, startDate, endDate)
}
