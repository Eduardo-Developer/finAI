package com.edudev.finai.presentation.ui.transaction

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.edudev.finai.R
import com.edudev.finai.presentation.components.FinAiButton
import com.edudev.finai.presentation.components.addTransactionScreen.CategorySelector
import com.edudev.finai.presentation.components.addTransactionScreen.DateSelector
import com.edudev.finai.presentation.components.addTransactionScreen.FormField
import com.edudev.finai.presentation.components.addTransactionScreen.TransactionTypeToggle
import com.edudev.finai.presentation.viewmodel.TransactionViewModel
import com.edudev.finai.ui.theme.Emerald
import com.edudev.finai.ui.theme.FinAITheme
import com.edudev.finai.ui.theme.OnSurfaceVariant
import com.edudev.finai.ui.theme.OnSurfaceWhite
import com.edudev.finai.ui.theme.Onyx
import com.edudev.finai.ui.theme.SurfaceContainerLow

@Composable
fun AddTransactionScreen(onBackClick: () -> Unit, viewModel: TransactionViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        containerColor = Onyx,
        topBar = {
            Row(
                modifier =
                Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = stringResource(R.string.btn_close),
                        tint = Emerald,
                        modifier = Modifier.size(28.dp)
                    )
                }
                Text(
                    text = stringResource(com.edudev.finai.R.string.new_transaction_title),
                    style =
                    MaterialTheme.typography.titleLarge.copy(
                        color = OnSurfaceWhite,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = (-0.02).sp
                    )
                )
                Box(modifier = Modifier.size(48.dp))
            }
        },
        bottomBar = {
            Box(
                modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
                    .navigationBarsPadding()
            ) {
                FinAiButton(
                    onClick = { viewModel.saveTransaction(onBackClick) },
                    enabled = !uiState.isSaving,
                    isLoading = uiState.isSaving,
                    text = stringResource(R.string.btn_save_transaction)
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier =
            Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Income / Expense Toggle
            TransactionTypeToggle(
                selectedType = uiState.type,
                onTypeSelected = { viewModel.setType(it) }
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Amount Input Group
            Text(
                text = stringResource(R.string.label_amount),
                style =
                MaterialTheme.typography.labelSmall.copy(
                    color = OnSurfaceVariant,
                    letterSpacing = 1.sp,
                    fontWeight = FontWeight.Bold
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "R$",
                    style =
                    MaterialTheme.typography.displaySmall.copy(
                        color = OnSurfaceVariant,
                        fontWeight = FontWeight.SemiBold
                    )
                )
                Spacer(Modifier.width(12.dp))
                BasicTextField(
                    value = TextFieldValue(
                        text = uiState.amount,
                        selection = TextRange(uiState.amount.length)
                    ),
                    onValueChange = { newValue ->
                        viewModel.setAmount(newValue.text)
                    },
                    textStyle =
                    MaterialTheme.typography.displayLarge.copy(
                        color = OnSurfaceWhite,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = (-0.02).sp
                    ),
                    cursorBrush = SolidColor(Emerald),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(0.7f),
                    decorationBox = { innerTextField ->
                        Box(contentAlignment = Alignment.Center) {
                            if (uiState.amount.isEmpty()) {
                                Text(
                                    text = "0,00",
                                    style = MaterialTheme.typography.displayLarge.copy(
                                        color = OnSurfaceWhite.copy(alpha = 0.3f),
                                        textAlign = TextAlign.Center,
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = (-0.02).sp
                                    )
                                )
                            }
                            innerTextField()
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.height(48.dp))

            // Form Section
            Column(
                modifier =
                Modifier
                    .fillMaxWidth()
                    .background(SurfaceContainerLow, RoundedCornerShape(32.dp))
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                CategorySelector(
                    selectedCategory = uiState.category,
                    onCategorySelected = { viewModel.setCategory(it) },
                    error = uiState.categoryError
                )

                FormField(
                    label = stringResource(R.string.label_description),
                    value = uiState.description,
                    onValueChange = { viewModel.setDescription(it) },
                    icon = Icons.Default.Edit,
                    placeholder = stringResource(R.string.placeholder_description)
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
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF111412)
@Composable
private fun AddTransactionScreenPreview() {
    FinAITheme {
        AddTransactionScreen(onBackClick = {})
    }
}
