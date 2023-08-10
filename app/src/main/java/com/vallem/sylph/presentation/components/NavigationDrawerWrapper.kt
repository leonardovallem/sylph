package com.vallem.sylph.presentation.components

import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.vallem.componentlibrary.domain.model.UserInfo
import com.vallem.componentlibrary.ui.drawer.SylphNavigationDrawer
import com.vallem.sylph.presentation.model.NavigationShortcut
import kotlinx.coroutines.launch

@Composable
fun <T : NavigationShortcut> NavigationDrawerWrapper(
    userInfo: UserInfo?,
    navigator: DestinationsNavigator,
    drawerState: DrawerState,
    modifier: Modifier = Modifier,
    selectedShortcut: T? = null,
    content: @Composable () -> Unit
) {
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        drawerState.snapTo(DrawerValue.Closed)
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = drawerState.isOpen,
        drawerContent = {
            SylphNavigationDrawer(
                userInfo = userInfo,
                selectedShortcut = selectedShortcut,
                shortCuts = userInfo?.let { NavigationShortcut.values }?.toSet().orEmpty(),
                onShortcutClick = {
                    if (it.destination == selectedShortcut?.destination) scope.launch { drawerState.close() }
                    else navigator.navigate(it.destination)
                }
            )
        },
        modifier = modifier,
        content = content
    )
}