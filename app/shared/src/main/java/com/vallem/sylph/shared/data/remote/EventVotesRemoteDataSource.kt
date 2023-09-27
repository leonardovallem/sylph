package com.vallem.sylph.shared.data.remote

import com.amazonaws.services.dynamodbv2.model.PutItemResult
import com.vallem.sylph.shared.data.dynamo.dto.EventVote

interface EventVotesRemoteDataSource {
    suspend fun vote(vote: EventVote): PutItemResult?
    suspend fun retrieveVotesForEvent(eventId: String): List<EventVote>?
    suspend fun retrieveVotesForUserEvents(userId: String): List<EventVote>?
}