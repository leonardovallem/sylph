package com.vallem.sylph.shared.map.util

import com.mapbox.maps.MercatorCoordinate
import com.mapbox.maps.ScreenBox
import kotlin.math.pow
import kotlin.math.sqrt

fun MercatorCoordinate.distanceTo(coordinate: MercatorCoordinate) =
    sqrt((x - coordinate.x).pow(2) + (y - coordinate.y).pow(2))

operator fun ScreenBox.contains(coordinate: MercatorCoordinate) = (coordinate.x >= min.x)
    .and(coordinate.x <= max.x)
    .and(coordinate.y >= min.y)
    .and(coordinate.y <= max.y)