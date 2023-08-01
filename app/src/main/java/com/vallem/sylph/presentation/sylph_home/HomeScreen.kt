package com.vallem.sylph.presentation.sylph_home

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.LocationSearching
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.MyLocation
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator
import com.vallem.componentlibrary.ui.appbar.SylphBottomBar
import com.vallem.componentlibrary.ui.appbar.SylphTopBar
import com.vallem.componentlibrary.ui.theme.SylphTheme
import com.vallem.componentlibrary.ui.theme.TransFlagColors
import com.vallem.sylph.BuildConfig
import com.vallem.sylph.map.rememberLocationProvider
import com.vallem.sylph.map.rememberMapState
import com.vallem.sylph.presentation.Routes
import com.vallem.sylph.presentation.components.MapBox
import com.vallem.sylph.presentation.destinations.AddEventScreenDestination
import com.vallem.sylph.presentation.model.HomeShortcut
import com.vallem.sylph.util.extensions.point

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Destination(route = Routes.Screen.Home)
@Composable
fun HomeScreen(navigator: DestinationsNavigator, viewModel: HomeViewModel = hiltViewModel()) {
    val snackbarHostState = remember { SnackbarHostState() }
    val systemUiController = rememberSystemUiController()
    val locationProvider = rememberLocationProvider()

    val location by locationProvider.location.collectAsState(initial = null)
    val isLocationEnabled by locationProvider.isLocationEnabled.collectAsState(initial = false)

    val mapState = rememberMapState(center = location?.point)

    val locationSettingLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult()
    ) {
        // check if resultCode == RESULT_OK to perform an action if the location was or was not enabled
    }

    SideEffect {
        systemUiController.run {
            setStatusBarColor(color = TransFlagColors.Pink, darkIcons = true)
            setNavigationBarColor(color = TransFlagColors.Blue, darkIcons = true)
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            location?.let(viewModel::saveCurrentLocation)
        }
    }

    Scaffold(
        topBar = {
            SylphTopBar(
                title = "Explorar",
                navigationIcon = {
                     IconButton(onClick = { /*TODO*/ }){
                        Icon(
                            imageVector = Icons.Rounded.Menu,
                            contentDescription = "Menu de navegação"
                        )
                    }
                },
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            SylphBottomBar(
                shortcuts = HomeShortcut.values,
                onShortcutClick = {
                    when (it) {
                        HomeShortcut.AddEvent -> navigator.navigate(AddEventScreenDestination)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                FloatingActionButton(
                    onClick = {
                        if (isLocationEnabled) mapState.center()
                        else locationProvider.requestEnableLocation(locationSettingLauncher)
                    },
                    containerColor = TransFlagColors.Pink,
                    modifier = Modifier.shadow(
                        elevation = 6.dp,
                        shape = RoundedCornerShape(16.dp),
                        ambientColor = Color(0xFF3F3F3F)
                    ),
                ) {
                    AnimatedContent(
                        targetState = locationProvider.fabState,
                        label = "HomeScreenFabTransform"
                    ) {
                        when (it) {
                            HomeScreenFabState.LocationDisabled -> Icon(
                                imageVector = Icons.Rounded.LocationOn,
                                contentDescription = "Centralizar mapa em sua localização"
                            )

                            HomeScreenFabState.LocationNotLoaded -> {
                                val transition = rememberInfiniteTransition(
                                    label = "LoadingLocationFabTransition"
                                )
                                val iconAlpha by transition.animateFloat(
                                    initialValue = 0.15f,
                                    targetValue = 1f,
                                    animationSpec = infiniteRepeatable(
                                        animation = tween(1_000),
                                        repeatMode = RepeatMode.Reverse,
                                    ),
                                    label = "LoadingLocationFabAlpha"
                                )

                                Icon(
                                    imageVector = Icons.Rounded.LocationSearching,
                                    contentDescription = "Centralizar mapa em sua localização",
                                    modifier = Modifier.alpha(iconAlpha)
                                )
                            }

                            HomeScreenFabState.LocationFound -> Icon(
                                imageVector = Icons.Rounded.MyLocation,
                                contentDescription = "Centralizar mapa em sua localização"
                            )
                        }
                    }
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
                .background(MaterialTheme.colorScheme.surface)
        ) {
            MapBox(
                state = mapState,
                accessToken = BuildConfig.MAP_BOX_API_TOKEN,
                modifier = Modifier
                    .padding(12.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
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