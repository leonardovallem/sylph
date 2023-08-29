package com.vallem.sylph.shared.map.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapInitOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.ResourceOptions
import com.mapbox.maps.plugin.gestures.gestures
import com.vallem.componentlibrary.ui.theme.SylphTheme
import com.vallem.sylph.shared.BuildConfig

@Composable
fun MapLocation(
    point: Point,
    accessToken: String,
    modifier: Modifier = Modifier,
    zoom: Double = 16.0
) {
    val isPreview = LocalInspectionMode.current

    if (isPreview) Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.background(Color.Blue)
    ) {
        Text("Map")
    } else AndroidView(
        factory = {
            MapView(
                context = it,
                mapInitOptions = MapInitOptions(
                    context = it,
                    cameraOptions = CameraOptions.Builder()
                        .center(point)
                        .zoom(zoom)
                        .build(),
                    resourceOptions = ResourceOptions.Builder()
                        .accessToken(accessToken)
                        .build()
                )
            ).apply {
                gestures.run {
                    pitchEnabled = false
                    scrollEnabled = false
                    pinchToZoomEnabled = false
                    rotateEnabled = false
                }
            }
        },
        modifier = modifier
    )
}

@Preview
@Composable
private fun MapLocationPreview() {
    SylphTheme {
        MapLocation(
            point = Point.fromLngLat(0.0, 0.0),
            accessToken = BuildConfig.MAP_BOX_API_TOKEN,
            modifier = Modifier.size(200.dp)
        )
    }
}