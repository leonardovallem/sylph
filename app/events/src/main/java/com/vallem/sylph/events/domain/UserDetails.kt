package com.vallem.sylph.events.domain

data class UserDetails(
    val name: String,
    val picUrl: String?,
    val eventsMetaData: UserEventsMetaData,
)

data class UserEventsMetaData(
    val totalPublishedEvents: Int,
    val eventsUpVotes: Int,
    val eventsDownVotes: Int,
) {
    val totalEventsVotes = eventsUpVotes + eventsDownVotes
}
