package com.edudev.finai.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edudev.finai.domain.model.Transaction
import com.edudev.finai.domain.model.TransactionType
import com.edudev.finai.domain.usecase.AddTransactionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class TransactionViewModel @Inject constructor(
    private val addTransactionUseCase: AddTransactionUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(TransactionUiState())
    val uiState: StateFlow<TransactionUiState> = _uiState.asStateFlow()

    fun setAmount(amount: String) {
        _uiState.value = _uiState.value.copy(
            amount = amount,
            amountError = null
        )
    }

    fun setCategory(category: String) {
        _uiState.value = _uiState.value.copy(
            category = category,
            categoryError = null
        )
    }

    fun setDescription(description: String) {
        _uiState.value = _uiState.value.copy(
            description = description
        )
    }

    fun setType(type: TransactionType) {
        _uiState.value = _uiState.value.copy(
            type = type
        )
    }

    fun setDate(date: Date) {
        _uiState.value = _uiState.value.copy(
            date = date
        )
    }

    fun saveTransaction(onSuccess: () -> Unit) {
        viewModelScope.launch {
            val state = _uiState.value
            
            if (validateInput(state)) {
                _uiState.value = state.copy(isSaving = true)
                
                try {
                    val transaction = Transaction(
                        amount = state.amount.toDoubleOrNull() ?: 0.0,
                        category = state.category,
                        description = state.description,
                        type = state.type,
                        date = state.date
                    )
                    
                    addTransactionUseCase(transaction)
                    _uiState.value = TransactionUiState()
                    onSuccess()
                } catch (e: Exception) {
                    _uiState.value = state.copy(
                        isSaving = false,
                        error = e.message
                    )
                }
            }
        }
    }

    private fun validateInput(state: TransactionUiState): Boolean {
        var isValid = true
        
        if (state.amount.isBlank() || state.amount.toDoubleOrNull() == null || state.amount.toDouble() <= 0) {
            _uiState.value = state.copy(amountError = "Valor inválido")
            isValid = false
        }
        
        if (state.category.isBlank()) {
            _uiState.value = state.copy(categoryError = "Categoria é obrigatória")
            isValid = false
        }
        
        return isValid
    }

    fun reset() {
        _uiState.value = TransactionUiState()
    }
}

data class TransactionUiState(
    val amount: String = "",
    val category: String = "",
    val description: String = "",
    val type: TransactionType = TransactionType.EXPENSE,
    val date: Date = Date(),
    val isSaving: Boolean = false,
    val amountError: String? = null,
    val categoryError: String? = null,
    val error: String? = null
)
