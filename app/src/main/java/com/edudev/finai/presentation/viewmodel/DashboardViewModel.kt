package com.edudev.finai.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edudev.finai.data.repository.AuthRepository
import com.edudev.finai.domain.model.AIInsight
import com.edudev.finai.domain.model.DashboardData
import com.edudev.finai.domain.model.Transaction
import com.edudev.finai.domain.repository.PreferencesRepository
import com.edudev.finai.domain.usecase.GetDashboardDataUseCase
import com.edudev.finai.domain.usecase.GetFinancialInsightsUseCase
import com.edudev.finai.domain.usecase.GetAllTransactionsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val getDashboardDataUseCase: GetDashboardDataUseCase,
    private val getFinancialInsightsUseCase: GetFinancialInsightsUseCase,
    private val getAllTransactionsUseCase: GetAllTransactionsUseCase,
    private val preferencesRepository: PreferencesRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()
    val currentUserId = authRepository.getCurrentUser()?.uid ?: ""

    val isAIEnabled = preferencesRepository.isAIEnabled

    init {
        loadDashboardData()
        observeTransactions()
    }

    private fun loadDashboardData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val dashboardData = getDashboardDataUseCase(currentUserId)
                _uiState.value = _uiState.value.copy(
                    dashboardData = dashboardData,
                    isLoading = false
                )
                loadAIInsights(dashboardData)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message,
                    isLoading = false
                )
            }
        }
    }

    private fun observeTransactions() {
        viewModelScope.launch {
            combine(
                getAllTransactionsUseCase(currentUserId),
                isAIEnabled
            ) { transactions, aiEnabled ->
                // Atualiza os dados do dashboard quando há mudanças nas transações
                try {
                    val dashboardData = getDashboardDataUseCase(currentUserId)
                    var insights = emptyList<AIInsight>()
                    
                    if (aiEnabled) {
                        try {
                            insights = getFinancialInsightsUseCase(transactions)
                        } catch (e: Exception) {
                            // Ignore AI errors
                        }
                    }
                    
                    _uiState.value = _uiState.value.copy(
                        dashboardData = dashboardData.copy(aiInsights = insights),
                        aiInsights = insights,
                        isLoading = false
                    )
                } catch (e: Exception) {
                    // Ignore errors
                }
            }.collect()
        }
    }

    private suspend fun loadAIInsights(dashboardData: DashboardData) {
        viewModelScope.launch {
            if (isAIEnabled.first()) {
                try {
                    val transactions = getAllTransactionsUseCase(currentUserId).first()
                    val insights = getFinancialInsightsUseCase(transactions)
                    _uiState.value = _uiState.value.copy(
                        aiInsights = insights,
                        dashboardData = dashboardData.copy(aiInsights = insights)
                    )
                } catch (e: Exception) {
                    // Ignore AI errors, use fallback insights
                }
            }
        }
    }

    fun refresh() {
        loadDashboardData()
    }
}

data class DashboardUiState(
    val dashboardData: DashboardData? = null,
    val aiInsights: List<AIInsight> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
