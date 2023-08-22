package com.vallem.sylph.shared.data.dynamo.dto

data class Event(
    val pointJson: String,
    val reasons: Set<String>,
    val victim: String?,
    val note: String,
    val publisherId: String,
    val eventId: String? = null,
)
