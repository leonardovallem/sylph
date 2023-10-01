package com.vallem.sylph.shared.data.datastore

data class AppSettings(
    val isLoggedIn: Boolean = false,
    val cameraState: MapCameraState = MapCameraState.Default,
)