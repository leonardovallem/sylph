package com.vallem.componentlibrary.extensions

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import java.io.ByteArrayOutputStream

fun Bitmap.encodeAsBase64(): String = ByteArrayOutputStream().use {
    compress(Bitmap.CompressFormat.JPEG, 100, it)
    Base64.encodeToString(it.toByteArray(), Base64.DEFAULT)
}

fun String.decodeAsBitmap(): Bitmap = Base64.decode(this, Base64.DEFAULT).let {
    BitmapFactory.decodeByteArray(it, 0, it.size)
}