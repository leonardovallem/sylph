package com.vallem.sylph.util.extensions

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable

fun Drawable.toBitmap(): Bitmap? = when {
    this is BitmapDrawable -> bitmap
    constantState == null -> null
    else -> {
        val result = constantState!!.newDrawable().mutate()
        val bitmap: Bitmap = Bitmap.createBitmap(
            result.intrinsicWidth, result.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )

        val canvas = Canvas(bitmap)
        result.setBounds(0, 0, canvas.width, canvas.height)
        result.draw(canvas)

        bitmap
    }
}