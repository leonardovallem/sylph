package com.vallem.sylph.shared.data.repository

import com.vallem.sylph.shared.data.mapper.toVoteCount
import com.vallem.sylph.shared.data.remote.EventVotesRemoteDataSource
import com.vallem.sylph.shared.domain.exception.VoteCountRetrievalException
import com.vallem.sylph.shared.domain.exception.VoteSavingException
import com.vallem.sylph.shared.domain.model.Result
import com.vallem.sylph.shared.domain.model.event.EventVote
import com.vallem.sylph.shared.domain.model.event.toEventVote
import com.vallem.sylph.shared.domain.repository.EventUserVotesRepository
import com.vallem.sylph.shared.data.dynamo.dto.EventVote as EventVoteDto

class EventUserVotesRepositoryImpl(
    private val dataSource: EventVotesRemoteDataSource
) : EventUserVotesRepository {
    override suspend fun vote(eventId: String, votingUserId: String, vote: EventVote) = try {
        dataSource.vote(EventVoteDto(eventId, votingUserId, vote == EventVote.UpVote))
            ?.let { Result.Success(Unit) }
            ?: Result.Failure(VoteSavingException())
    } catch (e: Exception) {
        Result.Failure(e)
    }

    override suspend fun clearVote(eventId: String, votingUserId: String) = try {
        dataSource.clearVote(eventId, votingUserId)
            ?.let { Result.Success(Unit) }
            ?: Result.Failure(VoteSavingException())
    } catch (e: Exception) {
        Result.Failure(e)
    }

    override suspend fun retrieveUserVoteForEvent(userId: String, eventId: String) = try {
        dataSource.retrieveUserVoteForEvent(userId, eventId).let {
            Result.Success(it.toEventVote())
        }
    } catch (e: Exception) {
        Result.Failure(e)
    }

    override suspend fun retrieveVoteCountsForEvent(eventId: String) = try {
        dataSource.retrieveVotesForEvent(eventId)
            ?.toVoteCount()
            ?.let { Result.Success(it) }
            ?: Result.Failure(VoteCountRetrievalException())
    } catch (e: Exception) {
        Result.Failure(e)
    }

    override suspend fun retrieveVoteCountsForUserEvents(userId: String) = try {
        dataSource.retrieveVotesForUserEvents(userId)
            ?.toVoteCount()
            ?.let { Result.Success(it) }
            ?: Result.Failure(VoteCountRetrievalException())
    } catch (e: Exception) {
        Result.Failure(e)
    }
}