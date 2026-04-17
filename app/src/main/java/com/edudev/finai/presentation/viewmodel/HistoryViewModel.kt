package com.edudev.finai.presentation.viewmodel

import android.os.Parcelable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.SavedStateHandle
import com.edudev.finai.domain.repository.AuthRepository
import com.edudev.finai.domain.model.Transaction
import com.edudev.finai.domain.model.TransactionType
import com.edudev.finai.domain.usecase.DeleteTransactionUseCase
import com.edudev.finai.domain.usecase.ExportTransactionsToCsvUseCase
import com.edudev.finai.domain.usecase.GetAllTransactionsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val getAllTransactionsUseCase: GetAllTransactionsUseCase,
    private val deleteTransactionUseCase: DeleteTransactionUseCase,
    private val exportTransactionsToCsvUseCase: ExportTransactionsToCsvUseCase,
    private val authRepository: AuthRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _transactions = MutableStateFlow<List<Transaction>>(emptyList())
    private val _exportState = MutableStateFlow<ExportState>(ExportState.Idle)
    private val _showExportConfirmDialog = MutableStateFlow(false)
    private val _showDatePicker = MutableStateFlow(false)

    private val selectedFilterFlow = savedStateHandle.getStateFlow<TransactionFilter>("filter", TransactionFilter.All)
    private val searchQueryFlow = savedStateHandle.getStateFlow<String>("query", "")

    val uiState: StateFlow<HistoryUiState> = combine(
        _transactions,
        selectedFilterFlow,
        searchQueryFlow,
        _exportState,
        combine(_showExportConfirmDialog, _showDatePicker) { showExport, showDate -> showExport to showDate }
    ) { transactions, filter, query, exportState, (showExportDialog, showDatePicker) ->
        HistoryUiState(
            transactions = transactions,
            filteredTransactions = applyFilters(transactions, filter, query),
            selectedFilter = filter,
            searchQuery = query,
            exportState = exportState,
            showExportConfirmDialog = showExportDialog,
            showDatePicker = showDatePicker
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = HistoryUiState()
    )

    private val currentUserId = authRepository.currentUser ?: ""

    init {
        viewModelScope.launch {
            getAllTransactionsUseCase(currentUserId).collect { trans ->
                _transactions.value = trans
            }
        }
    }

    fun setFilter(filter: TransactionFilter) {
        savedStateHandle["filter"] = filter
    }

    fun setSearchQuery(query: String) {
        savedStateHandle["query"] = query
    }

    fun onShowDatePicker() {
        _showDatePicker.value = true
    }

    fun onDismissDatePicker() {
        _showDatePicker.value = false
    }

    fun deleteTransaction(id: Long) {
        viewModelScope.launch {
            try {
                deleteTransactionUseCase(currentUserId, id)
            } catch (e: Exception) {

            }
        }
    }

    fun onExportIntent() {
        _showExportConfirmDialog.value = true
    }

    fun onDismissExportDialog() {
        _showExportConfirmDialog.value = false
    }

    fun onExportClicked() {

        _showExportConfirmDialog.value = false

        viewModelScope.launch {
            _exportState.value = ExportState.Loading

            exportTransactionsToCsvUseCase(currentUserId)
                .onSuccess { csvData ->
                    _exportState.value = ExportState.Success(csvData)
                }
                .onFailure { error ->
                    _exportState.value = ExportState.Error(error.message ?: "Erro desconhecido")
                }
        }
    }

    fun onExportStateConsumed() {
        _exportState.value = ExportState.Idle
    }

    private fun applyFilters(
        transactions: List<Transaction>,
        filter: TransactionFilter,
        query: String
    ): List<Transaction> {
        var filtered = transactions

        when (filter) {
            is TransactionFilter.ByType -> {
                filtered = filtered.filter { it.type == filter.type }
            }

            is TransactionFilter.ByCategory -> {
                filtered = filtered.filter { it.category == filter.category }
            }

            is TransactionFilter.ByDateRange -> {
                val startCal = Calendar.getInstance().apply { timeInMillis = filter.startMillis }
                val endCal = Calendar.getInstance().apply {
                    timeInMillis = filter.endMillis
                    set(Calendar.HOUR_OF_DAY, 23)
                    set(Calendar.MINUTE, 59)
                    set(Calendar.SECOND, 59)
                    set(Calendar.MILLISECOND, 999)
                }

                filtered = filtered.filter { tx ->
                    val txCal = Calendar.getInstance().apply { timeInMillis = tx.date.time }
                    !txCal.before(startCal) && !txCal.after(endCal)
                }
            }

            TransactionFilter.All -> {}
        }

        if (query.isNotBlank()) {
            filtered = filtered.filter {
                it.description.contains(query, ignoreCase = true) ||
                        it.category.contains(query, ignoreCase = true)
            }
        }

        return filtered
    }
}

data class HistoryUiState(
    val transactions: List<Transaction> = emptyList(),
    val filteredTransactions: List<Transaction> = emptyList(),
    val exportState: ExportState = ExportState.Idle,
    val selectedFilter: TransactionFilter = TransactionFilter.All,
    val showExportConfirmDialog: Boolean = false,
    val showDatePicker: Boolean = false,
    val searchQuery: String = "",
    val isLoading: Boolean = false
)

@Parcelize
sealed class TransactionFilter : Parcelable {
    @Parcelize
    data object All : TransactionFilter()
    @Parcelize
    data class ByType(val type: TransactionType) : TransactionFilter()
    @Parcelize
    data class ByCategory(val category: String) : TransactionFilter()
    @Parcelize
    data class ByDateRange(val startMillis: Long, val endMillis: Long) : TransactionFilter()
}

sealed class ExportState {
    object Idle : ExportState()
    object Loading : ExportState()
    data class Success(val csvData: String) : ExportState()
    data class Error(val message: String) : ExportState()
}
