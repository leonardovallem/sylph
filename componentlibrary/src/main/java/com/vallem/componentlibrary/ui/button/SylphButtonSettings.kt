package com.vallem.componentlibrary.ui.button

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

data class SylphButtonColors(val content: Color, val container: Color)

object SylphButtonDefaults {
    @Composable
    fun pillColors(
        content: Color = MaterialTheme.colorScheme.onSecondaryContainer,
        container: Color = MaterialTheme.colorScheme.secondaryContainer,
    ) = SylphButtonColors(content, container)

    @Composable
    fun elevatedColors(
        content: Color = MaterialTheme.colorScheme.onSecondaryContainer,
        container: Color = MaterialTheme.colorScheme.secondaryContainer,
    ) = SylphButtonColors(content, container)
}