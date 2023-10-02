package com.vallem.sylph.shared.presentation.navigation

import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.vallem.componentlibrary.ui.drawer.SylphNavigationDrawer
import com.vallem.componentlibrary.ui.user.UserInfoSkeleton
import com.vallem.sylph.shared.Routes
import com.vallem.sylph.shared.domain.model.Result
import com.vallem.sylph.shared.presentation.components.AlertLevel
import com.vallem.sylph.shared.presentation.components.AlertMessage
import com.vallem.sylph.shared.presentation.model.NavigationShortcut
import kotlinx.coroutines.launch

@Composable
fun <T : NavigationShortcut> NavigationDrawerWrapper(
    navigator: DestinationsNavigator,
    drawerState: DrawerState,
    modifier: Modifier = Modifier,
    selectedShortcut: T? = null,
    viewModel: NavigationViewModel = hiltViewModel(),
    content: @Composable () -> Unit
) {
    val scope = rememberCoroutineScope()
    val userInfo by viewModel.userInfo.collectAsState()
    val navigationEvent by viewModel.navigationEvent.collectAsState(null)

    LaunchedEffect(Unit) {
        drawerState.snapTo(DrawerValue.Closed)
    }

    LaunchedEffect(navigationEvent) {
        when (navigationEvent) {
            NavigationEvent.LogOut -> navigator.navigate(Routes.Screen.Login) {
                popUpTo(Routes.Screen.Home) {
                    inclusive = true
                }
            }

            null -> Unit
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = drawerState.isOpen,
        drawerContent = {
            when (val res = userInfo) {
                is Result.Success -> SylphNavigationDrawer(
                    userInfo = res.data!!,
                    selectedShortcut = selectedShortcut,
                    shortCuts = NavigationShortcut.values,
                    onShortcutClick = {
                        if (it.destination == selectedShortcut?.destination) scope.launch { drawerState.close() }
                        else navigator.navigate(it.destination.route)
                    },
                    onLogOut = viewModel::logOut
                )

                else -> SylphNavigationDrawer.Base(
                    topContent = { UserInfoSkeleton() },
                    middleContent = {
                        if (res is Result.Failure) AlertMessage(
                            title = "Algo deu errado...",
                            description = "Ocorreu um erro ao recuperar os seus dados",
                            level = AlertLevel.Error,
                        )
                    },
                    bottomContent = {},
                )
            }
        },
        modifier = modifier,
        content = content
    )
}