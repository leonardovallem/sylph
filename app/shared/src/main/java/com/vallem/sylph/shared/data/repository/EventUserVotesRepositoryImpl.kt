package com.vallem.sylph.shared.data.repository

import com.vallem.sylph.shared.data.dynamo.DynamoDbInstantiationException
import com.vallem.sylph.shared.data.mapper.toVoteCount
import com.vallem.sylph.shared.data.remote.EventVotesRemoteDataSource
import com.vallem.sylph.shared.domain.model.Result
import com.vallem.sylph.shared.domain.model.event.EventVote
import com.vallem.sylph.shared.domain.repository.EventUserVotesRepository
import com.vallem.sylph.shared.data.dynamo.dto.EventVote as EventVoteDto

class EventUserVotesRepositoryImpl(
    private val dataSource: EventVotesRemoteDataSource
) : EventUserVotesRepository {
    override suspend fun vote(eventId: String, votingUserId: String, vote: EventVote) = try {
        dataSource.vote(EventVoteDto(eventId, votingUserId, vote == EventVote.UpVote))
            ?.let { Result.Success(Unit) }
            ?: Result.Failure(DynamoDbInstantiationException())
    } catch (e: Exception) {
        Result.Failure(e)
    }

    override suspend fun retrieveVoteCountsForEvent(eventId: String) = try {
        dataSource.retrieveVotesForEvent(eventId)
            ?.toVoteCount()
            ?.let { Result.Success(it) }
            ?: Result.Failure(DynamoDbInstantiationException())
    } catch (e: Exception) {
        Result.Failure(e)
    }

    override suspend fun retrieveVoteCountsForUserEvents(userId: String) = try {
        dataSource.retrieveVotesForUserEvents(userId)
            ?.toVoteCount()
            ?.let { Result.Success(it) }
            ?: Result.Failure(DynamoDbInstantiationException())
    } catch (e: Exception) {
        Result.Failure(e)
    }
}