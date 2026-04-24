package com.edudev.finai.ui.modifiers

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.asImageBitmap
import kotlin.random.Random

/**
 * Applies a subtle noise/grain texture to the background.
 * Matches the "Obsidian Ledger" aesthetic.
 */
fun Modifier.noiseBackground(opacity: Float = 0.05f): Modifier = composed {
    val noiseBitmap =
        remember {
            createNoiseBitmap(200, 200)
        }

    this.drawBehind {
        val imageBitmap = noiseBitmap.asImageBitmap()
        val iterationsX = (size.width / 200).toInt() + 1
        val iterationsY = (size.height / 200).toInt() + 1

        for (x in 0 until iterationsX) {
            for (y in 0 until iterationsY) {
                drawImage(
                    image = imageBitmap,
                    topLeft = androidx.compose.ui.geometry.Offset(x * 200f, y * 200f),
                    alpha = opacity
                )
            }
        }
    }
}

private fun createNoiseBitmap(width: Int, height: Int): Bitmap {
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    val random = Random(42) // Deterministic noise
    val paint = Paint()

    for (x in 0 until width) {
        for (y in 0 until height) {
            val brightness = random.nextInt(256)
            paint.color = android.graphics.Color.argb(255, brightness, brightness, brightness)
            canvas.drawPoint(x.toFloat(), y.toFloat(), paint)
        }
    }
    return bitmap
}
