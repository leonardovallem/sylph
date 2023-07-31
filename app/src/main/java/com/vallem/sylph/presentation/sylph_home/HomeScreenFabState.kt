package com.vallem.sylph.presentation.sylph_home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.vallem.sylph.map.LocationProvider

enum class HomeScreenFabState {
    LocationDisabled, LocationNotLoaded, LocationFound
}

val LocationProvider.fabState: HomeScreenFabState
    @Composable get() {
        val location by location.collectAsState(initial = null)
        val isLocationEnabled by isLocationEnabled.collectAsState(initial = false)

        return when {
            !isLocationEnabled -> HomeScreenFabState.LocationDisabled
            location != null -> HomeScreenFabState.LocationFound
            else -> HomeScreenFabState.LocationNotLoaded
        }
    }