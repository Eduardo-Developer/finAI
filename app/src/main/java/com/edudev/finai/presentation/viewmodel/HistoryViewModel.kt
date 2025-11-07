package com.edudev.finai.presentation.viewmodel

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edudev.finai.domain.model.Transaction
import com.edudev.finai.domain.model.TransactionType
import com.edudev.finai.domain.usecase.DeleteTransactionUseCase
import com.edudev.finai.domain.usecase.ExportTransactionsToCsvUseCase
import com.edudev.finai.domain.usecase.GetAllTransactionsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val getAllTransactionsUseCase: GetAllTransactionsUseCase,
    private val deleteTransactionUseCase: DeleteTransactionUseCase,
    private val exportTransactionsToCsvUseCase: ExportTransactionsToCsvUseCase
) : ViewModel() {

    private val _transactions = MutableStateFlow<List<Transaction>>(emptyList())
    val transactions: StateFlow<List<Transaction>> = _transactions.asStateFlow()
    private val _exportState = MutableStateFlow<ExportState>(ExportState.Idle)
    val exportState = _exportState.asStateFlow()
    private val _selectedFilter = MutableStateFlow<TransactionFilter>(TransactionFilter.All)
    val selectedFilter: StateFlow<TransactionFilter> = _selectedFilter.asStateFlow()

    private val _showExportConfirmDialog = MutableStateFlow(false)
    val showExportConfirmDialog = _showExportConfirmDialog.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    val filteredTransactions: StateFlow<List<Transaction>> = combine(
        transactions,
        selectedFilter,
        searchQuery
    ) { trans: List<Transaction>, filter: TransactionFilter, query: String ->
        var filtered = trans

        when (filter) {
            is TransactionFilter.ByType -> {
                filtered = filtered.filter { it.type == filter.type }
            }

            is TransactionFilter.ByCategory -> {
                filtered = filtered.filter { it.category == filter.category }
            }

            TransactionFilter.All -> {}
        }

        if (query.isNotBlank()) {
            filtered = filtered.filter {
                it.description.contains(query, ignoreCase = true) ||
                        it.category.contains(query, ignoreCase = true)
            }
        }

        filtered
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    init {
        viewModelScope.launch {
            getAllTransactionsUseCase().collect { trans ->
                _transactions.value = trans
            }
        }
    }

    fun setFilter(filter: TransactionFilter) {
        _selectedFilter.value = filter
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun deleteTransaction(id: Long) {
        viewModelScope.launch {
            try {
                deleteTransactionUseCase(id)
            } catch (e: Exception) {
                // Handle error
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

            exportTransactionsToCsvUseCase()
                .onSuccess { csvData ->
                    // Sucesso! Envia os dados CSV para a UI.
                    _exportState.value = ExportState.Success(csvData)
                }
                .onFailure { error ->
                    // Falha. Envia a mensagem de erro para a UI.
                    _exportState.value = ExportState.Error(error.message ?: "Erro desconhecido")
                }
        }
    }

    fun onExportStateConsumed() {
        _exportState.value = ExportState.Idle
    }
}

sealed class TransactionFilter {
    object All : TransactionFilter()
    data class ByType(val type: TransactionType) : TransactionFilter()
    data class ByCategory(val category: String) : TransactionFilter()
}

sealed class ExportState {
    object Idle : ExportState()
    object Loading : ExportState()
    data class Success(val csvData: String) : ExportState()
    data class Error(val message: String) : ExportState()
}
