package com.vallem.sylph.shared.util

import android.graphics.Bitmap
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap

fun ImageBitmap.resized(width: Int, height: Int) =
    Bitmap.createScaledBitmap(asAndroidBitmap(), width, height, true)

fun ActivityResultLauncher<String>.pickImage() = launch("image/*")