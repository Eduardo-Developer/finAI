package com.edudev.finai.presentation.components.historyTransaction

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionItem(transaction: Transaction, onDeleteClick: () -> Unit) {
    val currencyFormat = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

    val dismissState =
        rememberSwipeToDismissBoxState(
            confirmValueChange = {
                if (it == SwipeToDismissBoxValue.EndToStart) {
                    onDeleteClick()
                    true
                } else {
                    false
                }
            }
        )

    SwipeToDismissBox(
        state = dismissState,
        enableDismissFromStartToEnd = false,
        backgroundContent = {
            val color by animateColorAsState(
                when (dismissState.targetValue) {
                    SwipeToDismissBoxValue.EndToStart -> MaterialTheme.colorScheme.error
                    else -> Color.Transparent
                },
                label = "DismissBackground"
            )
            Box(
                Modifier
                    .fillMaxSize()
                    .background(color)
                    .padding(horizontal = 20.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = MaterialTheme.colorScheme.onError
                )
            }
        }
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.surfaceContainerLow
        ) {
            Row(
                modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Category Icon Box
                Box(
                    modifier =
                    Modifier
                        .size(48.dp)
                        .background(
                            color = MaterialTheme.colorScheme.surfaceContainerHighest,
                            shape = RoundedCornerShape(12.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = getCategoryIcon(transaction.category),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                // Transaction Details
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = transaction.description.ifBlank { transaction.category },
                        style =
                        MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.sp
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "${timeFormat.format(transaction.date)} • ${transaction.category}",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // Amount
                Text(
                    text = "${if (transaction.type == TransactionType.INCOME) "+" else "-"} ${
                        currencyFormat.format(transaction.amount).replace("R$", "").trim()
                    }",
                    style =
                    MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = (-0.5).sp
                    ),
                    color =
                    if (transaction.type == TransactionType.INCOME) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.onSurface
                    }
                )
            }
        }
    }
}

private fun getCategoryIcon(category: String): ImageVector {
    return when (category.lowercase()) {
        "alimentação", "food", "lazer" -> Icons.Default.Restaurant
        "transporte", "transport" -> Icons.Default.DirectionsCar
        "moradia", "home" -> Icons.Default.Home
        "saúde", "health" -> Icons.Default.MedicalServices
        "educação", "education" -> Icons.Default.School
        "salário", "salary", "renda" -> Icons.Default.AccountBalanceWallet
        else -> Icons.Default.Payments
    }
}

@Preview(showBackground = true)
@Composable
private fun TransactionItemPreview() {
    val transaction =
        Transaction(
            userId = "123",
            id = 1,
            description = "Blue Bottle Coffee",
            amount = 12.50,
            type = TransactionType.EXPENSE,
            category = "Alimentação",
            date = Date()
        )
    FinAITheme {
        TransactionItem(transaction = transaction, onDeleteClick = {})
    }
}
