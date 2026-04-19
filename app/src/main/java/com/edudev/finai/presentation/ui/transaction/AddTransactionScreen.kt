package com.edudev.finai.presentation.ui.transaction

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.edudev.finai.presentation.components.FinAiButton
import com.edudev.finai.presentation.components.addTransactionScreen.CategorySelector
import com.edudev.finai.presentation.components.addTransactionScreen.DateSelector
import com.edudev.finai.presentation.components.addTransactionScreen.FormField
import com.edudev.finai.presentation.components.addTransactionScreen.TransactionTypeToggle
import com.edudev.finai.presentation.viewmodel.TransactionViewModel
import com.edudev.finai.ui.theme.Emerald
import com.edudev.finai.ui.theme.FinAITheme
import com.edudev.finai.ui.theme.Jade
import com.edudev.finai.ui.theme.MintEmerald
import com.edudev.finai.ui.theme.OnSurfaceVariant
import com.edudev.finai.ui.theme.OnSurfaceWhite
import com.edudev.finai.ui.theme.Onyx
import com.edudev.finai.ui.theme.SurfaceContainerLow

@Composable
fun AddTransactionScreen(
    onBackClick: () -> Unit,
    viewModel: TransactionViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        containerColor = Onyx,
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = Emerald,
                        modifier = Modifier.size(28.dp)
                    )
                }
                Text(
                    text = "New Transaction",
                    style = MaterialTheme.typography.titleLarge.copy(
                        color = OnSurfaceWhite,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = (-0.02).sp
                    )
                )
                Box(modifier = Modifier.size(48.dp)) // Spacer to balance the close button
            }
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
                    .navigationBarsPadding()
            ) {
                FinAiButton(
                    onClick = { viewModel.saveTransaction(onBackClick) },
                    enabled = !uiState.isSaving,
                    isLoading = uiState.isSaving,
                    text = "Save Transaction"
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
            Spacer(modifier = Modifier.height(16.dp))

            // Income / Expense Toggle
            TransactionTypeToggle(
                selectedType = uiState.type,
                onTypeSelected = { viewModel.setType(it) }
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Amount Input Group
            Text(
                text = "AMOUNT",
                style = MaterialTheme.typography.labelSmall.copy(
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
                    style = MaterialTheme.typography.displaySmall.copy(
                        color = OnSurfaceVariant,
                        fontWeight = FontWeight.SemiBold
                    )
                )
                Spacer(Modifier.width(12.dp))
                BasicTextField(
                    value = uiState.amount,
                    onValueChange = { viewModel.setAmount(it) },
                    textStyle = MaterialTheme.typography.displayLarge.copy(
                        color = OnSurfaceWhite,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = (-0.02).sp
                    ),
                    cursorBrush = SolidColor(Emerald),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth(0.7f)
                )
            }

            Spacer(modifier = Modifier.height(48.dp))

            // Form Section
            Column(
                modifier = Modifier
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
                    label = "Description",
                    value = uiState.description,
                    onValueChange = { viewModel.setDescription(it) },
                    icon = Icons.Default.Edit,
                    placeholder = "What was this for?"
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
