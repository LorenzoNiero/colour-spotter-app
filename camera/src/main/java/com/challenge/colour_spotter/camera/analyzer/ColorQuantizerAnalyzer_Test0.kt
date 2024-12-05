package com.challenge.colour_spotter.camera.analyzer

import android.graphics.Bitmap
import android.graphics.Color
import androidx.camera.view.PreviewView
import com.challenge.colour_spotter.camera.extension.toHexColor

/**
 *  #B17540
 *  Execution time: 32 ms
 *  Execution time: 34 ms
 */class ColorQuantizerAnalyzer_Test0(
    isEnable: Boolean,
    onColorDetected: suspend (String) -> Unit
) : ColorQuantizerAnalyzerBase(isEnable, onColorDetected) {

    private var lastAnalyzedTimeStamp = 0L
    override suspend fun analyzeColorFromImage(croppedBitmap: Bitmap): String {

        val dominantColor = getDominantColor(croppedBitmap)

        return dominantColor.toHexColor()
    }

    private fun getDominantColor(bitmap: Bitmap): Int {
        val scaledBitmap = Bitmap.createScaledBitmap(bitmap, 50, 50, true)
        val width = scaledBitmap.width
        val height = scaledBitmap.height

        val colorCount = (0 until height).flatMap { y ->
            (0 until width).map { x -> scaledBitmap.getPixel(x, y) }
        }.groupingBy { Color.rgb(Color.red(it), Color.green(it), Color.blue(it)) }
            .eachCount()

        scaledBitmap.recycle() // Recycle the scaled bitmap
        return colorCount.maxByOrNull { it.value }?.key ?: Color.BLACK
//
//        return colorCount.maxByOrNull { it.value }?.key ?: Color.BLACK
//        val scaledBitmap = Bitmap.createScaledBitmap(bitmap, 50, 50, false)
//        val pixelCount = IntArray(scaledBitmap.width * scaledBitmap.height)
//        scaledBitmap.getPixels(pixelCount, 0, scaledBitmap.width, 0, 0, scaledBitmap.width, scaledBitmap.height)
//
//        val colorCount = mutableMapOf<Int, Int>()
//        for (pixel in pixelCount) {
//            val color = Color.rgb(Color.red(pixel), Color.green(pixel), Color.blue(pixel))
//            colorCount[color] = (colorCount[color] ?: 0) + 1
//        }
//
//        return colorCount.maxByOrNull { it.value }?.key ?: Color.BLACK
    }
}