package com.vallem.sylph.presentation.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.ui.graphics.vector.ImageVector
import com.vallem.componentlibrary.ui.model.SylphShortcut

enum class HomeShortcut(
    override val label: String,
    override val icon: ImageVector
) : SylphShortcut {
    AddEvent("Adicionar evento", Icons.Rounded.Add);

    companion object {
        val values = values().toSet()
    }
}