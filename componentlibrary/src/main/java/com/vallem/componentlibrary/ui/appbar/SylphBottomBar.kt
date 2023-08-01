package com.vallem.componentlibrary.ui.appbar

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.LocationSearching
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vallem.componentlibrary.ui.model.SylphShortcut
import com.vallem.componentlibrary.ui.theme.TransFlagColors

@Composable
fun <T : SylphShortcut> SylphBottomBar(
    shortcuts: Set<T>,
    onShortcutClick: (T) -> Unit,
    modifier: Modifier = Modifier,
    floatingActionButton: @Composable () -> Unit = {}
) {
    BottomAppBar(
        actions = {
            shortcuts.forEach {
                IconButton(onClick = { onShortcutClick(it) }) {
                    Icon(
                        imageVector = it.icon,
                        contentDescription = it.label,
                    )
                }
            }
        },
        floatingActionButton = floatingActionButton,
        containerColor = TransFlagColors.Blue,
        contentColor = TransFlagColors.OnBlue,
        modifier = modifier
    )
}

@Preview
@Composable
private fun SylphBottomBarPreview() {
    SylphBottomBar(
        shortcuts = setOf(
            object : SylphShortcut {
                override val label = "Adicionar evento"
                override val icon = Icons.Rounded.Add
            }
        ),
        onShortcutClick = {},
        modifier = Modifier.fillMaxWidth()
    ) {
        FloatingActionButton(
            onClick = {},
            containerColor = TransFlagColors.Pink,
            modifier = Modifier.shadow(
                elevation = 6.dp,
                shape = RoundedCornerShape(16.dp),
                ambientColor = Color(0xFF3F3F3F)
            )
        ) {
            Icon(imageVector = Icons.Rounded.LocationSearching, null)
        }
    }
}