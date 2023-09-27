package com.vallem.sylph.shared.data.mapper

import com.amazonaws.services.dynamodbv2.model.AttributeValue
import com.vallem.sylph.shared.data.dynamo.dto.EventVote
import com.vallem.sylph.shared.domain.model.event.VoteCount

fun List<EventVote>.toVoteCount() = partition {
    it.isUpVote
}.let { VoteCount(it.first.size, it.second.size) }

fun EventVote.toDynamoItem() = mutableMapOf<String, AttributeValue>().apply {
    this["event_id"] = AttributeValue(eventId)
    this["user_id"] = AttributeValue(votingUserId)
    this["isUpVote"] = AttributeValue().withBOOL(isUpVote)
}

object EventVoteMapper {
    fun fromDynamoItem(item: Map<String, AttributeValue>): EventVote? {
        return EventVote(
            eventId = item["event_id"]?.s ?: return null,
            votingUserId = item["user_id"]?.s ?: return null,
            isUpVote = item["isUpVote"]?.bool ?: return null,
        )
    }
}