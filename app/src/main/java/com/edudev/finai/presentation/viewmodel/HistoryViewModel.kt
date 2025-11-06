package com.edudev.finai.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edudev.finai.domain.model.Transaction
import com.edudev.finai.domain.model.TransactionType
import com.edudev.finai.domain.usecase.DeleteTransactionUseCase
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
    private val deleteTransactionUseCase: DeleteTransactionUseCase
) : ViewModel() {

    private val _transactions = MutableStateFlow<List<Transaction>>(emptyList())
    val transactions: StateFlow<List<Transaction>> = _transactions.asStateFlow()

    private val _selectedFilter = MutableStateFlow<TransactionFilter>(TransactionFilter.All)
    val selectedFilter: StateFlow<TransactionFilter> = _selectedFilter.asStateFlow()

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
}

sealed class TransactionFilter {
    object All : TransactionFilter()
    data class ByType(val type: TransactionType) : TransactionFilter()
    data class ByCategory(val category: String) : TransactionFilter()
}
