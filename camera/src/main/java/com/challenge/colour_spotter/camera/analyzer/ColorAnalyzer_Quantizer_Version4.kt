package com.challenge.colour_spotter.camera.analyzer

import android.graphics.Bitmap
import android.graphics.Color
import com.challenge.colour_spotter.camera.analyzer.base.ColorQuantizerAnalyzerBase
import com.challenge.colour_spotter.camera.extension.toHexColor

/**
    color dominated: #000000
    Execution time: 4150 ms
    color dominated: #000000
    Execution time: 4167 ms

    with real device
    color dominated: #202020
    Execution time: 853 ms
    color dominated: #D0D0D0
    Execution time: 836 ms


 * This analyzer quantizes the colors in the image to reduce the number of
 * distinct colors, making it more efficient to find the most common color.
 * It then iterates through the pixels of the image, counting the occurrences
 * of each quantized color. The color with the highest frequency is considered
 * the dominant color.

 */
class ColorAnalyzer_Quantizer_Version4 (
    isEnable: Boolean,
    onColorDetected: suspend (String) -> Unit
) : ColorQuantizerAnalyzerBase(isEnable, onColorDetected) {

    override suspend fun analyzeColorFromImage(croppedBitmap: Bitmap): String {

        val mostCommonColor = getMostCommonColor(croppedBitmap)

        return mostCommonColor.toHexColor()
    }

    /**
     * Calculates the most common color in a bitmap using color quantization.
     */
    private fun getMostCommonColor(bitmap: Bitmap): Int {
        val width = bitmap.width
        val height = bitmap.height

        // Map to store the frequency of quantized colors
        val colorMap = mutableMapOf<Int, Int>()

        // Quantization step determines the "chunk" size for reducing color variance
        val quantizationStep = 16 // Adjust the chunk size for color quantization

        // Function to quantize colors by reducing RGB values to discrete steps
        val quantizeColor: (Int) -> Int = { color ->
            Color.rgb(
                Color.red(color) / quantizationStep * quantizationStep,
                Color.green(color) / quantizationStep * quantizationStep,
                Color.blue(color) / quantizationStep * quantizationStep
            )
        }

        // Loop through all pixels in the bitmap
        for (x in 0 until width) {
            for (y in 0 until height) {
                val pixel = bitmap.getPixel(x, y) // Get the pixel color
                val quantizedColor = quantizeColor(pixel)

                // Increment the count of the quantized color in the map
                colorMap[quantizedColor] = colorMap.getOrDefault(quantizedColor, 0) + 1
            }
        }

        // Find the most common color (the color with the highest frequency)
        return colorMap.maxByOrNull { it.value }?.key ?: Color.BLACK
    }

}