package com.vallem.sylph.shared.data.datastore

import com.vallem.sylph.shared.map.model.MapCenter

data class AppSettings(
    val isLoggedIn: Boolean = false,
    val mapCenter: MapCenter = MapCenter(0.0, 0.0)
)