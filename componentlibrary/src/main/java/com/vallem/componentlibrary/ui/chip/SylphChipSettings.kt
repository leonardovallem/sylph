package com.vallem.componentlibrary.ui.chip

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

data class SylphChipColors(val content: Color, val container: Color, val border: Color)

object SylphChipDefaults {
    @Composable
    fun primaryColors(
        content: Color = MaterialTheme.colorScheme.onPrimary,
        container: Color = MaterialTheme.colorScheme.primary,
        border: Color = MaterialTheme.colorScheme.outlineVariant,
    ) = SylphChipColors(content, container, border)
}