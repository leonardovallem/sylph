package com.vallem.sylph.shared.domain.model.event

import android.os.Parcelable
import com.mapbox.geojson.Point
import com.vallem.sylph.shared.map.model.PointWrapper
import kotlinx.parcelize.Parcelize

interface Event : Parcelable {
    val point: PointWrapper
    val reasons: Set<Reason>
    val note: String
    val userId: String

    fun update(
        reasons: Set<Reason> = this.reasons,
        note: String = this.note,
        userId: String = this.userId,
        victim: DangerVictim? = (this as? DangerEvent)?.victim
    ): Event

    enum class Type {
        Danger, Safety
    }

    val type: Type
        get() = when (this) {
            is SafetyEvent -> Type.Safety
            else -> Type.Danger
        }
}

@Parcelize
data class SafetyEvent(
    override val point: PointWrapper,
    override val reasons: Set<SafetyReason>,
    override val note: String,
    override val userId: String
) : Event {
    override fun update(reasons: Set<Reason>, note: String, userId: String, victim: DangerVictim?) =
        copy(
            reasons = reasons.filterIsInstance<SafetyReason>().toSet(),
            note = note,
            userId = userId
        )

    companion object {
        fun defaultFor(point: Point) = SafetyEvent(PointWrapper(point), emptySet(), "", "")
    }
}

@Parcelize
data class DangerEvent(
    override val point: PointWrapper,
    override val reasons: Set<DangerReason>,
    val victim: DangerVictim?,
    override val note: String,
    override val userId: String
) : Event {
    override fun update(
        reasons: Set<Reason>,
        note: String,
        userId: String,
        victim: DangerVictim?
    ) = copy(
        reasons = reasons.filterIsInstance<DangerReason>().toSet(),
        note = note,
        userId = userId,
        victim = victim
    )

    companion object {
        fun defaultFor(point: Point) = DangerEvent(PointWrapper(point), emptySet(), null, "", "")
    }
}
