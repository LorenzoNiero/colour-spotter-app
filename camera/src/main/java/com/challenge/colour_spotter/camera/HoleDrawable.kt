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

        // Draw the semitransparent black rectangle
        canvas.drawRect(bounds, paint)

        // Calculate the size and position of the transparent circle
        val centerX = bounds.exactCenterX()
        val centerY = bounds.exactCenterY()
        val radius = minOf(bounds.width(), bounds.height()) * Constants.percentDivisionImage

        // Set the transfer mode to create the transparent hole
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)

        // Draw the transparent circle
        canvas.drawCircle(centerX, centerY, radius, paint)

        // Restore the original transfer mode
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