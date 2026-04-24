package com.edudev.finai

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.edudev.finai.navigation.RootNavigationGraph
import com.edudev.finai.presentation.viewmodel.MainViewModel
import com.edudev.finai.ui.theme.FinAITheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val uiState by mainViewModel.uiState.collectAsStateWithLifecycle()

            FinAITheme {
                RootNavigationGraph(
                    isUserLoggedIn = { mainViewModel.isUserLoggedIn() },
                    onLogout = { mainViewModel.logout() }
                )
            }
        }
    }
}
