package com.vallem.componentlibrary.ui.drawer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vallem.componentlibrary.domain.model.UserInfo
import com.vallem.componentlibrary.ui.model.MockShortcut
import com.vallem.componentlibrary.ui.model.SylphShortcut
import com.vallem.componentlibrary.ui.theme.SylphTheme
import com.vallem.componentlibrary.ui.theme.ZoneEventColors
import com.vallem.componentlibrary.ui.user.UserInfo

@Composable
fun <T : SylphShortcut> SylphNavigationDrawer(
    userInfo: UserInfo?,
    modifier: Modifier = Modifier,
    selectedShortcut: T? = null,
    shortCuts: Set<T> = emptySet(),
    onShortcutClick: (T) -> Unit = {},
    onLogOut: () -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .clip(RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(vertical = 24.dp)
            .fillMaxWidth(0.85f)
            .fillMaxHeight(),
    ) {
        userInfo?.let {
            UserInfo(name = it.name, picUrl = it.picture)

            Divider(modifier = Modifier.fillMaxWidth())
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(vertical = 16.dp),
        ) {
            shortCuts.forEach {
                NavigationDrawerItem(
                    label = { Text(text = it.label) },
                    selected = it == selectedShortcut,
                    icon = { Icon(imageVector = it.icon, contentDescription = null) },
                    onClick = { onShortcutClick(it) },
                    modifier = Modifier.padding(horizontal = 12.dp)
                )
            }
        }

        TextButton(onClick = onLogOut) {
            Text(
                text = "SAIR",
                style = MaterialTheme.typography.titleMedium,
                color = remember { ZoneEventColors() }.danger,
            )
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