package com.edudev.finai.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edudev.finai.domain.model.Transaction
import com.edudev.finai.domain.model.TransactionType
import com.edudev.finai.domain.repository.AuthRepository
import com.edudev.finai.domain.usecase.AddTransactionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.Date
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class TransactionViewModel
@Inject
constructor(
    private val addTransactionUseCase: AddTransactionUseCase,
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(TransactionUiState())
    val uiState: StateFlow<TransactionUiState> = _uiState.asStateFlow()

    fun setAmount(amount: String) {
        val cleanString = amount.replace("[^\\d]".toRegex(), "")
        if (cleanString.isEmpty()) {
            _uiState.value = _uiState.value.copy(amount = "", amountError = null)
            return
        }

        try {
            val parsed = cleanString.toDouble() / 100
            val formatted = java.text.NumberFormat.getCurrencyInstance(java.util.Locale("pt", "BR")).format(parsed)
            // Remove the "R$" and spaces for the text field, we'll keep it simple
            val result = formatted.replace("R$", "").trim()

            _uiState.value =
                _uiState.value.copy(
                    amount = result,
                    amountError = null
                )
        } catch (e: Exception) {
            // Ignore parsing errors
        }
    }

    fun setCategory(category: String) {
        _uiState.value =
            _uiState.value.copy(
                category = category,
                categoryError = null
            )
    }

    fun setDescription(description: String) {
        _uiState.value =
            _uiState.value.copy(
                description = description
            )
    }

    fun setType(type: TransactionType) {
        _uiState.value =
            _uiState.value.copy(
                type = type
            )
    }

    fun setDate(date: Date) {
        _uiState.value =
            _uiState.value.copy(
                date = date
            )
    }

    fun saveTransaction(onSuccess: () -> Unit) {
        viewModelScope.launch {
            val state = _uiState.value

            val currentUserId =
                authRepository.currentUser
                    ?: run {
                        _uiState.value = state.copy(error = "Usuário não autenticado")
                        return@launch
                    }

            if (validateInput(state)) {
                _uiState.value = state.copy(isSaving = true)

                try {
                    val transaction =
                        Transaction(
                            amount = parseAmount(state.amount),
                            userId = currentUserId,
                            category = state.category,
                            description = state.description,
                            type = state.type,
                            date = state.date
                        )

                    addTransactionUseCase(transaction)
                    _uiState.value = TransactionUiState()
                    onSuccess()
                } catch (e: Exception) {
                    _uiState.value =
                        state.copy(
                            isSaving = false,
                            error = e.message
                        )
                }
            }
        }
    }

    private fun validateInput(state: TransactionUiState): Boolean {
        var isValid = true

        val amountValue = parseAmount(state.amount)
        if (state.amount.isBlank() || amountValue <= 0) {
            _uiState.value = state.copy(amountError = "Valor inválido")
            isValid = false
        }

        if (state.category.isBlank()) {
            _uiState.value = state.copy(categoryError = "Categoria é obrigatória")
            isValid = false
        }

        return isValid
    }

    private fun parseAmount(amount: String): Double {
        val cleanString = amount.replace("[^\\d]".toRegex(), "")
        return cleanString.toDoubleOrNull()?.let { it / 100 } ?: 0.0
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
