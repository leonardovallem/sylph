package com.vallem.componentlibrary.ui.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.Home
import androidx.compose.ui.graphics.vector.ImageVector

interface SylphShortcut {
    val label: String
    val icon: ImageVector
}

internal enum class MockShortcut(
    override val label: String,
    override val icon: ImageVector
) : SylphShortcut {
    Home("Home", Icons.Rounded.Home),
    Favorites("Favorites", Icons.Rounded.FavoriteBorder);

    companion object {
        val values by lazy { values().toSet() }
    }
}