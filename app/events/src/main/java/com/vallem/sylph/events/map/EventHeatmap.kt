package com.vallem.sylph.events.map

import com.mapbox.geojson.FeatureCollection
import com.mapbox.maps.extension.style.expressions.dsl.generated.all
import com.mapbox.maps.extension.style.expressions.dsl.generated.interpolate
import com.mapbox.maps.extension.style.expressions.generated.Expression
import com.mapbox.maps.extension.style.layers.generated.HeatmapLayerDsl
import com.mapbox.maps.extension.style.layers.generated.heatmapLayer
import com.mapbox.maps.extension.style.sources.generated.GeoJsonSource
import com.mapbox.maps.extension.style.sources.generated.geoJsonSource
import com.vallem.sylph.events.map.EventHeatmap.Layers.Constants.filterEventType
import com.vallem.sylph.shared.domain.model.event.Event
import kotlin.math.max
import kotlin.math.min

object EventHeatmap {
    const val EventDataSourceId = "EVENT_DATA"

    private var cachedSource: GeoJsonSource? = null

    fun sourceFrom(features: FeatureCollection, forceReload: Boolean = false): GeoJsonSource {
        if (cachedSource == null || forceReload) cachedSource = geoJsonSource(EventDataSourceId) {
            featureCollection(features)
        }

        return cachedSource!!
    }

    object Layers {
        object Id {
            const val Danger = "DANGER_EVENT_HEATMAP_LAYER"
            const val Safety = "SAFETY_EVENT_HEATMAP_LAYER"
        }

        private object Constants {
            const val Radius = 20.0
            const val Intensity = 0.9

            fun HeatmapLayerDsl.filterEventType(eventType: Event.Type) = filter(
                all {
                    eq {
                        get("eventType")
                        literal(eventType.name)
                    }
                }
            )
        }

        val Danger = heatmapLayer(
            layerId = Id.Danger,
            sourceId = EventDataSourceId
        ) {
            filterEventType(Event.Type.Danger)

            heatmapRadius(Constants.Radius)
            heatmapIntensity(Constants.Intensity)

            heatmapColor(
                interpolate {
                    linear()
                    heatmapDensity()

                    stop {
                        literal(0)
                        rgba(0.0, 0.0, 255.0, 0.0)
                    }
                    stop {
                        literal(0.1)
                        hsl(51f, 0.31f, 0.81f)
                    }
                    stop {
                        literal(0.3)
                        hsl(43f, 0.63f, 0.72f)
                    }
                    stop {
                        literal(0.5)
                        hsl(25f, 0.65f, 0.64f)
                    }
                    stop {
                        literal(0.7)
                        hsl(12f, 0.72f, 0.5f)
                    }
                    stop {
                        literal(1)
                        rgb(255.0, 0.0, 0.0)
                    }
                }
            )
        }

        val Safety = heatmapLayer(
            layerId = Id.Safety,
            sourceId = EventDataSourceId
        ) {
            filterEventType(Event.Type.Safety)

            heatmapRadius(Constants.Radius)
            heatmapIntensity(Constants.Intensity)

            heatmapColor(
                interpolate {
                    linear()
                    heatmapDensity()

                    stop {
                        literal(0)
                        rgba(0.0, 0.0, 255.0, 0.0)
                    }
                    stop {
                        literal(0.1)
                        hsl(186f, 0.76f, 0.82f)
                    }
                    stop {
                        literal(0.3)
                        hsl(163f, 0.68f, 0.6f)
                    }
                    stop {
                        literal(0.5)
                        hsl(120f, 0.4f, 0.58f)
                    }
                    stop {
                        literal(0.7)
                        hsl(103f, 0.76f, 0.54f)
                    }
                    stop {
                        literal(1)
                        hsl(120f, 1f, 0.5f)
                    }
                }
            )
        }
    }
}

private fun Expression.ExpressionBuilder.hsl(
    hue: Float,
    saturation: Float,
    lightness: Float,
    alpha: Float = 1f,
) {
    require(hue in 0f..360f && saturation in 0f..1f && lightness in 0f..1f) {
        "HSL ($hue, $saturation, $lightness) must be in range (0..360, 0..1, 0..1)"
    }
    val red = hslToRgbComponent(0, hue, saturation, lightness) * 255.0
    val green = hslToRgbComponent(8, hue, saturation, lightness) * 255.0
    val blue = hslToRgbComponent(4, hue, saturation, lightness) * 255.0
    addArgument(Expression.rgba(red, green, blue, alpha.toDouble()))
}

private fun hslToRgbComponent(n: Int, h: Float, s: Float, l: Float): Double {
    val k = (n.toFloat() + h / 30f) % 12f
    val a = s * min(l, 1f - l)
    return l - a * max(-1f, minOf(k - 3, 9 - k, 1f)).toDouble()
}