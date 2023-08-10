package com.vallem.sylph.presentation.sylph_user_events

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator
import com.vallem.componentlibrary.domain.model.UserInfo
import com.vallem.componentlibrary.ui.appbar.SylphTopBar
import com.vallem.sylph.presentation.Routes
import com.vallem.sylph.presentation.components.NavigationDrawerWrapper
import com.vallem.sylph.presentation.model.NavigationShortcut
import kotlinx.coroutines.launch

@Destination(route = Routes.Screen.UserEvents)
@Composable
fun UserEventsScreen(
    navigator: DestinationsNavigator,
    viewModel: UserEventsViewModel = hiltViewModel()
) {
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(DrawerValue.Closed)

    BackHandler(onBack = navigator::navigateUp)

    NavigationDrawerWrapper(
        drawerState = drawerState,
        userInfo = viewModel.currentUser?.displayName?.let { UserInfo(it, null) },
        navigator = navigator,
        selectedShortcut = NavigationShortcut.RegisteredEvents,
    ) {
        Scaffold(
            topBar = {
                SylphTopBar(
                    title = "Meus eventos",
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                scope.launch { drawerState.open() }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Menu,
                                contentDescription = "Menu de navegação"
                            )
                        }
                    },
                )
            },
            modifier = Modifier.fillMaxSize(),
        ) { pv ->
            Column(
                modifier = Modifier
                    .padding(pv)
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surface)
            ) {

            }
        }
    }
}

@Preview
@Composable
private fun UserEventsScreenPreview() {
    UserEventsScreen(EmptyDestinationsNavigator)
}