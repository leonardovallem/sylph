package com.vallem.sylph.util.extensions

import android.content.Context
import android.content.pm.PackageManager
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat

fun Context.getDrawable(@DrawableRes drawableId: Int, @ColorRes colorId: Int) =
    ContextCompat.getDrawable(this, drawableId)?.apply {
        DrawableCompat.setTint(mutate(), ContextCompat.getColor(this@getDrawable, colorId))
    }

fun Context.hasPermission(permission: String) = ContextCompat.checkSelfPermission(
    this,
    permission
) == PackageManager.PERMISSION_GRANTED