package com.edudev.finai

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.edudev.finai.domain.repository.AuthRepository
import com.edudev.finai.navigation.RootNavigationGraph
import com.edudev.finai.presentation.viewmodel.SettingsViewModel
import com.edudev.finai.ui.theme.FinAITheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity() : AppCompatActivity() {

    @Inject
    lateinit var authRepository: AuthRepository
    private val settingsViewModel: SettingsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val isDarkTheme by settingsViewModel.isDarkTheme.collectAsStateWithLifecycle()
            FinAITheme(darkTheme = isDarkTheme) {
                RootNavigationGraph(authRepository = authRepository)
            }
        }
    }
}
