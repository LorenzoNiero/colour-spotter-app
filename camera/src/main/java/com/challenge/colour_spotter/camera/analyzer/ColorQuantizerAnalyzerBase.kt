package com.challenge.colour_spotter.camera.analyzer

import android.graphics.Bitmap
import android.graphics.Matrix
import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.challenge.colour_spotter.camera.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import java.util.concurrent.TimeUnit
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.system.measureTimeMillis

/**
 * Base class for color quantizer analyzers.
 *
 * This class provides a basic implementation of an ImageAnalysis.Analyzer that periodically analyzes
 * camera frames to detect the dominant color. It handles enabling/disabling analysis, throttling
 * the analysis frequency, and invoking a callback with the detected color.
 *
 * Subclasses are responsible for implementing the actual color analysis logic in the
 * `analyzeColorFromImage` method.
 *
 * @param previewView The PreviewView used for displaying the camera preview.
 * @param isEnableOnStart Whether color analysis should be enabled when the analyzer starts.
 * @param onColorDetected A callback function that is invoked when a color is detected. The function
 * receives the detected color as a hexadecimal string.
 */
abstract class ColorQuantizerAnalyzerBase(
    isEnableOnStart: Boolean,
    private val onColorDetected: suspend (String) -> Unit
) : ImageAnalysis.Analyzer {

    var enable = isEnableOnStart
    private var lastAnalyzedTimeStamp = 0L

    override fun analyze(imageProxy: ImageProxy) {

        val currentTimestamp = System.currentTimeMillis()
        try {
            if (enable && currentTimestamp - lastAnalyzedTimeStamp >= TimeUnit.MILLISECONDS.toMillis(
                    SCAN_DELAY_MILLIS
                )
            ) {
                runBlocking(Dispatchers.IO) {
                    val bitmap = imageProxy.toBitmapRotated()

                    val croppedBitmap = getCentralCrop(bitmap)

                    //TODO: uncomment to see the execution time to analyze the color
//                    val executionTime = measureTimeMillis {
                    val colorHex = analyzeColorFromImage(croppedBitmap)
                    println("color dominated: $colorHex")

                    onColorDetected(colorHex)
//
//                    }
//
//                    println("Execution time: $executionTime ms")

                    lastAnalyzedTimeStamp = currentTimestamp
                }
            }
        } catch (e: Exception) {
            Log.e("ColorQuantizerAnalyzerBase", "error analyze: ", e)
        } finally {
            imageProxy.close()
        }

    }

    /**
     * Analyzes the given image proxy and returns the detected color as a hexadecimal string.
     *
     * This method must be implemented by subclasses to provide the actual color analysis logic.
     *
     * @param croppedBitmap The cropped bitmap to analyze.
     * @return The detected color as a hexadecimal string.
     */
    abstract suspend fun analyzeColorFromImage(croppedBitmap: Bitmap) : String

    companion object {
        /**
         * The delay between color analysis scans, in milliseconds.
         */
        const val SCAN_DELAY_MILLIS = 1000L
    }


    protected fun ImageProxy.toBitmapRotated(): Bitmap {
        val bitmap = toBitmap()
        val matrix = Matrix()
        matrix.postRotate(imageInfo.rotationDegrees.toFloat())
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    protected fun getCentralCrop(bitmap: Bitmap): Bitmap {
        val cropSize = (minOf(
            abs(bitmap.width), abs(bitmap.height)
        ) * Constants.percentDivisionImage).toInt()
        val x = max(0, (bitmap.width - cropSize) / 2)
        val y = max(0, (bitmap.height - cropSize) / 2)

        val width = min(cropSize, bitmap.width - x)
        val height = min(cropSize, bitmap.height - y)

        return Bitmap.createBitmap(bitmap, x, y, width, height)
    }

}