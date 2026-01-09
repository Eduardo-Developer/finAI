package com.edudev.finai.presentation.ui.history

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.edudev.finai.R
import com.edudev.finai.presentation.components.FinAiTopAppBar
import com.edudev.finai.presentation.components.historyTransaction.FilterChips
import com.edudev.finai.presentation.components.historyTransaction.TransactionItem
import com.edudev.finai.presentation.viewmodel.ExportState
import com.edudev.finai.presentation.viewmodel.HistoryViewModel
import java.io.IOException

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

    LaunchedEffect(exportState) {
        when (val state = exportState) {
            is ExportState.Success -> {
                fileSaverLauncher.launch("historico_finai.csv")
            }

            is ExportState.Error -> {
                snackbarHostState.showSnackbar("Erro ao exportar: ${state.message}")
                viewModel.onExportStateConsumed()
            }

            else -> {
            }
        }
    }

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