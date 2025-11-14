package com.edudev.finai.presentation.ui.transaction

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.edudev.finai.presentation.components.CategorySelector
import com.edudev.finai.presentation.components.DateSelector
import com.edudev.finai.presentation.components.TransactionTypeSelector
import com.edudev.finai.presentation.viewmodel.TransactionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionScreen(
    onBackClick: () -> Unit,
    viewModel: TransactionViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nova Transação") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Tipo de transação
            TransactionTypeSelector(
                selectedType = uiState.type,
                onTypeSelected = { viewModel.setType(it) }
            )

            // Valor
            OutlinedTextField(
                value = uiState.amount,
                onValueChange = { viewModel.setAmount(it) },
                label = { Text("Valor") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = uiState.amountError != null,
                supportingText = uiState.amountError?.let { { Text(it) } },
                prefix = { Text("R$ ") }
            )

            // Categoria
            CategorySelector(
                selectedCategory = uiState.category,
                onCategorySelected = { viewModel.setCategory(it) },
                error = uiState.categoryError
            )

            // Descrição
            OutlinedTextField(
                value = uiState.description,
                onValueChange = { viewModel.setDescription(it) },
                label = { Text("Descrição") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Data
            DateSelector(
                selectedDate = uiState.date,
                onDateSelected = { viewModel.setDate(it) }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Botão salvar
            Button(
                onClick = { viewModel.saveTransaction(onBackClick) },
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isSaving
            ) {
                if (uiState.isSaving) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text("Salvar")
            }

            uiState.error?.let { error ->
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}