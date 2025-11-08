package com.edudev.finai.presentation.ui.history

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.edudev.finai.domain.model.Transaction
import com.edudev.finai.domain.model.TransactionType
import com.edudev.finai.presentation.viewmodel.HistoryViewModel
import com.edudev.finai.presentation.viewmodel.TransactionFilter
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import com.edudev.finai.R
import java.util.Locale
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import com.edudev.finai.presentation.components.FinAiTopAppBar
import com.edudev.finai.presentation.viewmodel.ExportState
import com.edudev.finai.ui.theme.FinAITheme
import java.io.IOException
import javax.xml.validation.ValidatorHandler

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    viewModel: HistoryViewModel = hiltViewModel()
) {
    val filteredTransactions by viewModel.filteredTransactions.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedFilter by viewModel.selectedFilter.collectAsState()
    val showDialog by viewModel.showExportConfirmDialog.collectAsState()

    val context = LocalContext.current
    val exportState by viewModel.exportState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    val fileSaverLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("text/csv"),
        onResult = { uri: Uri? ->
            uri?.let {
                // Se o usuário escolheu um local, escrevemos os dados no arquivo
                val csvData = (viewModel.exportState.value as? ExportState.Success)?.csvData
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

    // 2. Use `LaunchedEffect` para reagir às mudanças de estado do ViewModel
    LaunchedEffect(exportState) {
        when (val state = exportState) {
            is ExportState.Success -> {
                // Estado de sucesso! Lança o seletor de arquivos do sistema
                fileSaverLauncher.launch("historico_finai.csv") // Nome padrão do arquivo
            }

            is ExportState.Error -> {
                // Estado de erro. Mostra uma mensagem para o usuário.
                snackbarHostState.showSnackbar("Erro ao exportar: ${state.message}")
                viewModel.onExportStateConsumed() // Reseta o estado
            }

            else -> {
            }
        }
    }

    //Dialog de confirmação de exportação csv
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.onDismissExportDialog() },
            title = { Text("Confirmar Exportação") },
            text = { Text("Deseja realmente exportar o histórico de transações para um arquivo CSV?") },
            confirmButton = {
                TextButton(onClick = { viewModel.onExportClicked() }) {
                    Text("Sim")
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.onDismissExportDialog() }) {
                    Text("Não")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            FinAiTopAppBar(
                title = { Text("Histórico") },

                )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Search bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.setSearchQuery(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                placeholder = { Text("Pesquisar...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                singleLine = true
            )

            Row(
                modifier = Modifier.padding(end = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Filters
                FilterChips(
                    modifier = Modifier.weight(1f),
                    selectedFilter = selectedFilter,
                    onFilterSelected = { viewModel.setFilter(it) }
                )

                IconButton(
                    onClick = {
                        if (exportState !is ExportState.Loading) {
                            viewModel.onExportIntent()
                        }
                    }) {
                    if (exportState is ExportState.Loading) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp))
                    } else {
                        Icon(
                            modifier = Modifier.size(24.dp),
                            painter = painterResource(R.drawable.csv_icon),
                            contentDescription = "Exportar Histórico"
                        )
                    }
                }
            }

            // Transactions list
            AnimatedContent(
                targetState = filteredTransactions.isEmpty(),
                modifier = Modifier.weight(1f)
            ) { isEmpty ->
                if (isEmpty) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Nenhuma transação encontrada",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                } else {
                    LazyColumn(
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(filteredTransactions) { transaction ->
                            TransactionItem(
                                transaction = transaction,
                                onDeleteClick = { viewModel.deleteTransaction(transaction.id!!) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FilterChips(
    modifier: Modifier = Modifier,
    selectedFilter: TransactionFilter,
    onFilterSelected: (TransactionFilter) -> Unit
) {
    Row(
        modifier = modifier
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FilterChip(
            selected = selectedFilter is TransactionFilter.All,
            onClick = { onFilterSelected(TransactionFilter.All) },
            label = { Text("Todas") }
        )
        FilterChip(
            selected = selectedFilter is TransactionFilter.ByType &&
                    selectedFilter.type == TransactionType.INCOME,
            onClick = { onFilterSelected(TransactionFilter.ByType(TransactionType.INCOME)) },
            label = { Text("Receitas") }
        )
        FilterChip(
            selected = selectedFilter is TransactionFilter.ByType &&
                    selectedFilter.type == TransactionType.EXPENSE,
            onClick = { onFilterSelected(TransactionFilter.ByType(TransactionType.EXPENSE)) },
            label = { Text("Despesas") }
        )
    }
}

@Composable
fun TransactionItem(
    transaction: Transaction,
    onDeleteClick: () -> Unit
) {
    val currencyFormat = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (transaction.type == TransactionType.INCOME)
                MaterialTheme.colorScheme.primaryContainer
            else
                MaterialTheme.colorScheme.errorContainer
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = transaction.description.ifBlank { transaction.category },
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = transaction.category,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = dateFormat.format(transaction.date),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = "${if (transaction.type == TransactionType.INCOME) "+" else "-"}${
                        currencyFormat.format(
                            transaction.amount
                        )
                    }",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = if (transaction.type == TransactionType.INCOME)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.error
                )
                IconButton(onClick = onDeleteClick) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Deletar",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

@Preview(name = "Filter Chips Preview")
@Composable
private fun FilterChipsPreview() {
    FinAITheme {
        FilterChips(
            selectedFilter = TransactionFilter.ByType(TransactionType.EXPENSE),
            onFilterSelected = {}
        )
    }
}

@Preview(name = "Transaction Item - Income")
@Composable
private fun TransactionItemIncomePreview() {
    val transaction = Transaction(
        userId = "123456",
        id = 1,
        description = "Salário",
        amount = 5000.0,
        type = TransactionType.INCOME,
        category = "Salário",
        date = Date()
    )
    FinAITheme {
        TransactionItem(transaction = transaction, onDeleteClick = {})
    }
}

@Preview(name = "Transaction Item - Expense (Dark)")
@Composable
private fun TransactionItemExpensePreview() {
    val transaction = Transaction(
        userId = "123456",
        id = 2,
        description = "Aluguel",
        amount = 1500.0,
        type = TransactionType.EXPENSE,
        category = "Moradia",
        date = Date()
    )
    FinAITheme {
        TransactionItem(transaction = transaction, onDeleteClick = {})
    }
}
