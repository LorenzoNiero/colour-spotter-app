package com.challenge.colour_spotted.spotted.presentation

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import androidx.annotation.OptIn
import androidx.camera.core.CameraEffect
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import java.nio.ByteBuffer
import java.util.concurrent.TimeUnit
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt


class ColorQuantizerAnalyzer(
    private val onColorDetected: (String) -> Unit // Callback per restituire il colore rilevato in formato hex
) : ImageAnalysis.Analyzer {

    private var lastAnalyzedTimeStamp = 0L

    override fun analyze(image: ImageProxy) {

        val currentTimestamp = System.currentTimeMillis()
        if (currentTimestamp - lastAnalyzedTimeStamp >= TimeUnit.MILLISECONDS.toMillis(SCAN_DELAY_MILLIS)) {
            val bitmap = image.toBitmap() ?: return

            // Prende la parte centrale dell'immagine di 100x100
            val croppedBitmap = getCentralCrop(bitmap, 100, 100)

            // Analizza il colore predominante
            val dominantColor = getDominantColor(bitmap)

            // Converti il colore in hex
            val hexColor = String.format("#%06X", 0xFFFFFF and dominantColor)

            // Restituisce il valore del colore tramite callback
            onColorDetected(hexColor)

            // Chiude l'immagine per consentire il prossimo frame

            lastAnalyzedTimeStamp = currentTimestamp
        }
        image.close()
    }

    // Metodo per ottenere una porzione centrale dell'immagine
    private fun getCentralCrop(bitmap: Bitmap, cropWidth: Int, cropHeight: Int): Bitmap {
        val x = max(0, (bitmap.width - cropWidth) / 2)
        val y = max(0, (bitmap.height - cropHeight) / 2)

        // Assicurati che il ritaglio non vada oltre i bordi
        val width = min(cropWidth, bitmap.width - x)
        val height = min(cropHeight, bitmap.height - y)

        return Bitmap.createBitmap(bitmap, x, y, width, height)
    }

    // Metodo per ottenere il colore predominante
    private fun getDominantColor(bitmap: Bitmap): Int {
        val scaledBitmap = Bitmap.createScaledBitmap(bitmap, 50, 50, false)
        val pixelCount = IntArray(scaledBitmap.width * scaledBitmap.height)
        scaledBitmap.getPixels(pixelCount, 0, scaledBitmap.width, 0, 0, scaledBitmap.width, scaledBitmap.height)

        val colorCount = mutableMapOf<Int, Int>()
        for (pixel in pixelCount) {
            val color = Color.rgb(Color.red(pixel), Color.green(pixel), Color.blue(pixel))
            colorCount[color] = (colorCount[color] ?: 0) + 1
        }

        return colorCount.maxByOrNull { it.value }?.key ?: Color.BLACK
    }

    // Metodo per convertire l'immagine a Bitmap
    private fun ImageProxy.toBitmap(): Bitmap? {
        val yBuffer = planes[0].buffer // Y
        val uBuffer = planes[1].buffer // U
        val vBuffer = planes[2].buffer // V

        val ySize = yBuffer.remaining()
        val uSize = uBuffer.remaining()
        val vSize = vBuffer.remaining()

        val nv21 = ByteArray(ySize + uSize + vSize)

        yBuffer.get(nv21, 0, ySize)
        vBuffer.get(nv21, ySize, vSize)
        uBuffer.get(nv21, ySize + vSize, uSize)

        val yuvImage = android.graphics.YuvImage(nv21, android.graphics.ImageFormat.NV21, width, height, null)
        val out = java.io.ByteArrayOutputStream()
        yuvImage.compressToJpeg(android.graphics.Rect(0, 0, width, height), 100, out)
        val imageBytes = out.toByteArray()
        return android.graphics.BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
    }

    companion object {
        const val SCAN_DELAY_MILLIS = 1000L
    }
}


class CameraOverlayEffect(private val context: Context) : CameraEffect.Builder(CameraEffect.PREVIEW) {

    init {
        setProcessor(CustomSurfaceProcessor())
    }

    private inner class CustomSurfaceProcessor : SurfaceProcessor {
        override fun onInputSurface(surface: android.view.Surface) {
            // Not used in this example, but you could initialize or release resources here if needed
        }

        override fun onOutputSurface(surface: android.view.Surface) {
            // Process to apply the effect
            val canvas: Canvas = surface.lockCanvas(null)
            val paint = Paint().apply {
                color = Color.BLACK
                alpha = 150 // Semi-trasparente
            }

            // Disegna l'overlay semi-trasparente
            canvas.drawRect(0f, 0f, canvas.width.toFloat(), canvas.height.toFloat(), paint)

            // Calcola la posizione del buco centrale di 100x100 pixel
            val centerX = canvas.width / 2f
            val centerY = canvas.height / 2f
            val halfWidth = 50f // 100 / 2
            val halfHeight = 50f

            paint.color = Color.TRANSPARENT
            paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)

            // Disegna il buco centrale
            canvas.drawRect(
                centerX - halfWidth,
                centerY - halfHeight,
                centerX + halfWidth,
                centerY + halfHeight,
                paint
            )

            // Rilascia il canvas
            surface.unlockCanvasAndPost(canvas)
        }
    }

    // Funzione helper per ottenere un CameraEffect dalla classe
    fun getCameraEffect(): CameraEffect {
        return build()
    }
}