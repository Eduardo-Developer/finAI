package com.edudev.finai.ui.theme

import androidx.compose.ui.graphics.Color

// Paleta de Cores Simplificada
val FinAIGreen = Color(0xFF117B5B) // Verde principal e vibrante
val FinAISoftGreen = Color(0xFFEFFCEF) // Verde mais suave para papéis secundários
val FinAIDarkGreen = Color(0xFF1B5E20) // Verde escuro para contraste

val White = Color.White
val Black = Color.Black
val LightGray = Color(0xFFF0F0F0) // Fundo claro e superfícies
val DarkGray = Color(0xFF121212) // Fundo escuro
val TextColorDark = Color(0xFFE0E0E0) // Cor do texto no tema escuro
val SecondaryRed = Color(0xFFE11D48)

val LightColorScheme = androidx.compose.material3.lightColorScheme(
    primary = FinAIGreen,
    onPrimary = White,
    primaryContainer = FinAISoftGreen,
    onPrimaryContainer = Black,
    secondary = FinAISoftGreen,
    onSecondary = Black,
    tertiary = FinAIDarkGreen,
    onTertiary = White,
    background = LightGray,
    onBackground = Black,
    surface = White,
    onSurface = Black,
    surfaceDim = SecondaryRed
)

val DarkColorScheme = androidx.compose.material3.darkColorScheme(
    primary = FinAIGreen,
    onPrimary = White,
    primaryContainer = FinAIDarkGreen,
    onPrimaryContainer = TextColorDark,
    secondary = FinAISoftGreen,
    onSecondary = Black,
    tertiary = FinAIDarkGreen,
    onTertiary = TextColorDark,
    background = DarkGray,
    onBackground = TextColorDark,
    surface = DarkGray,
    onSurface = TextColorDark,
    surfaceDim = SecondaryRed
)
