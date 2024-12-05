package com.challenge.colour_spotter.camera.analyzer

import android.graphics.Bitmap
import android.graphics.Color
import androidx.palette.graphics.Palette
import com.challenge.colour_spotter.camera.extension.toHexColor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
Execution time: 35 ms
    color dominated: #C8C8C8
    Execution time: 35 ms
    color dominated: #282020
    Execution time: 33 ms
    color dominated: #C8C8C8
    Execution time: 30 ms
 */
class ColorQuantizerAnalyzerWithPaletteVersion4 (
    isEnable: Boolean,
    onColorDetected: suspend (String) -> Unit
) : ColorQuantizerAnalyzerBase(isEnable, onColorDetected) {

    override suspend fun analyzeColorFromImage(croppedBitmap: Bitmap): String {

        val mostCommonColor = getMostCommonColor(croppedBitmap)
        return mostCommonColor.toHexColor()
    }

    private suspend fun getMostCommonColor(bitmap: Bitmap): Int = withContext(Dispatchers.Default) {
        val palette = Palette.from(bitmap).generate()

        val colorDominant = palette.getDominantColor(Color.BLACK)

        return@withContext colorDominant
    }

}