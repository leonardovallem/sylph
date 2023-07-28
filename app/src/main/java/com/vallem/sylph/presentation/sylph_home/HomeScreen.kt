package com.vallem.sylph.presentation.sylph_home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.MyLocation
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator
import com.vallem.componentlibrary.ui.theme.SylphTheme
import com.vallem.sylph.BuildConfig
import com.vallem.sylph.map.rememberMapState
import com.vallem.sylph.presentation.Routes
import com.vallem.sylph.presentation.components.MapBox
import com.vallem.sylph.presentation.components.rememberCurrentLocationState
import com.vallem.sylph.util.extensions.point
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Destination(route = Routes.Screen.Home)
@Composable
fun HomeScreen(navigator: DestinationsNavigator, viewModel: HomeViewModel = hiltViewModel()) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val currentLocation by rememberCurrentLocationState(viewModel.locationEngine) {
        scope.launch {
            snackbarHostState.showSnackbar(it.cause?.message ?: "Error")
        }
    }
    val mapState = rememberMapState(center = currentLocation?.point)

    rememberSystemUiController().setSystemBarsColor(
        color = MaterialTheme.colorScheme.surface,
        darkIcons = !isSystemInDarkTheme()
    )

    DisposableEffect(Unit) {
        onDispose {
            currentLocation?.let(viewModel::saveCurrentLocation)
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                navigationIcon = {
                    Icon(
                        imageVector = Icons.Rounded.Menu,
                        contentDescription = "Menu de navegação"
                    )
                },
                title = { Text(text = "Explorar") }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            AnimatedVisibility(
                visible = mapState.center != null,
                enter = fadeIn() + slideInVertically(),
                exit = slideOutVertically() + fadeOut()
            ) {
                FloatingActionButton(
                    onClick = mapState::center,
                    modifier = Modifier.padding(24.dp),
                ) {
                    Icon(
                        imageVector = Icons.Rounded.MyLocation,
                        contentDescription = "Centralizar mapa em sua localização"
                    )
                }
            }
        },
        modifier = Modifier.fillMaxSize(),
    ) { pv ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(pv)
                .fillMaxSize()
        ) {
            MapBox(state = mapState, accessToken = BuildConfig.MAP_BOX_API_TOKEN)
        }
    }
}

@Preview
@Composable
private fun HomeScreenPreview() {
    SylphTheme {
        HomeScreen(navigator = EmptyDestinationsNavigator)
    }
}