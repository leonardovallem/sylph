package com.vallem.sylph.map

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.plugin.animation.MapAnimationOptions
import com.mapbox.maps.plugin.animation.camera

class MapState(center: Point?) {
    var center by mutableStateOf<Point?>(null)
        private set
    var mapView by mutableStateOf<MapView?>(null)
        private set

    init {
        this.center = center
    }

    fun recenter(center: Point) {
        this.center = center
    }

    fun setView(view: MapView) {
        mapView = view
    }

    fun zoomToLocation(point: Point, zoom: Double = 12.0, duration: Long = 1000L) {
        mapView?.camera?.easeTo(
            cameraOptions = CameraOptions.Builder()
                .center(point)
                .zoom(zoom)
                .build(),
            animationOptions = MapAnimationOptions.mapAnimationOptions {
                duration(duration)
            }
        )
    }

    fun center(zoom: Double = 12.0, duration: Long = 1000L) = center?.let {
        zoomToLocation(it, zoom, duration)
    }
}

@Composable
fun rememberMapState(center: Point?): MapState {
    val state = remember { MapState(center) }
    LaunchedEffect(center) { if (center != null) state.recenter(center) }
    return state
}