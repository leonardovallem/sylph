package com.vallem.componentlibrary.ui.drawer

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vallem.componentlibrary.domain.model.UserInfo
import com.vallem.componentlibrary.ui.model.MockShortcut
import com.vallem.componentlibrary.ui.model.SylphShortcut
import com.vallem.componentlibrary.ui.theme.SylphTheme
import com.vallem.componentlibrary.ui.theme.ZoneEventColors
import com.vallem.componentlibrary.ui.user.UserInfo

object SylphNavigationDrawer {
    @Composable
    operator fun <T : SylphShortcut> invoke(
        userInfo: UserInfo,
        modifier: Modifier = Modifier,
        selectedShortcut: T? = null,
        shortCuts: Set<T> = emptySet(),
        onShortcutClick: (T) -> Unit = {},
        onLogOut: () -> Unit,
    ) {
        var confirmDialogShown by remember { mutableStateOf(false) }

        if (confirmDialogShown) AlertDialog(
            onDismissRequest = { confirmDialogShown = false },
            confirmButton = {
                TextButton(onClick = onLogOut) {
                    Text(text = "Sair", color = remember { ZoneEventColors() }.danger)
                }
            },
            dismissButton = {
                TextButton(onClick = { confirmDialogShown = false }) {
                    Text(text = "Cancelar", color = MaterialTheme.colorScheme.primary)
                }
            },
            title = { Text(text = "Deseja sair de sua conta?") },
        )

        Base(
            modifier = modifier,
            topContent = { UserInfo(userInfo) },
            middleContent = {
                shortCuts.forEach {
                    NavigationDrawerItem(
                        label = { Text(text = it.label) },
                        selected = it == selectedShortcut,
                        icon = { Icon(imageVector = it.icon, contentDescription = null) },
                        onClick = { onShortcutClick(it) },
                        modifier = Modifier.padding(horizontal = 12.dp)
                    )
                }
            },
            bottomContent = {
                TextButton(onClick = { confirmDialogShown = true }) {
                    Text(
                        text = "SAIR",
                        style = MaterialTheme.typography.titleMedium,
                        color = remember { ZoneEventColors() }.danger,
                    )
                }
            },
        )
    }

    @Composable
    fun Base(
        modifier: Modifier = Modifier,
        topContent: @Composable ColumnScope.() -> Unit,
        middleContent: @Composable ColumnScope.() -> Unit,
        bottomContent: @Composable ColumnScope.() -> Unit,
    ) {
        ModalDrawerSheet(modifier = modifier.fillMaxWidth(.85f)) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(vertical = 24.dp),
            ) {
                topContent()

                Divider(modifier = Modifier.fillMaxWidth())

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(vertical = 16.dp),
                ) {
                    middleContent()
                }

                bottomContent()
            }
        }
    }
}

@Preview
@Composable
private fun SylphNavigationDrawerPreview() {
    SylphTheme {
        SylphNavigationDrawer(
            userInfo = UserInfo("User name", null),
            selectedShortcut = MockShortcut.values.first(),
            shortCuts = MockShortcut.values,
            onLogOut = {}
        )
    }
}