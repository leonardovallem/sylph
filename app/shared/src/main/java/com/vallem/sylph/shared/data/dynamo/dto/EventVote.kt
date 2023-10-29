package com.vallem.sylph.shared.data.dynamo.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class EventVote(
    val eventId: String,
    val votingUserId: String,
    val eventPublisherId: String,
    val isUpVote: Boolean,
) : Parcelable
