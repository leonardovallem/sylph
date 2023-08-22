package com.vallem.sylph.shared.data.mapper

import com.amazonaws.services.dynamodbv2.model.AttributeValue
import com.mapbox.geojson.Point
import com.vallem.sylph.shared.domain.model.event.DangerReason
import com.vallem.sylph.shared.domain.model.event.DangerVictim
import com.vallem.sylph.shared.domain.model.event.Event
import com.vallem.sylph.shared.domain.model.event.SafetyReason
import com.vallem.sylph.shared.map.model.PointWrapper
import io.hypersistence.tsid.TSID
import com.vallem.sylph.shared.data.dynamo.dto.Event as EventDto

fun Event<*>.toDto(userId: String) = EventDto(
    pointJson = point.value.toJson(),
    reasons = reasons.map { it.enumName }.toSet(),
    victim = (this as? Event.Danger)?.victim?.name,
    note = note,
    publisherId = userId
)

fun EventDto.toEvent(): Event<*>? {
    val point = try {
        Point.fromJson(pointJson)
    } catch (_: Throwable) {
        return null
    }

    return if (victim == null) Event.Safety(
        point = PointWrapper(point),
        reasons = reasons.mapNotNull { SafetyReason[it] }.toSet(),
        note = note
    ) else Event.Danger(
        point = PointWrapper(point),
        reasons = reasons.mapNotNull { DangerReason[it] }.toSet(),
        victim = DangerVictim[victim],
        note = note
    )
}

fun EventDto.toDynamoItem() = mutableMapOf<String, AttributeValue>().apply {
    this["point"] = AttributeValue(pointJson)
    this["reasons"] = AttributeValue(reasons.toList())
    victim?.let { this["victim"] = AttributeValue(it) }
    this["note"] = AttributeValue(note)
    this["publisher_id"] = AttributeValue(publisherId)
    this["event_id"] = AttributeValue(eventId ?: TSID.fast().toString())
}

object EventMapper {
    fun fromDynamoItem(item: Map<String, AttributeValue>): EventDto? {
        val point = item["point"]?.s ?: return null
        val publisherId = item["publisher_id"]?.s ?: return null

        val reasons = item["reasons"]?.ss?.toSet().orEmpty()
        val victim = item["victim"]?.s
        val note = item["note"]?.s.orEmpty()
        val eventId = item["event_id"]?.s.orEmpty()

        return EventDto(point, reasons, victim, note, publisherId, eventId)
    }
}
