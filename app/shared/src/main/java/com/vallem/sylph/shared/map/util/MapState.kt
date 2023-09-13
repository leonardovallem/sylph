package com.vallem.sylph.shared.map.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.extension.style.StyleInterface
import com.mapbox.maps.plugin.animation.MapAnimationOptions
import com.mapbox.maps.plugin.animation.camera
import com.mapbox.maps.plugin.annotation.AnnotationConfig
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager
import com.vallem.sylph.shared.R
import com.vallem.sylph.util.extensions.getDrawable
import com.vallem.sylph.util.extensions.toBitmap

class MapState(center: Point?) {
    var center by mutableStateOf<Point?>(null)
        private set
    var mapView by mutableStateOf<MapView?>(null)
        private set

    private val pointAnnotationManager by derivedStateOf {
        mapView
            ?.annotations
            ?.createPointAnnotationManager(AnnotationConfig())
    }

    init {
        this.center = center
    }

    fun recenter(center: Point) {
        this.center = center
    }

    fun setView(view: MapView) {
        mapView = view
    }

    fun style(block: StyleInterface.() -> Unit) = mapView
        ?.getMapboxMap()
        ?.getStyle()
        ?.run(block)

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

    fun addPointMarker(point: Point, singlePoint: Boolean) = mapView?.context?.getDrawable(
        drawableId = R.drawable.ic_location_on_24,
        colorId = com.vallem.componentlibrary.R.color.red
    )?.toBitmap()?.let {
        val pointAnnotationOptions = PointAnnotationOptions()
            .withPoint(point)
            .withIconImage(it)

        pointAnnotationManager?.run {
            if (singlePoint) pointAnnotationManager?.deleteAll()
            create(pointAnnotationOptions)
        }
    }
}

@Composable
fun rememberMapState(center: Point?): MapState {
    val state = remember { MapState(center) }
    LaunchedEffect(center) { if (center != null) state.recenter(center) }
    return state
}