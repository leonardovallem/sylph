package com.vallem.sylph.shared.domain.model.event

enum class EventVote {
    UpVote, DownVote
}

fun Boolean?.toEventVote() = when (this) {
    true -> EventVote.UpVote
    false -> EventVote.DownVote
    null -> null
}