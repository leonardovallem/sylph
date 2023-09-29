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
import androidx.compose.foundation.isSystemInDarkTheme
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
import com.mapbox.bindgen.Value
import com.mapbox.geojson.Point
import com.mapbox.maps.MercatorCoordinate
import com.mapbox.maps.ScreenBox
import com.mapbox.maps.ScreenCoordinate
import com.mapbox.maps.SourceQueryOptions
import com.mapbox.maps.extension.style.layers.addPersistentLayer
import com.mapbox.maps.extension.style.layers.getLayer
import com.mapbox.maps.extension.style.sources.addSource
import com.mapbox.maps.extension.style.sources.getSource
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator
import com.ramcosta.composedestinations.result.NavResult
import com.ramcosta.composedestinations.result.OpenResultRecipient
import com.vallem.componentlibrary.domain.model.UserInfo
import com.vallem.componentlibrary.ui.appbar.SylphBottomBar
import com.vallem.componentlibrary.ui.appbar.SylphTopBar
import com.vallem.componentlibrary.ui.loading.SylphLoading
import com.vallem.componentlibrary.ui.theme.ColorSystemBars
import com.vallem.componentlibrary.ui.theme.SylphTheme
import com.vallem.componentlibrary.ui.theme.TransFlagColors
import com.vallem.sylph.events.domain.EventDetails
import com.vallem.sylph.events.map.EventHeatmap
import com.vallem.sylph.events.presentation.destinations.AddEventScreenDestination
import com.vallem.sylph.events.presentation.destinations.EventDetailsBottomSheetDestination
import com.vallem.sylph.events.presentation.destinations.UserDetailsScreenDestination
import com.vallem.sylph.events.presentation.detail.EventDetailsResult
import com.vallem.sylph.home.presentation.model.HomeShortcut
import com.vallem.sylph.shared.BuildConfig
import com.vallem.sylph.shared.Routes
import com.vallem.sylph.shared.SylphDestination
import com.vallem.sylph.shared.domain.model.Result
import com.vallem.sylph.shared.extensions.point
import com.vallem.sylph.shared.map.model.PointWrapper
import com.vallem.sylph.shared.map.presentation.MapBox
import com.vallem.sylph.shared.map.util.defaultStyle
import com.vallem.sylph.shared.map.util.rememberLocationProvider
import com.vallem.sylph.shared.map.util.rememberMapState
import com.vallem.sylph.shared.presentation.components.NavigationDrawerWrapper
import com.vallem.sylph.shared.presentation.model.NavigationShortcut
import com.vallem.sylph.shared.util.EmptyOpenResultRecipient
import com.vallem.sylph.shared.util.truthyCallback
import kotlin.math.pow
import kotlin.math.sqrt
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Destination(route = Routes.Screen.Home)
@Composable
fun HomeScreen(
    navigator: DestinationsNavigator,
    eventDetailsRecipient: OpenResultRecipient<EventDetailsResult>,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val locationProvider = rememberLocationProvider()
    val isDarkMode = isSystemInDarkTheme()

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

    eventDetailsRecipient.onNavResult {
        if (it is NavResult.Value) when (val result = it.value) {
            is EventDetailsResult.ShowUserDetails -> navigator.navigate(
                UserDetailsScreenDestination(result.userId)
            )
        }
    }

    LaunchedEffect(Unit) {
        viewModel.updateEvents()
    }

    LaunchedEffect(eventsFeatures) {
        when (val features = eventsFeatures) {
            is Result.Success -> mapState.mapView?.defaultStyle(isDarkMode) {
                getSource(EventHeatmap.EventDataSourceId)
                    ?: addSource(EventHeatmap.sourceFrom(features.data))
                getLayer(EventHeatmap.Layers.Id.Danger)
                    ?: addPersistentLayer(EventHeatmap.Layers.Danger)
                getLayer(EventHeatmap.Layers.Id.Safety)
                    ?: addPersistentLayer(EventHeatmap.Layers.Safety)
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
                    onClick = { clickedPoint ->
                        val mapbox = mapState.mapView?.getMapboxMap() ?: return@MapBox false
                        val coordinates = mapbox.project(clickedPoint, mapbox.cameraState.zoom)

                        val tolerance = 10.0
                        val screenBox = ScreenBox(
                            ScreenCoordinate(coordinates.x - tolerance, coordinates.y - tolerance),
                            ScreenCoordinate(coordinates.x + tolerance, coordinates.y + tolerance),
                        )

                        mapbox.executeOnRenderThread {
                            // TODO make it work using [queryRenderedFeatures]
                            mapbox.querySourceFeatures(
                                sourceId = EventHeatmap.EventDataSourceId,
                                options = SourceQueryOptions(
                                    EventHeatmap.Layers.Id.values,
                                    Value.nullValue()
                                )
                            ) { features ->
                                features.value?.mapNotNull {
                                    it.feature
                                }?.filter {
                                    val point = it.geometry() as? Point ?: return@filter false
                                    val coords = mapbox.project(point, mapbox.cameraState.zoom)

                                    coords in screenBox
                                }?.minByOrNull {
                                    val point = it.geometry() as Point
                                    val coords = mapbox.project(point, mapbox.cameraState.zoom)
                                    coords.distanceTo(coordinates)
                                }?.let {
                                    mapState.recenter(it.geometry() as Point)
                                    mapState.center(18.0)

                                    it.id()?.let { id ->
                                        scope.launch(Dispatchers.Main) {
                                            navigator.navigate(
                                                EventDetailsBottomSheetDestination(
                                                    EventDetails.Async(id, true)
                                                )
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        false
                    },
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

fun MercatorCoordinate.distanceTo(coordinate: MercatorCoordinate) =
    sqrt((x - coordinate.x).pow(2) + (y - coordinate.y).pow(2))

operator fun ScreenBox.contains(coordinate: MercatorCoordinate) = (coordinate.x >= min.x)
    .and(coordinate.x <= max.x)
    .and(coordinate.y >= min.y)
    .and(coordinate.y <= max.y)

@Preview
@Composable
private fun HomeScreenPreview() {
    SylphTheme {
        HomeScreen(
            navigator = EmptyDestinationsNavigator,
            eventDetailsRecipient = EmptyOpenResultRecipient(),
        )
    }
}