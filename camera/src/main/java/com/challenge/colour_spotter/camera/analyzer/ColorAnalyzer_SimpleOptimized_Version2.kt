package com.challenge.colour_spotter.camera.analyzer

import android.graphics.Bitmap
import android.graphics.Color
import com.challenge.colour_spotter.camera.analyzer.base.ColorQuantizerAnalyzerBase
import com.challenge.colour_spotter.camera.extension.toHexColor

/**
 * BIG bitmap
Execution time: 1933 ms
color look for: #040404
Execution time: 1931 ms
color look for: #040404
Execution time: 1928 ms
color look for: #040404
Execution time: 1930 ms

See for detail [ColorAnalyzer_Simple_Version1]
 *
 */
class ColorAnalyzer_SimpleOptimized_Version2(
    isEnable: Boolean,
    onColorDetected: suspend (String) -> Unit
) : ColorQuantizerAnalyzerBase(isEnable, onColorDetected) {

    override suspend fun analyzeColorFromImage(croppedBitmap: Bitmap): String {

        val dominantColor = getDominantColor(croppedBitmap)

        return dominantColor.toHexColor()
    }

    private fun getDominantColor(bitmap: Bitmap): Int {
        val scaledBitmap = bitmap
        val width = scaledBitmap.width
        val height = scaledBitmap.height

        val colorCount = (0 until height).flatMap { y ->
            (0 until width).map { x -> scaledBitmap.getPixel(x, y) }
        }.groupingBy { Color.rgb(Color.red(it), Color.green(it), Color.blue(it)) }
            .eachCount()

        return colorCount.maxByOrNull { it.value }?.key ?: Color.BLACK
    }
}