package com.vallem.componentlibrary.ui.chip

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

data class SylphChipColors(val content: Color, val container: Color, val border: Color)

object SylphChipDefaults {
    @Composable
    fun primaryColors(
        content: Color = MaterialTheme.colorScheme.onPrimaryContainer,
        container: Color = MaterialTheme.colorScheme.primaryContainer,
        border: Color = MaterialTheme.colorScheme.primary,
    ) = SylphChipColors(content, container, border)

    @Composable
    fun smallColors(
        content: Color = MaterialTheme.colorScheme.onTertiaryContainer,
        container: Color = MaterialTheme.colorScheme.tertiaryContainer,
        border: Color = MaterialTheme.colorScheme.tertiary,
    ) = SylphChipColors(content, container, border)
}