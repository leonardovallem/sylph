package com.vallem.sylph.shared.data.datastore

import com.mapbox.maps.CameraState
import com.mapbox.maps.EdgeInsets
import com.vallem.sylph.shared.extensions.asMapCenter
import com.vallem.sylph.shared.extensions.asPoint
import com.vallem.sylph.shared.map.model.MapCenter

data class MapCameraState(
    val center: MapCenter,
    val padding: EdgeInsets,
    val zoom: Double,
    val bearing: Double,
    val pitch: Double,
) {
    companion object {
        val Default = MapCameraState(
            center = MapCenter(-12.426217251158677, -54.73436358441633),
            padding = EdgeInsets(0.0, 0.0, 0.0, 0.0),
            zoom = 2.5,
            bearing = 0.0,
            pitch = 0.0,
        )
    }
}

fun MapCameraState.toCameraState() = CameraState(center.asPoint(), padding, zoom, bearing, pitch)

fun CameraState.serializable() = MapCameraState(center.asMapCenter(), padding, zoom, bearing, pitch)