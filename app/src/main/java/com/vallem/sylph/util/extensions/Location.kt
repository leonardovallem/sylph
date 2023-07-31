package com.vallem.sylph.util.extensions

import android.location.Location
import com.mapbox.geojson.Point
import com.vallem.sylph.data.datastore.MapCenter

val Location.point: Point
    get() = Point.fromLngLat(longitude, latitude)

val Location.mapCenter: MapCenter
    get() = MapCenter(latitude, longitude)
