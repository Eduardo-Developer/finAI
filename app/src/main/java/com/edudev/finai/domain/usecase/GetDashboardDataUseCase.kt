package com.edudev.finai.domain.usecase

import com.edudev.finai.domain.model.DashboardData
import com.edudev.finai.domain.repository.TransactionRepository
import javax.inject.Inject

class GetDashboardDataUseCase @Inject constructor(
    private val repository: TransactionRepository
) {
    suspend operator fun invoke(): DashboardData = repository.getDashboardData()
}
