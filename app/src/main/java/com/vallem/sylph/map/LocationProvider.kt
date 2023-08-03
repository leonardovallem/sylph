package com.vallem.sylph.map

import android.Manifest.permission
import android.annotation.SuppressLint
import android.content.Context
import android.location.GnssStatus
import android.location.LocationManager
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.annotation.RequiresPermission
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.Granularity
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.Priority
import com.vallem.sylph.util.extensions.hasPermission
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class LocationProvider internal constructor(
    private val context: Context,
    updateInterval: Long = 10_000L
) {
    private val locationRequests = listOf(
        LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, updateInterval),
        LocationRequest.Builder(Priority.PRIORITY_BALANCED_POWER_ACCURACY, updateInterval)
    ).map {
        it
            .setGranularity(Granularity.GRANULARITY_PERMISSION_LEVEL)
            .setWaitForAccurateLocation(true)
            .build()
    }

    private val locationSettingsRequest = LocationSettingsRequest.Builder()
        .addAllLocationRequests(locationRequests)
        .build()

    @SuppressLint("MissingPermission")
    @RequiresPermission(permission.ACCESS_FINE_LOCATION)
    val isLocationEnabled = callbackFlow {
        val manager = context.getSystemService(LocationManager::class.java)

        val callback = object : GnssStatus.Callback() {
            override fun onStarted() {
                trySend(true)
            }

            override fun onStopped() {
                trySend(false)
            }
        }

        if (context.hasPermission(permission.ACCESS_FINE_LOCATION)) {
            manager.registerGnssStatusCallback(callback, null)
        }

        awaitClose { manager.unregisterGnssStatusCallback(callback) }
    }

    @SuppressLint("MissingPermission")
    @RequiresPermission(anyOf = ["android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"])
    val location = callbackFlow {
        val client = LocationServices.getFusedLocationProviderClient(context)
        val callback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                trySend(result.lastLocation)
            }
        }

        if (context.hasPermission(permission.ACCESS_FINE_LOCATION)) {
            client.lastLocation.await()?.let { send(it) }
            locationRequests.forEach { client.requestLocationUpdates(it, callback, null) }
        }

        awaitClose { client.removeLocationUpdates(callback) }
    }

    fun requestEnableLocation(launcher: ManagedActivityResultLauncher<IntentSenderRequest, ActivityResult>) =
        LocationServices.getSettingsClient(context)
            .checkLocationSettings(locationSettingsRequest)
            .addOnFailureListener {
                if (it is ResolvableApiException) {
                    val intentSenderRequest = IntentSenderRequest.Builder(it.resolution).build()
                    launcher.launch(intentSenderRequest)
                }
            }
}

@Composable
fun rememberLocationProvider(updateInterval: Long = 10_000L): LocationProvider {
    val context = LocalContext.current
    return remember { LocationProvider(context, updateInterval) }
}