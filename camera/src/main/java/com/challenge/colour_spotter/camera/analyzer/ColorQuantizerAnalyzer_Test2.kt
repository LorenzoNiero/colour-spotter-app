package com.challenge.colour_spotter.camera.analyzer

import android.graphics.Bitmap
import android.graphics.Color
import androidx.camera.core.ImageProxy
import com.challenge.colour_spotter.camera.extension.toHexColor
import java.nio.ByteBuffer

/**
    color dominated: #000000
    Execution time: 4150 ms
    color dominated: #000000
    Execution time: 4167 ms
 */
class ColorQuantizerAnalyzer_Test2 (
    isEnable: Boolean,
    onColorDetected: suspend (String) -> Unit
) : ColorQuantizerAnalyzerBase(isEnable, onColorDetected) {

    override suspend fun analyzeColorFromImage(croppedBitmap: Bitmap): String {

        val mostCommonColor = getMostCommonColor(croppedBitmap)

        return mostCommonColor.toHexColor()
    }

    private fun getMostCommonColor(bitmap: Bitmap): Int {
        val width = bitmap.width
        val height = bitmap.height

        // Mappa dei colori quantizzati
        val colorMap = mutableMapOf<Int, Int>()

        // Split del colore nello spazio in chunk
        val quantizationStep = 16 // Regola la dimensione dei chunk
        val quantizeColor: (Int) -> Int = { color ->
            Color.rgb(
                Color.red(color) / quantizationStep * quantizationStep,
                Color.green(color) / quantizationStep * quantizationStep,
                Color.blue(color) / quantizationStep * quantizationStep
            )
        }

        for (x in 0 until width) {
            for (y in 0 until height) {
                val pixel = bitmap.getPixel(x, y)
                val quantizedColor = quantizeColor(pixel)

                colorMap[quantizedColor] = colorMap.getOrDefault(quantizedColor, 0) + 1
            }
        }

        // Trova il colore più comune
        return colorMap.maxByOrNull { it.value }?.key ?: Color.BLACK
    }

}