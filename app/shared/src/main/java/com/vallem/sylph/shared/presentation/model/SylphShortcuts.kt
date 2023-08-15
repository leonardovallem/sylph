package com.vallem.sylph.shared.presentation.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Assignment
import androidx.compose.material.icons.rounded.Map
import androidx.compose.ui.graphics.vector.ImageVector
import com.vallem.componentlibrary.ui.model.SylphShortcut
import com.vallem.sylph.shared.SylphDestination

enum class NavigationShortcut(
    override val label: String,
    override val icon: ImageVector,
    val destination: SylphDestination
) : SylphShortcut {
    Map("Mapa", Icons.Rounded.Map, SylphDestination.Screen.Home),
    RegisteredEvents("Meus eventos", Icons.Rounded.Assignment, SylphDestination.Screen.UserEvents);

    companion object {
        val values = values().toSet()
    }
}
