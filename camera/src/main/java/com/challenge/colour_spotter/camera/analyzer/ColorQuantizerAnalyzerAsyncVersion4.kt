package com.challenge.colour_spotter.camera.analyzer

import android.graphics.Bitmap
import android.graphics.Color
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
 */
class ColorQuantizerAnalyzerAsyncVersion4 (
    isEnable: Boolean,
    onColorDetected: suspend (String) -> Unit
) : ColorQuantizerAnalyzerBase(isEnable, onColorDetected) {

    override suspend fun analyzeColorFromImage(croppedBitmap: Bitmap): String {

        val mostCommonColor = getMostCommonColor(croppedBitmap)

        return mostCommonColor.toHexColor()
    }

    private suspend fun getMostCommonColor(bitmap: Bitmap): Int = withContext(Dispatchers.Default) {
        val width = bitmap.width
        val height = bitmap.height

        // Mappa per combinare i risultati
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

        // Suddividi il lavoro per righe (o blocchi di righe)
        val rowChunks = (0 until height).chunked(height / Runtime.getRuntime().availableProcessors())

        // Esegui l'elaborazione di ogni blocco in parallelo
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
        }.awaitAll() // Attendi tutti i risultati

        // Combina i risultati delle mappe locali
        for (localMap in results) {
            for ((color, count) in localMap) {
                colorMap[color] = colorMap.getOrDefault(color, 0) + count
            }
        }

        return@withContext colorMap.maxByOrNull { it.value }?.key ?: Color.BLACK
    }

}