package com.vallem.sylph.presentation.components

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.location.Location
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import com.mapbox.android.core.location.LocationEngine
import com.vallem.sylph.util.extensions.onLocationEngineResult
import com.vallem.sylph.util.extensions.withLocation

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun rememberCurrentLocationState(
    locationEngine: LocationEngine,
    onFailure: (Throwable) -> Unit = {}
): MutableState<Location?> {
    val context = LocalContext.current
    val currentLocation = remember { mutableStateOf<Location?>(null) }

    val locationPermissionState = rememberPermissionState(
        permission = Manifest.permission.ACCESS_FINE_LOCATION
    )

    val requestLocationLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult()
    ) {
        if (it.resultCode != Activity.RESULT_OK) Toast.makeText(
            context,
            "Error trying to turn GPS on",
            Toast.LENGTH_SHORT
        ).show()
    }

    context.withLocation(requestLocationLauncher) {
        when (locationPermissionState.status) {
            is PermissionStatus.Denied -> locationPermissionState.launchPermissionRequest()

            PermissionStatus.Granted -> @SuppressLint("MissingPermission") {
                locationEngine.getLastLocation(onLocationEngineResult(onFailure) {
                    currentLocation.value = it
                })
            }
        }
    }

    return currentLocation
}