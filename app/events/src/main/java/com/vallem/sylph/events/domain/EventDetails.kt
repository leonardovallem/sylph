package com.vallem.sylph.events.domain

import android.os.Parcelable
import com.vallem.sylph.shared.domain.model.event.Event
import kotlinx.parcelize.Parcelize

sealed interface EventDetails : Parcelable {
    @Parcelize
    data class Sync(val event: Event) : EventDetails

    @Parcelize
    data class Async(val eventId: String, val showUserInfo: Boolean) : EventDetails
}