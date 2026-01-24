package com.edudev.finai.presentation.components.dashboardScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.edudev.finai.R
import com.edudev.finai.ui.theme.FinAITheme
import java.text.NumberFormat

@Composable
fun CategorySpendingCard(
    modifier: Modifier = Modifier,
    category: String,
    total: Double,
    percentage: Float,
    currencyFormat: NumberFormat
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.padding(end = 16.dp),
                imageVector = getIconForCategory(category),
                contentDescription = null,
                tint = androidx.compose.ui.graphics.Color.Unspecified
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = category,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = currencyFormat.format(total),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            Text(
                text = "${String.format("%.1f", percentage)}%",
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}

@Composable
fun getIconForCategory(category: String): ImageVector {
    return when (category.lowercase()) {
        "alimentação" -> ImageVector.vectorResource(id = R.drawable.ic_food)
        "transporte" -> ImageVector.vectorResource(id = R.drawable.ic_transport)
        "moradia" -> ImageVector.vectorResource(id = R.drawable.ic_home)
        "saúde" -> ImageVector.vectorResource(id = R.drawable.ic_medical_services)
        "educação" -> ImageVector.vectorResource(id = R.drawable.ic_education)
        "lazer" -> ImageVector.vectorResource(id = R.drawable.ic_lazer)
        "compras" -> ImageVector.vectorResource(id = R.drawable.ic_shopping)


        else -> {
            ImageVector.vectorResource(R.drawable.ic_food)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CategorySpendingCardPreview() {
    FinAITheme {
        CategorySpendingCard(
            category = "Alimentação",
            total = 1.0,
            percentage = 1f,
            currencyFormat = NumberFormat.getCurrencyInstance()
        )
    }
}