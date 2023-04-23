package com.vallem.sylph.util.extensions

import android.content.Context
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat

fun Context.getDrawable(@DrawableRes drawableId: Int, @ColorRes colorId: Int) =
    ContextCompat.getDrawable(this, drawableId)?.apply {
        DrawableCompat.setTint(mutate(), ContextCompat.getColor(this@getDrawable, colorId))
    }