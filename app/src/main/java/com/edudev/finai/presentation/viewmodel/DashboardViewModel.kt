package com.edudev.finai.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edudev.finai.domain.model.AIInsight
import com.edudev.finai.domain.model.DashboardData
import com.edudev.finai.domain.model.Transaction
import com.edudev.finai.domain.repository.AuthRepository
import com.edudev.finai.domain.repository.PreferencesRepository
import com.edudev.finai.domain.usecase.GetAllTransactionsUseCase
import com.edudev.finai.domain.usecase.GetDashboardDataUseCase
import com.edudev.finai.domain.usecase.GetFinancialInsightsUseCase
import com.edudev.finai.domain.usecase.GetUserDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val getDashboardDataUseCase: GetDashboardDataUseCase,
    private val getFinancialInsightsUseCase: GetFinancialInsightsUseCase,
    private val getAllTransactionsUseCase: GetAllTransactionsUseCase,
    private val getUserDataUseCase: GetUserDataUseCase,
    private val preferencesRepository: PreferencesRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()
    val currentUserId = authRepository.getCurrentUser()?.uid ?: ""
    val isAIEnabled = preferencesRepository.isAIEnabled

    init {
        loadInitialData()
        observeTransactions()
    }

    private fun loadInitialData(){
        loadUserData()
        loadDashboardData()
    }

    private fun loadUserData(){
        viewModelScope.launch {
            getUserDataUseCase(currentUserId).collect{
                user ->
                _uiState.value = _uiState.value.copy(
                    userName = user?.fullName?.split(" ")?.firstOrNull() ?: "",
                    userImage = user?.imageUrl
                )
            }
        }
    }

    private fun loadDashboardData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            _uiState.value = _uiState.value.copy(isLoadingAI = true)
            try {
                val dashboardData = getDashboardDataUseCase(currentUserId)
                _uiState.value = _uiState.value.copy(
                    dashboardData = dashboardData,
                    isLoading = false,
                    isLoadingAI = false
                )
                loadAIInsights(dashboardData)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message,
                    isLoading = false,
                    isLoadingAI = false
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
                Pair(transactions, aiEnabled)
            }.collect { (transactions, aiEnabled) ->
                try {
                    val dashboardData = getDashboardDataUseCase(currentUserId)
                    _uiState.value = _uiState.value.copy(
                        dashboardData = dashboardData,
                        isLoading = false
                    )
                    if (aiEnabled) {
                        _uiState.value = _uiState.value.copy(isLoadingAI = true)
                        try {
                            val insights = getFinancialInsightsUseCase(transactions)
                            _uiState.value = _uiState.value.copy(
                                aiInsights = insights,
                                dashboardData = _uiState.value.dashboardData?.copy(aiInsights = insights),
                                isLoadingAI = false
                            )
                        } catch (e: Exception) {
                            _uiState.value = _uiState.value.copy(isLoadingAI = false)
                        }
                    }
                } catch (e: Exception) {
                    _uiState.value = _uiState.value.copy(isLoading = false)
                }
            }
        }
    }

    private fun loadAIInsights(dashboardData: DashboardData) {
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
                }
            }
        }
    }

    fun refresh() {
        viewModelScope.launch {
            loadDashboardData()
        }
    }
}

data class DashboardUiState(
    val dashboardData: DashboardData? = null,
    val transactions: List<Transaction> = emptyList(),
    val aiInsights: List<AIInsight> = emptyList(),
    val isLoading: Boolean = false,
    val isLoadingAI: Boolean = false,
    val error: String? = null,
    val userName: String = "",
    val userImage: String? = null
)
