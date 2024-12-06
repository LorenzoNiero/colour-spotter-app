package com.challenge.colour_spotter.camera.analyzer

import android.graphics.Bitmap
import android.graphics.Color
import com.challenge.colour_spotter.camera.analyzer.base.ColorQuantizerAnalyzerBase
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
 * A simple implementation of [ColorQuantizerAnalyzerBase] that analyzes an image
 * to identify the dominant color by iterating through all pixels.
 *
 * This analyzer does **not** use color quantization techniques. Instead, it directly
 * iterates through each pixel of the image, counting the occurrences of each color.
 * The color with the highest frequency is considered the dominant color.
 *
 * Note: This is a basic implementation and might not be as efficient as
 * analyzers that employ color quantization for larger images.
 *
 */
class ColorAnalyzer_Simple_Version1(
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