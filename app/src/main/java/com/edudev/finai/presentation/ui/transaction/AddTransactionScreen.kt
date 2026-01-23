package com.edudev.finai.presentation.ui.transaction

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.room.util.TableInfo
import com.edudev.finai.presentation.components.FinAiButton
import com.edudev.finai.presentation.components.addTransactionScreen.CategorySelector
import com.edudev.finai.presentation.components.addTransactionScreen.DateSelector
import com.edudev.finai.presentation.components.addTransactionScreen.FormField
import com.edudev.finai.presentation.components.addTransactionScreen.TransactionTypeSelector
import com.edudev.finai.presentation.components.addTransactionScreen.TransactionTypeToggle
import com.edudev.finai.presentation.viewmodel.TransactionViewModel
import com.edudev.finai.ui.theme.FinAITheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionScreen(
    onBackClick: () -> Unit,
    viewModel: TransactionViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val TextDark = Color(0xFF1E293B)
    val TextMuted = Color(0xFF94A3B8)

    Scaffold(
        containerColor = Color.White,
        contentWindowInsets = WindowInsets.systemBars,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Nova Transação"
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.KeyboardArrowLeft, contentDescription = "Voltar", modifier = Modifier.size(32.dp))                    }
                }
            )
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .padding(24.dp)
                    .navigationBarsPadding()) {
                FinAiButton(
                    onClick = { viewModel.saveTransaction(onBackClick) },
                    enabled = !uiState.isSaving,
                    isLoading = uiState.isSaving,
                    text = "Salvar"
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Tipo de transação
            TransactionTypeToggle(
                selectedType = uiState.type,
                onTypeSelected = { viewModel.setType(it) }
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                "VALOR DA TRANSAÇÃO",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = TextMuted,
                letterSpacing = 1.sp
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text("R$", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = TextMuted)
                Spacer(Modifier.width(8.dp))
                BasicTextField(
                    value = uiState.amount,
                    onValueChange = { viewModel.setAmount(it) },
                    textStyle = androidx.compose.ui.text.TextStyle(
                        fontSize = 56.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextDark,
                        textAlign = TextAlign.Center
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.width(IntrinsicSize.Min)
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
                CategorySelector(
                    selectedCategory = uiState.category,
                    onCategorySelected = { viewModel.setCategory(it) },
                    error = uiState.categoryError
                )
                FormField(
                    label = "Descrição",
                    value = uiState.description,
                    onValueChange = { viewModel.setDescription(it) },
                    icon = Icons.Default.Edit,
                    placeholder = "Ex: Aluguel de Janeiro"
                )
                DateSelector(
                    selectedDate = uiState.date,
                    onDateSelected = { viewModel.setDate(it) }
                )
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


@Preview
@Composable
private fun AddTransactionScreenPreview() {
    FinAITheme {
        AddTransactionScreen(onBackClick = {})
    }
}