package com.vallem.sylph.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import com.mapbox.geojson.Point
import com.mapbox.maps.MapInitOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.ResourceOptions
import com.mapbox.maps.Style
import com.mapbox.maps.extension.localization.localizeLabels
import com.mapbox.maps.plugin.LocationPuck2D
import com.mapbox.maps.plugin.locationcomponent.location
import com.vallem.sylph.BuildConfig
import com.vallem.sylph.R
import com.vallem.sylph.map.MapState
import com.vallem.sylph.map.rememberMapState
import com.vallem.sylph.util.extensions.getDrawable

@Composable
fun MapBox(
    state: MapState,
    accessToken: String,
    modifier: Modifier = Modifier,
) {
    AndroidView(
        modifier = modifier,
        factory = {
            MapView(
                context = it,
                mapInitOptions = MapInitOptions(
                    context = it,
                    resourceOptions = ResourceOptions.Builder()
                        .accessToken(accessToken)
                        .build()
                )
            )
        }
    ) { view ->
        state.setView(view)

        view.getMapboxMap().run {
            getStyle {
                it.localizeLabels(view.context.resources.configuration.locales[0])
            }

            loadStyleUri(Style.MAPBOX_STREETS) {
                view.location.updateSettings {
                    enabled = true
                    locationPuck = LocationPuck2D(
                        topImage = view.context.getDrawable(
                            R.drawable.ic_location_on_24,
                            com.vallem.componentlibrary.R.color.purple_200
                        )
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun MapBoxPreview() {
    MapBox(
        state = rememberMapState(Point.fromLngLat(0.0, 0.0)),
        accessToken = BuildConfig.MAP_BOX_API_TOKEN
    )
}
