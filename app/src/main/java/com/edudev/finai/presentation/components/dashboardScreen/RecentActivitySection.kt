package com.edudev.finai.presentation.components.dashboardScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.edudev.finai.domain.model.Transaction
import com.edudev.finai.domain.model.TransactionType
import com.edudev.finai.ui.theme.FinAITheme
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun RecentActivitySection(
    transactions: List<Transaction>,
    onViewAllClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(32.dp)),
        color = MaterialTheme.colorScheme.surfaceContainerLow
    ) {
        Column(
            modifier = Modifier.padding(24.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Recent Activity",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                TextButton(onClick = onViewAllClick) {
                    Text(
                        text = "View All",
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                transactions.take(3).forEach { transaction ->
                    RecentTransactionItem(transaction = transaction)
                }
                
                if (transactions.isEmpty()) {
                    Text(
                        text = "No recent activity",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(vertical = 16.dp).align(Alignment.CenterHorizontally)
                    )
                }
            }
        }
    }
}

@Composable
private fun RecentTransactionItem(transaction: Transaction) {
    val currencyFormat = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
    val dateFormat = SimpleDateFormat("MMM d, h:mm a", Locale.US)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable { /* Detail */ }
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.surfaceContainer),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = getCategoryIcon(transaction.category),
                contentDescription = null,
                tint = if (transaction.type == TransactionType.INCOME) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = transaction.description.ifBlank { transaction.category },
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "${transaction.category} • ${dateFormat.format(transaction.date)}",
                style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 0.5.sp),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Text(
            text = "${if (transaction.type == TransactionType.INCOME) "+" else "-"} ${
                currencyFormat.format(transaction.amount).replace("R$", "").trim()
            }",
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.ExtraBold),
            color = if (transaction.type == TransactionType.INCOME) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
        )
    }
}

private fun getCategoryIcon(category: String): ImageVector {
    return when (category.lowercase()) {
        "alimentação", "food", "dining" -> Icons.Default.Restaurant
        "transporte", "transport" -> Icons.Default.DirectionsCar
        "salário", "salary", "deposit" -> Icons.Default.AccountBalanceWallet
        else -> Icons.Default.Payments
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF111412)
@Composable
private fun RecentActivitySectionPreview() {
    FinAITheme {
        Box(modifier = Modifier.padding(16.dp)) {
            RecentActivitySection(
                transactions = listOf(
                    Transaction(
                        id = 1L,
                        userId = "1",
                        amount = 142.50,
                        category = "Groceries",
                        description = "Whole Foods Market",
                        type = TransactionType.EXPENSE,
                        date = Date()
                    ),
                    Transaction(
                        id = 2L,
                        userId = "1",
                        amount = 4250.0,
                        category = "Deposit",
                        description = "TechCorp Salary",
                        type = TransactionType.INCOME,
                        date = Date()
                    )
                ),
                onViewAllClick = {}
            )
        }
    }
}
