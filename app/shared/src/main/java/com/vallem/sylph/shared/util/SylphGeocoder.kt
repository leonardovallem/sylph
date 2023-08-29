package com.vallem.sylph.shared.util

import android.content.Context
import android.location.Geocoder
import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.mapbox.geojson.Point
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.withContext

class SylphGeocoder(context: Context) {
    private val geocoder = Geocoder(context)

    fun infoFor(point: Point, maxResults: Int = 1) = callbackFlow {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) geocoder.getFromLocation(
            point.latitude(),
            point.longitude(),
            maxResults
        ) {
            trySend(it)
        } else {
            val result = withContext(Dispatchers.IO) {
                geocoder.getFromLocation(
                    point.latitude(),
                    point.longitude(),
                    maxResults
                )
            }

            trySend(result)
        }

        awaitClose {}
    }

    @Composable
    fun briefLocationDetails(point: Point) = infoFor(point).collectAsState(initial = null)
        .value
        ?.firstOrNull()
        ?.getAddressLine(0)
}

@Composable
fun rememberGeocoder(): SylphGeocoder {
    val context = LocalContext.current
    return remember { SylphGeocoder(context) }
}