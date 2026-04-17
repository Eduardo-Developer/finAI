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
import androidx.lifecycle.SavedStateHandle
import com.edudev.finai.domain.usecase.GetUserDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
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
    private val authRepository: AuthRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState())
    private val _showDateRangePicker = MutableStateFlow(false)
    
    val uiState: StateFlow<DashboardUiState> = combine(
        _uiState,
        preferencesRepository.isAIEnabled,
        _showDateRangePicker
    ) { state, aiEnabled, showDatePicker ->
        state.copy(
            isAIEnabled = aiEnabled,
            showDateRangePicker = showDatePicker
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = DashboardUiState()
    )

    val currentUserId = authRepository.currentUser ?: ""

    private val filterStartDateFlow = savedStateHandle.getStateFlow<Long?>("start_date", null)
    private val filterEndDateFlow = savedStateHandle.getStateFlow<Long?>("end_date", null)

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
                val start = filterStartDateFlow.value?.let { Date(it) }
                val end = filterEndDateFlow.value?.let { Date(it) }
                val dashboardData = getDashboardDataUseCase(currentUserId, start, end)
                _uiState.value = _uiState.value.copy(
                    dashboardData = dashboardData,
                    filterStartDate = start,
                    filterEndDate = end,
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
                filterStartDateFlow,
                filterEndDateFlow
            ) { start, end -> Pair(start, end) }
                .flatMapLatest { (startMillis, endMillis) ->
                    val start = startMillis?.let { Date(it) }
                    val end = endMillis?.let { Date(it) }
                    if (start != null && end != null) {
                        getTransactionsByDateRangeUseCase(currentUserId, start, end)
                    } else {
                        getAllTransactionsUseCase(currentUserId)
                    }
                }.combine(preferencesRepository.isAIEnabled) { transactions, aiEnabled ->
                    Pair(transactions, aiEnabled)
                }.collect { (transactions, aiEnabled) ->
                try {
                    val start = filterStartDateFlow.value?.let { Date(it) }
                    val end = filterEndDateFlow.value?.let { Date(it) }
                    val dashboardData = getDashboardDataUseCase(currentUserId, start, end)
                    _uiState.value = _uiState.value.copy(
                        dashboardData = dashboardData,
                        transactions = transactions,
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
            if (preferencesRepository.isAIEnabled.first()) {
                try {
                    val start = filterStartDateFlow.value?.let { Date(it) }
                    val end = filterEndDateFlow.value?.let { Date(it) }
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
        savedStateHandle["start_date"] = startDate?.time
        savedStateHandle["end_date"] = endDate?.time
        refresh()
    }

    fun onShowDatePicker() {
        _showDateRangePicker.value = true
    }

    fun onDismissDatePicker() {
        _showDateRangePicker.value = false
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
    val filterEndDate: Date? = null,
    val isAIEnabled: Boolean = true,
    val showDateRangePicker: Boolean = false
)
