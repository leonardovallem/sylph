package com.vallem.componentlibrary.ui.classes

import androidx.compose.ui.graphics.Color

data class ColorPair(val inactive: Color, val active: Color) {
    companion object {
        val Unspecified = ColorPair(Color.Unspecified, Color.Unspecified)
    }
}

infix fun Color.pairWith(activeColor: Color) = ColorPair(this, activeColor)