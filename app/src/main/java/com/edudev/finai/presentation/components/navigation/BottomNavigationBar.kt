package com.edudev.finai.presentation.components.navigation

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.edudev.finai.presentation.navigation.Screen
import com.edudev.finai.ui.theme.Emerald
import com.edudev.finai.ui.theme.FinAITheme
import com.edudev.finai.ui.theme.Jade
import com.edudev.finai.ui.theme.OnSurfaceVariant
import com.edudev.finai.ui.theme.Onyx
import com.edudev.finai.ui.theme.SurfaceContainerHigh

private val NavBarHeight = 72.dp
private val FabSize = 60.dp
private val FabElevation = 24.dp

// Notch radius = FAB radius + a small gap so the bar doesn't touch the FAB
private val NotchRadius = (FabSize / 2) + 8.dp

/**
 * Draws the nav bar background with a smooth semicircular notch at the center top.
 *
 * Geometry: The FAB center sits exactly at the top edge of the canvas (y=0).
 * The notch is a downward-facing semicircle of [notchRadiusPx].
 *
 * Each half of the semicircle is approximated by a cubic bezier using the
 * standard k = 0.5523 constant for quarter-circle approximation.
 */
private fun DrawScope.drawNotchedBackground(color: Color, notchRadiusPx: Float) {
    val w = size.width
    val h = size.height
    val cx = w / 2f
    val cornerRadius = 20.dp.toPx() // Smoothness of the "shoulders"

    val path = Path().apply {
        moveTo(0f, 0f)
        // ── Left flat part ──────────────────────────────────────────────────
        lineTo(cx - notchRadiusPx - cornerRadius, 0f)

        // ── Left shoulder ───────────────────────────────────────────────────
        quadraticBezierTo(
            cx - notchRadiusPx,
            0f,
            cx - notchRadiusPx,
            cornerRadius
        )

        // ── Main Arc (The Cradle) ───────────────────────────────────────────
        // We center the arc slightly below the top to give it depth
        arcTo(
            rect = androidx.compose.ui.geometry.Rect(
                left = cx - notchRadiusPx,
                top = cornerRadius - notchRadiusPx,
                right = cx + notchRadiusPx,
                bottom = cornerRadius + notchRadiusPx
            ),
            startAngleDegrees = 180f,
            sweepAngleDegrees = -180f,
            forceMoveTo = false
        )

        // ── Right shoulder ──────────────────────────────────────────────────
        quadraticBezierTo(
            cx + notchRadiusPx,
            0f,
            cx + notchRadiusPx + cornerRadius,
            0f
        )

        // ── Right flat part and closing ─────────────────────────────────────
        lineTo(w, 0f)
        lineTo(w, h)
        lineTo(0f, h)
        close()
    }

    drawPath(path = path, color = color)
}

@Composable
fun BottomNavigationBar(navController: NavHostController, onAddClick: () -> Unit) {
    val leftItems = listOf(Screen.Dashboard, Screen.History)
    val rightItems = listOf(Screen.Settings)

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val fabOverlapDp = FabSize / 2 // how much the FAB pokes above the nav bar

    Box(
        modifier =
        Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .height(NavBarHeight)
    ) {
        // ---- Canvas-drawn notched background ----
        Canvas(
            modifier =
            Modifier
                .fillMaxWidth()
                .height(NavBarHeight)
                .align(Alignment.BottomCenter)
        ) {
            drawNotchedBackground(
                color = SurfaceContainerHigh,
                notchRadiusPx = NotchRadius.toPx()
            )
        }

        // ---- Navigation Items Row ----
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .height(NavBarHeight),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left side slots (Always 2)
            leftItems.forEach { screen ->
                NavigationBarItem(
                    modifier = Modifier.weight(1f),
                    icon = { Icon(requireNotNull(screen.icon), contentDescription = null) },
                    label = { Text(requireNotNull(screen.title), style = MaterialTheme.typography.labelSmall) },
                    selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Emerald,
                        selectedTextColor = Emerald,
                        unselectedIconColor = OnSurfaceVariant,
                        unselectedTextColor = OnSurfaceVariant,
                        indicatorColor = Color.Transparent
                    ),
                    onClick = {
                        navController.navigate(screen.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = false
                            }
                            launchSingleTop = true
                            restoreState = false
                        }
                    }
                )
            }
            if (leftItems.size < 2) Spacer(modifier = Modifier.weight(1f))

            // Spacer for FAB area - Increased for better harmony
            Spacer(modifier = Modifier.weight(1.5f))

            // Right side slots (Always 2)
            if (rightItems.size < 2) Spacer(modifier = Modifier.weight(1f))
            rightItems.forEach { screen ->
                NavigationBarItem(
                    modifier = Modifier.weight(1f),
                    icon = { Icon(requireNotNull(screen.icon), contentDescription = null) },
                    label = { Text(requireNotNull(screen.title), style = MaterialTheme.typography.labelSmall) },
                    selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Emerald,
                        selectedTextColor = Emerald,
                        unselectedIconColor = OnSurfaceVariant,
                        unselectedTextColor = OnSurfaceVariant,
                        indicatorColor = Color.Transparent
                    ),
                    onClick = {
                        navController.navigate(screen.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = false
                            }
                            launchSingleTop = true
                            restoreState = false
                        }
                    }
                )
            }
        }

        // ---- Centered FAB ----
        Box(
            modifier =
            Modifier
                .size(FabSize)
                .align(Alignment.Center)
                .shadow(
                    elevation = FabElevation,
                    shape = CircleShape,
                    ambientColor = Emerald.copy(alpha = 0.2f),
                    spotColor = Emerald.copy(alpha = 0.35f)
                )
                .clip(CircleShape)
                .background(Brush.radialGradient(listOf(Emerald, Jade)))
                .clickable { onAddClick() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add Transaction",
                tint = Onyx,
                modifier = Modifier.size(28.dp)
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF111412)
@Composable
private fun BottomNavigationBarPreview() {
    FinAITheme {
        BottomNavigationBar(
            navController = rememberNavController(),
            onAddClick = {}
        )
    }
}
