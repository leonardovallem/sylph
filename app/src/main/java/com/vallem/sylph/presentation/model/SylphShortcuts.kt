package com.vallem.sylph.presentation.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Assignment
import androidx.compose.material.icons.rounded.Map
import androidx.compose.ui.graphics.vector.ImageVector
import com.vallem.componentlibrary.ui.model.SylphShortcut
import com.vallem.sylph.presentation.destinations.DirectionDestination
import com.vallem.sylph.presentation.destinations.HomeScreenDestination
import com.vallem.sylph.presentation.destinations.UserEventsScreenDestination

enum class NavigationShortcut(
    override val label: String,
    override val icon: ImageVector,
    val destination: DirectionDestination
) : SylphShortcut {
    Map("Mapa", Icons.Rounded.Map, HomeScreenDestination),
    RegisteredEvents("Meus eventos", Icons.Rounded.Assignment, UserEventsScreenDestination);

    companion object {
        val values = values().toSet()
    }
}

enum class HomeShortcut(
    override val label: String,
    override val icon: ImageVector
) : SylphShortcut {
    AddEvent("Adicionar evento", Icons.Rounded.Add);

    companion object {
        val values = values().toSet()
    }
}