package com.challenge.colour_spotter.camera.analyzer

import android.graphics.Bitmap
import android.graphics.Color
import com.challenge.colour_spotter.camera.analyzer.base.ColorQuantizerAnalyzerBase
import com.challenge.colour_spotter.camera.extension.toHexColor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext

/**
    Execution time: 494 ms
    color dominated: #C0C0C0
    Execution time: 511 ms
    color dominated: #C0C0C0

    with real device:
    Execution time: 196 ms
    color dominated: #D0D0D0
    Execution time: 241 ms
    color dominated: #D0D0D0
    Execution time: 207 ms
    Execution time: 191 ms>

    See [ColorAnalyzer_Quantizer_Version4] for more details but this use coroutine for parallel processing

 */
class ColorAnalyzer_QuantizerAsync_Version5 (
    isEnable: Boolean,
    onColorDetected: suspend (String) -> Unit
) : ColorQuantizerAnalyzerBase(isEnable, onColorDetected) {

    // Number of coroutines used for parallel processing
    val numberCoroutinesAsync = 16

    override suspend fun analyzeColorFromImage(croppedBitmap: Bitmap): String {

        val mostCommonColor = getMostCommonColor(croppedBitmap)

        return mostCommonColor.toHexColor()
    }

    /**
     * Finds the most common color in the given Bitmap using parallel processing.
     */
    private suspend fun getMostCommonColor(bitmap: Bitmap): Int = withContext(Dispatchers.Default) {
        val width = bitmap.width
        val height = bitmap.height

        // Global map to store the frequency of quantized colors
        val colorMap = mutableMapOf<Int, Int>()

        // Step size for color quantization
        val quantizationStep = 16  // Adjust to change the granularity of quantization

        // Function to quantize a color by reducing RGB values into discrete steps
        val quantizeColor: suspend (Int) -> Int =  { color ->
            Color.rgb(
                Color.red(color) / quantizationStep * quantizationStep,
                Color.green(color) / quantizationStep * quantizationStep,
                Color.blue(color) / quantizationStep * quantizationStep
            )
        }

        // Divide the rows of the image into chunks for parallel processing
        val rowChunks = (0 until height).chunked(height / numberCoroutinesAsync)

        // Process each chunk in a separate coroutine
        val results = rowChunks.map { rows ->
            async {
                val localColorMap = mutableMapOf<Int, Int>()

                for (y in rows) {
                    for (x in 0 until width) {
                        val pixel = bitmap.getPixel(x, y)
                        val quantizedColor = quantizeColor(pixel)

                        localColorMap[quantizedColor] = localColorMap.getOrDefault(quantizedColor, 0) + 1
                    }
                }
                localColorMap
            }
        }.awaitAll() // Return the local map for this chunk

        // Merge all local color maps into the global color map
        for (localMap in results) {
            for ((color, count) in localMap) {
                colorMap[color] = colorMap.getOrDefault(color, 0) + count
            }
        }

        return@withContext colorMap.maxByOrNull { it.value }?.key ?: Color.BLACK
    }

}