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
import com.edudev.finai.domain.usecase.GetTransactionsByDateRangeUseCase
import com.edudev.finai.domain.usecase.GetUserDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val getDashboardDataUseCase: GetDashboardDataUseCase,
    private val getFinancialInsightsUseCase: GetFinancialInsightsUseCase,
    private val getAllTransactionsUseCase: GetAllTransactionsUseCase,
    private val getTransactionsByDateRangeUseCase: GetTransactionsByDateRangeUseCase,
    private val getUserDataUseCase: GetUserDataUseCase,
    private val preferencesRepository: PreferencesRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()
    val currentUserId = authRepository.currentUser ?: ""
    val isAIEnabled = preferencesRepository.isAIEnabled

    private val dateFilterFlow = MutableStateFlow<Pair<Date?, Date?>>(Pair(null, null))

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
            _uiState.value = _uiState.value.copy(isLoading = true, isLoadingAI = true)
            try {
                val filter = dateFilterFlow.value
                val dashboardData = getDashboardDataUseCase(currentUserId, filter.first, filter.second)
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

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun observeTransactions() {
        viewModelScope.launch {
            combine(
                dateFilterFlow.flatMapLatest { (start, end) ->
                    if (start != null && end != null) {
                        getTransactionsByDateRangeUseCase(currentUserId, start, end)
                    } else {
                        getAllTransactionsUseCase(currentUserId)
                    }
                },
                isAIEnabled
            ) { transactions, aiEnabled ->
                Pair(transactions, aiEnabled)
            }.collect { (transactions, aiEnabled) ->
                try {
                    val filter = dateFilterFlow.value
                    val dashboardData = getDashboardDataUseCase(currentUserId, filter.first, filter.second)
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
                    val (start, end) = dateFilterFlow.value
                    val transactions = if (start != null && end != null) {
                        getTransactionsByDateRangeUseCase(currentUserId, start, end).first()
                    } else {
                        getAllTransactionsUseCase(currentUserId).first()
                    }
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

    fun onDateFilterChanged(startDate: Date?, endDate: Date?) {
        dateFilterFlow.value = Pair(startDate, endDate)
        _uiState.value = _uiState.value.copy(
            filterStartDate = startDate,
            filterEndDate = endDate
        )
        refresh()
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
    val userImage: String? = null,
    val filterStartDate: Date? = null,
    val filterEndDate: Date? = null
)
