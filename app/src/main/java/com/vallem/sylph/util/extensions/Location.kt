package com.vallem.sylph.util.extensions

import android.content.Context
import android.location.Location
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsResponse
import com.mapbox.android.core.location.LocationEngineCallback
import com.mapbox.android.core.location.LocationEngineResult
import com.mapbox.geojson.Point
import com.vallem.sylph.data.datastore.MapCenter

val Location.point: Point
    get() = Point.fromLngLat(longitude, latitude)

val Location.mapCenter: MapCenter
    get() = MapCenter(latitude, longitude)

fun onLocationEngineResult(onFailure: (Throwable) -> Unit, onSuccess: (Location?) -> Unit) =
    object : LocationEngineCallback<LocationEngineResult> {
        override fun onSuccess(result: LocationEngineResult) = onSuccess(result.lastLocation)

        override fun onFailure(exception: Exception) = onFailure(exception)
    }

fun LocationRequest.withHighAccuracy() = apply { priority = LocationRequest.PRIORITY_HIGH_ACCURACY }
fun LocationRequest.withBalancedPowerAccuracy() = apply {
    priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
}

fun Context.withLocation(
    launcher: ActivityResultLauncher<IntentSenderRequest>,
    action: (LocationSettingsResponse) -> Unit
) {
    val locationRequests = listOf(
        LocationRequest.create().withHighAccuracy(),
        LocationRequest.create().withBalancedPowerAccuracy(),
    )

    val locationRequest = LocationSettingsRequest.Builder()
        .addAllLocationRequests(locationRequests)
        .build()

    LocationServices.getSettingsClient(this)
        .checkLocationSettings(locationRequest)
        .addOnSuccessListener(action)
        .addOnFailureListener {
            val intentSender = (it as ResolvableApiException).resolution.intentSender
            val request = IntentSenderRequest.Builder(intentSender).build()
            launcher.launch(request)
        }
}
