package com.vallem.sylph.shared.extensions

import android.location.Location
import com.mapbox.geojson.Point
import com.vallem.sylph.shared.map.model.MapCenter

val Location.point: Point
    get() = Point.fromLngLat(longitude, latitude)

val Location.mapCenter: MapCenter
    get() = MapCenter(latitude, longitude)
