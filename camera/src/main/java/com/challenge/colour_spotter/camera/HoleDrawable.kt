package com.challenge.colour_spotter.camera

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.drawable.Drawable

class HoleDrawable() : Drawable() {
    private val paint = Paint().apply {
        color = Color.BLACK
        alpha = (255 * 0.5).toInt()  //opacity
        isAntiAlias = true
    }

    override fun draw(canvas: Canvas) {
        val bounds = canvas.getClipBounds()

        // Disegna il rettangolo nero semi-trasparente
        canvas.drawRect(bounds, paint)

        // Calcola le dimensioni e la posizione del cerchio trasparente
        val centerX = bounds.exactCenterX()
        val centerY = bounds.exactCenterY()
        val radius = minOf(bounds.width(), bounds.height()) / 8f

        // Imposta la modalità di trasferimento per creare il foro trasparente
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)

        // Disegna il cerchio trasparente
        canvas.drawCircle(centerX, centerY, radius, paint)

        // Ripristina la modalità di trasferimento originale
        paint.xfermode = null
    }

    override fun setAlpha(alpha: Int) {
        paint.alpha = alpha
        invalidateSelf()
    }

    @Deprecated("Deprecated in Java")
    override fun getOpacity(): Int {
        return paint.alpha
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        paint.colorFilter = colorFilter
        invalidateSelf()
    }
}