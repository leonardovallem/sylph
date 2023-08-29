package com.vallem.sylph.shared.extensions

import android.content.Intent
import android.net.Uri
import com.mapbox.geojson.Point

fun Point.asGeoIntent(zoom: Double = 16.0) = Intent(
    Intent.ACTION_VIEW,
    Uri.parse("geo:${latitude()},${longitude()}?z=$zoom")
)

val Point.formatAsDMS: String
    get() = "${latitude().decimalToDMS()}, ${longitude().decimalToDMS()}"

private fun Double.decimalToDMS(): String {
    val degrees = toInt()
    val minutes = ((this - degrees) * 60).toInt()
    val seconds = ((this - degrees - minutes / 60.0) * 3600).toInt()
    return "$degrees°$minutes'$seconds\""
}