package com.edudev.finai.presentation.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.edudev.finai.ui.theme.FinAITheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FinAiTopAppBar(
    title: @Composable () -> Unit,
    actions: @Composable (RowScope.() -> Unit) = {},
    navigationIcon: @Composable () -> Unit = {}
) {
    TopAppBar(
        title = title,
        actions = actions,
        navigationIcon = navigationIcon,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            titleContentColor = MaterialTheme.colorScheme.onBackground,
            actionIconContentColor = MaterialTheme.colorScheme.onBackground,
            navigationIconContentColor = MaterialTheme.colorScheme.onBackground
        )
    )
}

@Preview
@Composable
private fun FinAiTopAppBarPreview() {
    FinAITheme {
        FinAiTopAppBar(title = { androidx.compose.material3.Text("Meu App") })
    }
}
