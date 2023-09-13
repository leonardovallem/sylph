package com.vallem.sylph.home.presentation

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import com.mapbox.maps.extension.style.layers.addLayer
import com.mapbox.maps.extension.style.sources.addSource
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator
import com.vallem.componentlibrary.domain.model.UserInfo
import com.vallem.componentlibrary.ui.appbar.SylphBottomBar
import com.vallem.componentlibrary.ui.appbar.SylphTopBar
import com.vallem.componentlibrary.ui.loading.SylphLoading
import com.vallem.componentlibrary.ui.theme.ColorSystemBars
import com.vallem.componentlibrary.ui.theme.SylphTheme
import com.vallem.componentlibrary.ui.theme.TransFlagColors
import com.vallem.sylph.events.map.EventHeatmap
import com.vallem.sylph.events.presentation.destinations.AddEventScreenDestination
import com.vallem.sylph.home.presentation.model.HomeShortcut
import com.vallem.sylph.shared.BuildConfig
import com.vallem.sylph.shared.Routes
import com.vallem.sylph.shared.SylphDestination
import com.vallem.sylph.shared.domain.model.Result
import com.vallem.sylph.shared.extensions.point
import com.vallem.sylph.shared.map.model.PointWrapper
import com.vallem.sylph.shared.map.presentation.MapBox
import com.vallem.sylph.shared.map.util.rememberLocationProvider
import com.vallem.sylph.shared.map.util.rememberMapState
import com.vallem.sylph.shared.presentation.components.NavigationDrawerWrapper
import com.vallem.sylph.shared.presentation.model.NavigationShortcut
import com.vallem.sylph.shared.util.truthyCallback
import kotlinx.coroutines.launch

@Destination(route = Routes.Screen.Home)
@Composable
fun HomeScreen(
    navigator: DestinationsNavigator,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val locationProvider = rememberLocationProvider()

    val eventsFeatures by viewModel.eventsFeatures.collectAsState(viewModel.viewModelScope.coroutineContext)

    val location by locationProvider.location.collectAsState(initial = null)
    val isLocationEnabled by locationProvider.isLocationEnabled.collectAsState(initial = false)

    val mapState = rememberMapState(center = location?.point)

    val locationSettingLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult()
    ) {
        // check if resultCode == RESULT_OK to perform an action if the location was or was not enabled
    }

    ColorSystemBars()

    DisposableEffect(Unit) {
        onDispose {
            location?.let(viewModel::saveCurrentLocation)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.updateEvents()
    }

    LaunchedEffect(eventsFeatures) {
        when (val features = eventsFeatures) {
            is Result.Success -> mapState.style {
                addSource(EventHeatmap.sourceFrom(features.data))
                addLayer(EventHeatmap.Layers.Danger)
                addLayer(EventHeatmap.Layers.Safety)
            }

            else -> Unit
        }
    }

    NavigationDrawerWrapper(
        drawerState = drawerState,
        userInfo = viewModel.currentUser?.displayName?.let { UserInfo(it, null) },
        navigator = navigator,
        selectedShortcut = NavigationShortcut.Map,
    ) {
        Scaffold(
            topBar = {
                Column {
                    SylphTopBar(
                        title = "Explorar",
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

                    AnimatedVisibility(visible = eventsFeatures == Result.Loading) {
                        SylphLoading.Linear(modifier = Modifier.fillMaxWidth())
                    }
                }
            },
            snackbarHost = { SnackbarHost(snackbarHostState) },
            bottomBar = {
                SylphBottomBar(
                    shortcuts = HomeShortcut.values,
                    onShortcutClick = {
                        when (it) {
                            HomeShortcut.AddEvent -> navigator.navigate(
                                AddEventScreenDestination(null)
                            )
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
                    onLongClick = truthyCallback {
                        SylphDestination.Screen.AddEvent(PointWrapper(it))
                        navigator.navigate(AddEventScreenDestination(PointWrapper(it)))
                    },
                    modifier = Modifier
                        .padding(12.dp)
                        .clip(RoundedCornerShape(8.dp))
                )
            }
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