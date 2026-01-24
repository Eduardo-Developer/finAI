package com.edudev.finai.domain.model

import android.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class CategorySpending(
    val category: String,
    val total: Double,
    val percentage: Float,
    val icon: ImageVector? = null
)
