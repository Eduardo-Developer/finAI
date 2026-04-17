package com.edudev.finai.presentation.ui.history

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.edudev.finai.R
import com.edudev.finai.domain.model.Transaction
import com.edudev.finai.presentation.components.dashboardScreen.DateRangePickerModal
import com.edudev.finai.presentation.components.historyTransaction.FilterChips
import com.edudev.finai.presentation.components.historyTransaction.TransactionItem
import com.edudev.finai.presentation.viewmodel.ExportState
import com.edudev.finai.presentation.viewmodel.HistoryViewModel
import com.edudev.finai.presentation.viewmodel.TransactionFilter
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    viewModel: HistoryViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val dateFormat = remember { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }

    if (uiState.showDatePicker) {
        DateRangePickerModal(
            onDateRangeSelected = { range ->
                val startMillis = range.first
                val endMillis = range.second
                if (startMillis != null && endMillis != null) {
                    viewModel.setFilter(TransactionFilter.ByDateRange(startMillis, endMillis))
                }
            },
            onDismiss = { viewModel.onDismissDatePicker() }
        )
    }

    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    val fileSaverLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("text/csv"),
        onResult = { uri: Uri? ->
            uri?.let {
                val csvData = (uiState.exportState as? ExportState.Success)?.csvData
                if (csvData != null) {
                    try {
                        context.contentResolver.openOutputStream(it)?.use { outputStream ->
                            outputStream.write(csvData.toByteArray())
                        }
                    } catch (e: IOException) {
                    }
                }
            }
            viewModel.onExportStateConsumed()
        }
    )

    LaunchedEffect(uiState.exportState) {
        when (val state = uiState.exportState) {
            is ExportState.Success -> {
                fileSaverLauncher.launch("historico_finai.csv")
            }
            is ExportState.Error -> {
                snackbarHostState.showSnackbar("Erro ao exportar: ${state.message}")
                viewModel.onExportStateConsumed()
            }
            else -> {}
        }
    }

    if (uiState.showExportConfirmDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.onDismissExportDialog() },
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
            title = { Text("Confirmar Exportaçao", style = MaterialTheme.typography.titleLarge) },
            text = { Text("Deseja realmente exportar o histórico de transações para um arquivo CSV?", style = MaterialTheme.typography.bodyMedium) },
            confirmButton = {
                TextButton(onClick = { viewModel.onExportClicked() }) {
                    Text("Sim", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.onDismissExportDialog() }) {
                    Text("Não", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        )
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Section 1: Header
            HeaderSection(
                onExportClick = { viewModel.onExportIntent() },
                isExporting = uiState.exportState is ExportState.Loading
            )

            // Section 2: Search Bar
            SearchBarSection(
                query = uiState.searchQuery,
                onQueryChange = { viewModel.setSearchQuery(it) },
                onFilterDateClick = { viewModel.onShowDatePicker() },
                isFilterActive = uiState.selectedFilter is TransactionFilter.ByDateRange
            )

            // Section 3: Filter Chips
            FilterChips(
                selectedFilter = uiState.selectedFilter,
                onFilterSelected = { viewModel.setFilter(it) },
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // Section 4: Transaction List Grouped by Date
            val groupedTransactions = remember(uiState.filteredTransactions) {
                groupTransactionsByDate(uiState.filteredTransactions)
            }

            AnimatedContent(
                targetState = groupedTransactions.isEmpty(),
                modifier = Modifier.weight(1f),
                label = "ListAnimation"
            ) { isEmpty ->
                if (isEmpty) {
                    EmptyHistoryPlaceholder()
                } else {
                    LazyColumn(
                        contentPadding = PaddingValues(bottom = 24.dp),
                        verticalArrangement = Arrangement.spacedBy(24.dp)
                    ) {
                        groupedTransactions.forEach { (dateHeader, transactions) ->
                            item {
                                TransactionGroup(
                                    header = dateHeader,
                                    transactions = transactions,
                                    onDeleteTransaction = { viewModel.deleteTransaction(it) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun HeaderSection(
    onExportClick: () -> Unit,
    isExporting: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 32.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        Column {
            Text(
                text = "History",
                style = MaterialTheme.typography.displayMedium.copy(
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = (-2).sp
                ),
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "Review your recent activity",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        
        IconButton(
            onClick = onExportClick,
            modifier = Modifier
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceContainerHigh)
        ) {
            if (isExporting) {
                CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
            } else {
                Icon(
                    painter = painterResource(R.drawable.csv_icon),
                    contentDescription = "Export CSV",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
private fun SearchBarSection(
    query: String,
    onQueryChange: (String) -> Unit,
    onFilterDateClick: () -> Unit,
    isFilterActive: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        TextField(
            value = query,
            onValueChange = onQueryChange,
            modifier = Modifier
                .weight(1f)
                .height(56.dp)
                .clip(CircleShape),
            placeholder = { Text("Search transactions...", color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant) },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                disabledContainerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            ),
            singleLine = true,
            shape = CircleShape
        )

        IconButton(
            onClick = onFilterDateClick,
            modifier = Modifier
                .size(56.dp)
                .background(
                    color = if (isFilterActive) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceContainerHighest,
                    shape = CircleShape
                )
        ) {
            Icon(
                imageVector = Icons.Default.FilterList,
                contentDescription = "Date Filter",
                tint = if (isFilterActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun TransactionGroup(
    header: String,
    transactions: List<Transaction>,
    onDeleteTransaction: (Long) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {
        Text(
            text = header.uppercase(),
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            ),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(start = 4.dp, bottom = 12.dp)
        )

        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(24.dp))
                .background(MaterialTheme.colorScheme.surfaceContainerLow)
        ) {
            transactions.forEachIndexed { index, transaction ->
                TransactionItem(
                    transaction = transaction,
                    onDeleteClick = { onDeleteTransaction(transaction.id!!) }
                )
            }
        }
    }
}

@Composable
private fun EmptyHistoryPlaceholder() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "No transactions found",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

private fun groupTransactionsByDate(transactions: List<Transaction>): List<Pair<String, List<Transaction>>> {
    val groupedMap = transactions
        .sortedByDescending { it.date }
        .groupBy { transaction ->
            getRelativeDate(transaction.date)
        }
    return groupedMap.toList()
}

private fun getRelativeDate(date: Date): String {
    val now = Calendar.getInstance()
    val target = Calendar.getInstance().apply { time = date }

    return when {
        isSameDay(now, target) -> "Today"
        isYesterday(now, target) -> "Yesterday"
        else -> SimpleDateFormat("MMMM d, yyyy", Locale.US).format(date)
    }
}

private fun isSameDay(cal1: Calendar, cal2: Calendar): Boolean {
    return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
            cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)
}

private fun isYesterday(now: Calendar, target: Calendar): Boolean {
    val yesterday = (now.clone() as Calendar).apply { add(Calendar.DAY_OF_YEAR, -1) }
    return isSameDay(yesterday, target)
}