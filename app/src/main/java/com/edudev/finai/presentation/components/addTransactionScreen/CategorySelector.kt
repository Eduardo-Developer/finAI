package com.edudev.finai.presentation.components.addTransactionScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.edudev.finai.R
import com.edudev.finai.ui.theme.FinAITheme
import com.edudev.finai.ui.theme.MintEmerald
import com.edudev.finai.ui.theme.OnSurfaceVariant
import com.edudev.finai.ui.theme.OnSurfaceWhite
import com.edudev.finai.ui.theme.Onyx
import com.edudev.finai.ui.theme.SurfaceContainerHighest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategorySelector(modifier: Modifier = Modifier, selectedCategory: String, onCategorySelected: (String) -> Unit, error: String?) {
    val categories =
        listOf(
            "Alimentação",
            "Transporte",
            "Moradia",
            "Saúde",
            "Educação",
            "Lazer",
            "Compras",
            "Outros"
        )

    var expanded by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        Text(
            text = stringResource(R.string.label_category),
            style =
            MaterialTheme.typography.labelSmall.copy(
                color = MintEmerald,
                letterSpacing = 1.sp,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
        )

        Box {
            Row(
                modifier =
                Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .background(SurfaceContainerHighest)
                    .clickable { expanded = !expanded }
                    .padding(horizontal = 16.dp, vertical = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.List,
                    contentDescription = null,
                    tint = OnSurfaceVariant,
                    modifier = Modifier.size(24.dp)
                )

                Text(
                    text = selectedCategory.ifBlank { stringResource(R.string.placeholder_category) },
                    modifier = Modifier.padding(start = 12.dp).weight(1f),
                    style =
                    MaterialTheme.typography.bodyMedium.copy(
                        color = if (selectedCategory.isBlank()) OnSurfaceVariant.copy(alpha = 0.5f) else OnSurfaceWhite,
                        fontWeight = FontWeight.Medium
                    )
                )

                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = null,
                    tint = OnSurfaceVariant,
                    modifier = Modifier.size(24.dp)
                )
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier =
                Modifier
                    .fillMaxWidth(0.9f)
                    .background(SurfaceContainerHighest)
            ) {
                categories.forEach { category ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = category,
                                color = OnSurfaceWhite,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        },
                        onClick = {
                            onCategorySelected(category)
                            expanded = false
                        }
                    )
                }
            }
        }

        error?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 8.dp, start = 16.dp)
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF111412)
@Composable
private fun CategorySelectorPreview() {
    FinAITheme {
        Box(Modifier.background(Onyx).padding(16.dp)) {
            CategorySelector(
                selectedCategory = "",
                onCategorySelected = {},
                error = null
            )
        }
    }
}
