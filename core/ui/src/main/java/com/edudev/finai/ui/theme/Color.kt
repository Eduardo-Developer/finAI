package com.edudev.finai.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme

// Onyx & Emerald Precision Palette
val Onyx = Color(0xFF111412)
val Emerald = Color(0xFF00E475)
val MintEmerald = Color(0xFF83D7B2)

// Tonal Architecture for Obsidian Ledger
val SurfaceContainerLow = Color(0xFF151916)
val SurfaceContainer = Color(0xFF1C211E)
val SurfaceContainerHigh = Color(0xFF262C28)
val SurfaceContainerHighest = Color(0xFF323933)

// Secondary & Semantic
val Jade = Color(0xFF00A550) // For gradients
val ErrorRed = Color(0xFFE11D48)

// On-Surface (Tonal White to prevent eye strain)
val OnSurfaceWhite = Color(0xFFE0E6E2)
val OnSurfaceVariant = Color(0xFF94A399)

val LightColorScheme = lightColorScheme(
    primary = Emerald,
    onPrimary = Onyx,
    primaryContainer = MintEmerald,
    onPrimaryContainer = Onyx,
    secondary = MintEmerald,
    onSecondary = Onyx,
    background = Color(0xFFF7F9F8),
    onBackground = Onyx,
    surface = Color.White,
    onSurface = Onyx,
    error = ErrorRed,
    onError = Color.White,
)

val DarkColorScheme = darkColorScheme(
    primary = Emerald,
    onPrimary = Onyx,
    primaryContainer = Jade,
    onPrimaryContainer = OnSurfaceWhite,
    secondary = MintEmerald,
    onSecondary = Onyx,
    background = Onyx,
    onBackground = OnSurfaceWhite,
    surface = Onyx,
    onSurface = OnSurfaceWhite,
    surfaceVariant = SurfaceContainer,
    onSurfaceVariant = OnSurfaceVariant,
    error = ErrorRed,
    onError = Onyx,
    outline = SurfaceContainerHigh,
    surfaceContainerLow = SurfaceContainerLow,
    surfaceContainer = SurfaceContainer,
    surfaceContainerHigh = SurfaceContainerHigh,
    surfaceContainerHighest = SurfaceContainerHighest
)
