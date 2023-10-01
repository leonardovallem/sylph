package com.vallem.sylph.shared.map.presentation

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraState
import com.mapbox.maps.MapInitOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.ResourceOptions
import com.mapbox.maps.dsl.cameraOptions
import com.mapbox.maps.extension.localization.localizeLabels
import com.mapbox.maps.plugin.gestures.addOnMapClickListener
import com.mapbox.maps.plugin.gestures.addOnMapLongClickListener
import com.mapbox.maps.plugin.gestures.removeOnMapClickListener
import com.mapbox.maps.plugin.gestures.removeOnMapLongClickListener
import com.mapbox.maps.toCameraOptions
import com.vallem.sylph.shared.BuildConfig
import com.vallem.sylph.shared.map.util.MapState
import com.vallem.sylph.shared.map.util.defaultStyle
import com.vallem.sylph.shared.map.util.rememberMapState
import com.vallem.sylph.shared.util.falsyCallback

@Composable
fun MapBox(
    state: MapState,
    accessToken: String,
    modifier: Modifier = Modifier,
    onClick: (Point) -> Boolean = falsyCallback {},
    onLongClick: (Point) -> Boolean = falsyCallback {},
    cameraState: CameraState? = null,
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
                    cameraOptions = cameraState?.toCameraOptions(),
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
        view.defaultStyle(isDarkMode)
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
