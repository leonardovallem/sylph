package com.vallem.sylph.shared.extensions

import com.mapbox.geojson.Point

val Point.formatAsDMS: String
    get() = "${latitude().decimalToDMS()}, ${longitude().decimalToDMS()}"

private fun Double.decimalToDMS(): String {
    val degrees = toInt()
    val minutes = ((this - degrees) * 60).toInt()
    val seconds = ((this - degrees - minutes / 60.0) * 3600).toInt()
    return "$degrees°$minutes'$seconds\""
}