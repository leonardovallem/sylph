package com.vallem.sylph.shared.data.mapper

import com.amazonaws.services.dynamodbv2.model.AttributeValue
import com.google.gson.JsonObject
import com.mapbox.geojson.Feature
import com.mapbox.geojson.Point
import com.vallem.sylph.shared.domain.model.event.DangerEvent
import com.vallem.sylph.shared.domain.model.event.DangerReason
import com.vallem.sylph.shared.domain.model.event.DangerVictim
import com.vallem.sylph.shared.domain.model.event.Event
import com.vallem.sylph.shared.domain.model.event.SafetyEvent
import com.vallem.sylph.shared.domain.model.event.SafetyReason
import com.vallem.sylph.shared.map.model.PointWrapper
import io.hypersistence.tsid.TSID
import com.vallem.sylph.shared.data.dynamo.dto.Event as EventDto

fun Event.toDto() = EventDto(
    featureJson = point.value.toFeature(type).toJson(),
    reasons = reasons.map { it.enumName }.toSet(),
    victim = (this as? DangerEvent)?.victim?.name,
    note = note,
    publisherId = userId
)

fun Point.toFeature(eventType: Event.Type) = Feature.fromGeometry(
    this,
    JsonObject().apply {
        addProperty("eventType", eventType.name)
    }
)

fun EventDto.toEvent(): Event? {
    val feature = try {
        Feature.fromJson(featureJson)
    } catch (_: Throwable) {
        return null
    }

    val isSafety = feature.properties()?.get("eventType")?.asString == Event.Type.Safety.name
    val point = feature.geometry() as? Point ?: return null

    return if (isSafety || victim == null) SafetyEvent(
        point = PointWrapper(point),
        reasons = reasons.mapNotNull { SafetyReason[it] }.toSet(),
        note = note,
        userId = publisherId
    ) else DangerEvent(
        point = PointWrapper(point),
        reasons = reasons.mapNotNull { DangerReason[it] }.toSet(),
        victim = DangerVictim[victim],
        note = note,
        userId = publisherId
    )
}

fun EventDto.toDynamoItem() = mutableMapOf<String, AttributeValue>().apply {
    this["feature"] = AttributeValue(featureJson)
    this["reasons"] = AttributeValue(reasons.toList())
    victim?.let { this["victim"] = AttributeValue(it) }
    this["note"] = AttributeValue(note)
    this["publisher_id"] = AttributeValue(publisherId)
    this["event_id"] = AttributeValue(eventId ?: TSID.fast().toString())
}

object EventMapper {
    fun fromDynamoItem(item: Map<String, AttributeValue>): EventDto? {
        val feature = item["feature"]?.s ?: return null
        val publisherId = item["publisher_id"]?.s ?: return null

        val reasons = item["reasons"]?.ss?.toSet().orEmpty()
        val victim = item["victim"]?.s
        val note = item["note"]?.s.orEmpty()
        val eventId = item["event_id"]?.s.orEmpty()

        return EventDto(feature, reasons, victim, note, publisherId, eventId)
    }
}
