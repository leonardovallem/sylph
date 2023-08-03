package com.vallem.sylph.presentation.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import com.mapbox.geojson.Point
import com.mapbox.maps.MapInitOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.ResourceOptions
import com.mapbox.maps.Style
import com.mapbox.maps.dsl.cameraOptions
import com.mapbox.maps.extension.localization.localizeLabels
import com.mapbox.maps.plugin.LocationPuck2D
import com.mapbox.maps.plugin.gestures.addOnMapClickListener
import com.mapbox.maps.plugin.gestures.addOnMapLongClickListener
import com.mapbox.maps.plugin.gestures.removeOnMapClickListener
import com.mapbox.maps.plugin.gestures.removeOnMapLongClickListener
import com.mapbox.maps.plugin.locationcomponent.location
import com.vallem.sylph.BuildConfig
import com.vallem.sylph.R
import com.vallem.sylph.map.MapState
import com.vallem.sylph.map.rememberMapState
import com.vallem.sylph.util.extensions.getDrawable
import com.vallem.sylph.util.falsyCallback

@Composable
fun MapBox(
    state: MapState,
    accessToken: String,
    modifier: Modifier = Modifier,
    onClick: (Point) -> Boolean = falsyCallback {},
    onLongClick: (Point) -> Boolean = falsyCallback {},
) {
    val isDarkMode = isSystemInDarkTheme()

    DisposableEffect(state.mapView) {
        state.mapView?.getMapboxMap()?.run {
            addOnMapClickListener(onClick)
            addOnMapLongClickListener(onLongClick)
        }

        onDispose {
            state.mapView?.getMapboxMap()?.run {
                removeOnMapClickListener(onClick)
                removeOnMapLongClickListener(onLongClick)
            }
        }
    }

    AndroidView(
        modifier = modifier,
        factory = {
            MapView(
                context = it,
                mapInitOptions = MapInitOptions(
                    context = it,
                    resourceOptions = ResourceOptions.Builder()
                        .accessToken(accessToken)
                        .build()
                )
            ).apply {
                getMapboxMap().run {
                    state.center?.let {
                        setCamera(
                            cameraOptions {
                                center(it)
                                zoom(12.0)
                            }
                        )
                    }

                    getStyle { style ->
                        style.localizeLabels(context.resources.configuration.locales[0])
                    }
                }
            }
        }
    ) { view ->
        state.setView(view)

        view.getMapboxMap().run {


            loadStyleUri(if (isDarkMode) Style.TRAFFIC_NIGHT else Style.TRAFFIC_DAY) {
                view.location.updateSettings {
                    enabled = true
                    locationPuck = LocationPuck2D(
                        topImage = view.context.getDrawable(
                            R.drawable.ic_location_on_24,
                            com.vallem.componentlibrary.R.color.purple_200
                        )
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun MapBoxPreview() {
    MapBox(
        state = rememberMapState(Point.fromLngLat(0.0, 0.0)),
        accessToken = BuildConfig.MAP_BOX_API_TOKEN
    )
}
