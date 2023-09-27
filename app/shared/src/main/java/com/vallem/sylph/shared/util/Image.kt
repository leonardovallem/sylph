package com.vallem.sylph.shared.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.result.ActivityResultLauncher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream

fun Bitmap.resized(width: Int, height: Int) = Bitmap.createScaledBitmap(this, width, height, true)

suspend fun Context.getBitmap(uri: Uri, quality: Int = 75) = withContext(Dispatchers.Default) {
    contentResolver.openInputStream(uri).use { istream ->
        istream?.readBytes()?.let { bytes ->
            ByteArrayOutputStream().use { baos ->
                BitmapFactory
                    .decodeByteArray(bytes, 0, bytes.size)
                    .compress(Bitmap.CompressFormat.JPEG, quality, baos)

                val result = baos.toByteArray()
                BitmapFactory.decodeByteArray(result, 0, result.size)
            }
        }
    }
}

fun ActivityResultLauncher<String>.pickImage() = launch("image/*")