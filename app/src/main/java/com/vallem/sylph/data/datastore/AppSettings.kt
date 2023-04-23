package com.vallem.sylph.data.datastore

data class MapCenter(val latitude: Double, val longitude: Double)

data class AppSettings(
    val isLoggedIn: Boolean = false,
    val mapCenter: MapCenter = MapCenter(0.0, 0.0)
)