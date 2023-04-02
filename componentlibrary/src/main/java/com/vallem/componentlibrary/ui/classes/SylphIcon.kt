package com.vallem.componentlibrary.ui.classes

import androidx.compose.ui.graphics.vector.ImageVector

data class SylphIcon(
    val icon: ImageVector,
    val colors: ColorPair = ColorPair.Unspecified,
    val action: (() -> Unit)? = null
)

fun ImageVector.toSylphIcon(colors: ColorPair = ColorPair.Unspecified) = SylphIcon(this, colors)

fun ColorPair.forIcons(vararg icons: ImageVector) = icons.map { SylphIcon(it, this) }