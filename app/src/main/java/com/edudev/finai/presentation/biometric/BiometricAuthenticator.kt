package com.edudev.finai.presentation.biometric

import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat

sealed class BiometricResult {
    data object Success : BiometricResult()
    data object Failed : BiometricResult()
    data class Error(val code: Int, val message: String) : BiometricResult()
}

class BiometricAuthenticator(private val activity: AppCompatActivity) {

    private lateinit var promptInfo: BiometricPrompt.PromptInfo
    private lateinit var biometricPrompt: BiometricPrompt

    fun isBiometricAuthAvailable(): Boolean {
        return BiometricManager.from(activity).canAuthenticate(
            BiometricManager.Authenticators.BIOMETRIC_STRONG
        ) == BiometricManager.BIOMETRIC_SUCCESS
    }

    fun prompt(onResult: (BiometricResult) -> Unit) {
        if (!isBiometricAuthAvailable()) {
            onResult(BiometricResult.Error(-1, "Biometria não disponível ou não cadastrada."))
            return
        }

        val executor = ContextCompat.getMainExecutor(activity)

        biometricPrompt = BiometricPrompt(
            activity,
            executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    onResult(BiometricResult.Success)
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    onResult(BiometricResult.Failed)
                }

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    onResult(BiometricResult.Error(errorCode, errString.toString()))
                }
            }
        )

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Login com Biometria")
            .setSubtitle("Toque no sensor para entrar")
            .setNegativeButtonText("Cancelar")
            .build()

        biometricPrompt.authenticate(promptInfo)
    }
}
