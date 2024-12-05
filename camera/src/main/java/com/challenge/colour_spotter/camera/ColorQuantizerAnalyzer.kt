package com.challenge.colour_spotter.camera

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Matrix
import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.view.PreviewView

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import java.util.concurrent.TimeUnit
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

class ColorQuantizerAnalyzer(
    val previewView: PreviewView,
    isEnable: Boolean,
    private val onColorDetected: suspend (String) -> Unit
) : ImageAnalysis.Analyzer {

    var enable = isEnable

    private var lastAnalyzedTimeStamp = 0L

    override fun analyze(image: ImageProxy) {

        val currentTimestamp = System.currentTimeMillis()
        try {
            if (enable && currentTimestamp - lastAnalyzedTimeStamp >= TimeUnit.MILLISECONDS.toMillis(
                    SCAN_DELAY_MILLIS
                )
            ) {
                runBlocking (Dispatchers.IO) {
                    val bitmap = image.toBitmapRotated()

                    val croppedBitmap = getCentralCrop(bitmap)

                    val dominantColor = getDominantColor(croppedBitmap)

                    val hexColor = String.format("#%06X", 0xFFFFFF and dominantColor)

                    onColorDetected(hexColor)

                    lastAnalyzedTimeStamp = currentTimestamp
                }
            }
        }catch (e: Exception) {
            Log.e("ColorQuantizerAnalyzer", "analyze: ", e)
        } finally {
            image.close()
        }

    }

    private fun ImageProxy.toBitmapRotated(): Bitmap {
        val bitmap = toBitmap()
        val matrix = Matrix()
        matrix.postRotate(imageInfo.rotationDegrees.toFloat())
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    private fun getCentralCrop(bitmap: Bitmap): Bitmap {
        val cropSize = minOf(
            abs(bitmap.width), abs(bitmap.height)
        ) / 8
        val x = max(0, (bitmap.width - cropSize) / 2)
        val y = max(0, (bitmap.height - cropSize) / 2)

        val width = min(cropSize, bitmap.width - x)
        val height = min(cropSize, bitmap.height - y)

        return Bitmap.createBitmap(bitmap, x, y, width, height)
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

    companion object {
        const val SCAN_DELAY_MILLIS = 1000L
    }
}