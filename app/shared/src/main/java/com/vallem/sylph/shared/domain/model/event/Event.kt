package com.vallem.sylph.shared.domain.model.event

import com.mapbox.geojson.Point
import com.vallem.sylph.shared.map.model.PointWrapper

sealed class Event<R : Reason>(
    open val point: PointWrapper,
    open val reasons: Set<R>,
    open val note: String
) {
    data class Safety(
        override val point: PointWrapper,
        override val reasons: Set<SafetyReason>,
        override val note: String
    ) : Event<SafetyReason>(point, reasons, note) {
        companion object {
            fun defaultFor(point: Point) = Safety(PointWrapper(point), emptySet(), "")
        }
    }

    data class Danger(
        override val point: PointWrapper,
        override val reasons: Set<DangerReason>,
        val victim: DangerVictim?,
        override val note: String
    ) : Event<DangerReason>(point, reasons, note) {
        companion object {
            fun defaultFor(point: Point) = Danger(PointWrapper(point), emptySet(), null, "")
        }
    }

    fun update(note: String) = when (this) {
        is Safety -> copy(note = note)
        is Danger -> copy(note = note)
    }
}