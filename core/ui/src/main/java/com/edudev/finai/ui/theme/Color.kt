package com.edudev.finai.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

// Onyx & Emerald Precision Palette
val Onyx = Color(0xFF111412)
val Emerald = Color(0xFF00E475)
val MintEmerald = Color(0xFF83D7B2)

// Tonal Architecture for Obsidian Ledger
val SurfaceContainerLow = Color(0xFF191C1A)
val SurfaceContainer = Color(0xFF1D201E)
val SurfaceContainerHigh = Color(0xFF282B28)
val SurfaceContainerHighest = Color(0xFF323533)

// Secondary & Semantic
val Jade = Color(0xFF00A550) // For gradients
val ErrorRed = Color(0xFFE11D48)

// On-Surface (Tonal White to prevent eye strain)
val OnSurfaceWhite = Color(0xFFE1E3DF)
val OnSurfaceVariant = Color(0xFFBEC9C1)

val LightColorScheme =
    lightColorScheme(
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
        onError = Color.White
    )

val DarkColorScheme =
    darkColorScheme(
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
