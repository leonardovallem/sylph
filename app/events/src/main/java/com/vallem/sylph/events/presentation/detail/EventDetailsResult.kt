package com.vallem.sylph.events.presentation.detail

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed interface EventDetailsResult : Parcelable {
    @Parcelize
    data class ShowUserDetails(val userId: String) : EventDetailsResult
}