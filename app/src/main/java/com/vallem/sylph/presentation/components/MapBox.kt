package com.vallem.sylph.presentation.components

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MyLocation
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapInitOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.ResourceOptions
import com.mapbox.maps.Style
import com.mapbox.maps.extension.localization.localizeLabels
import com.mapbox.maps.plugin.LocationPuck2D
import com.mapbox.maps.plugin.animation.MapAnimationOptions
import com.mapbox.maps.plugin.animation.camera
import com.mapbox.maps.plugin.locationcomponent.location
import com.vallem.sylph.BuildConfig
import com.vallem.sylph.R
import com.vallem.sylph.util.extensions.getDrawable

@Composable
fun MapBox(
    center: Point?,
    modifier: Modifier = Modifier,
) {
    var mapView by remember { mutableStateOf<MapView?>(null) }

    BoxWithConstraints {
        AndroidView(
            modifier = modifier
                .width(maxWidth)
                .height(maxHeight),
            factory = {
                MapView(
                    context = it,
                    mapInitOptions = MapInitOptions(
                        context = it,
                        resourceOptions = ResourceOptions.Builder()
                            .accessToken(BuildConfig.MAP_BOX_API_TOKEN)
                            .build()
                    )
                )
            }
        ) { view ->
            mapView = view

            view.camera.easeTo(
                cameraOptions = CameraOptions.Builder()
                    .center(center)
                    .zoom(12.0)
                    .build(),
                animationOptions = MapAnimationOptions.mapAnimationOptions {
                    duration(1000L)
                }
            )

            view.getMapboxMap().run {
                getStyle {
                    it.localizeLabels(view.context.resources.configuration.locales[0])
                }

                loadStyleUri(Style.MAPBOX_STREETS) {
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

        FloatingActionButton(
            onClick = {
                mapView?.camera?.easeTo(
                    cameraOptions = CameraOptions.Builder()
                        .center(center)
                        .zoom(12.0)
                        .build(),
                    animationOptions = MapAnimationOptions.mapAnimationOptions {
                        duration(1000L)
                    }
                )
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp),
        ) {
            Icon(
                imageVector = Icons.Rounded.MyLocation,
                contentDescription = "Centralizar mapa em sua localização"
            )
        }
    }
}

@Preview
@Composable
private fun MapBoxPreview() {
    MapBox(center = Point.fromLngLat(0.0, 0.0))
}
