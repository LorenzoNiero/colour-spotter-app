package com.challenge.colour_spotter.camera.analyzer

import android.graphics.Bitmap
import android.graphics.Color
import com.challenge.colour_spotter.camera.extension.toHexColor

/**
 * SMALL bitmap
 * Execution time: 19 ms
 * color look for: #B07742
 * Execution time: 19 ms
 * color look for: #B07742
 * Execution time: 20 ms
 *
 * BIG bitmap
 * Execution time: 1736 ms
 * color look for: #040404
 * Execution time: 1738 ms
 * color look for: #040404
 * Execution time: 1764 ms
 *
 */
class ColorQuantizerAnalyzer_EasyVersion1(
    isEnable: Boolean,
    onColorDetected: suspend (String) -> Unit
) : ColorQuantizerAnalyzerBase(isEnable, onColorDetected) {

    override suspend fun analyzeColorFromImage(croppedBitmap: Bitmap): String {

        val dominantColor = getDominantColor(croppedBitmap)

        return dominantColor.toHexColor()
    }

    private fun getDominantColor(bitmap: Bitmap): Int {
        val scaledBitmap = bitmap
        val pixelCount = IntArray(scaledBitmap.width * scaledBitmap.height)
        scaledBitmap.getPixels(pixelCount, 0, scaledBitmap.width, 0, 0, scaledBitmap.width, scaledBitmap.height)

        val colorCount = mutableMapOf<Int, Int>()
        for (pixel in pixelCount) {
            val color = Color.rgb(Color.red(pixel), Color.green(pixel), Color.blue(pixel))
            colorCount[color] = (colorCount[color] ?: 0) + 1
        }

        return colorCount.maxByOrNull { it.value }?.key ?: Color.BLACK
    }
}