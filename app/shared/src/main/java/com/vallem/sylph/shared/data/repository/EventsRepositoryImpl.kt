package com.vallem.sylph.shared.data.repository

import com.vallem.sylph.shared.data.mapper.toDto
import com.vallem.sylph.shared.data.mapper.toEvent
import com.vallem.sylph.shared.data.remote.EventRemoteDataSource
import com.vallem.sylph.shared.domain.exception.EventFeaturesRetrievalException
import com.vallem.sylph.shared.domain.exception.EventNotFoundException
import com.vallem.sylph.shared.domain.exception.EventSavingException
import com.vallem.sylph.shared.domain.exception.UserEventsRetrievalException
import com.vallem.sylph.shared.domain.model.Result
import com.vallem.sylph.shared.domain.model.event.Event
import com.vallem.sylph.shared.domain.model.event.EventDetails
import com.vallem.sylph.shared.domain.model.event.VoteCount
import com.vallem.sylph.shared.domain.repository.EventUserVotesRepository
import com.vallem.sylph.shared.domain.repository.EventsRepository

class EventsRepositoryImpl(
    private val dataSource: EventRemoteDataSource,
    private val votesRepository: EventUserVotesRepository,
) : EventsRepository {
    override suspend fun saveEvent(event: Event) = try {
        dataSource.save(event.toDto())
            ?.let { Result.Success(it) }
            ?: Result.Failure(EventSavingException())
    } catch (e: Exception) {
        Result.Failure(e)
    }

    override suspend fun retrieveEventDetails(eventId: String) = try {
        val voteCount = votesRepository.retrieveVoteCountsForEvent(eventId).getOrNull()

        dataSource.retrieveEventDetails(eventId)?.let { event ->
            event.toEvent()?.let {
                Result.Success(
                    EventDetails(
                        event = it,
                        voteCount = voteCount ?: VoteCount(0, 0)
                    )
                )
            }
        } ?: Result.Failure(EventNotFoundException())
    } catch (e: Exception) {
        Result.Failure(e)
    }

    override suspend fun retrieveUserEvents(userId: String) = try {
        val response = dataSource.retrieveUserEvents(userId)

        response
            ?.mapNotNull { it.toEvent() }
            ?.let { Result.Success(it) }
            ?: Result.Failure(UserEventsRetrievalException())
    } catch (e: Exception) {
        Result.Failure(e)
    }

    override suspend fun retrieveEventsFeatures() = try {
        dataSource.retrieveAllEvents()
            ?.let { Result.Success(it) }
            ?: Result.Failure(EventFeaturesRetrievalException())
    } catch (e: Exception) {
        Result.Failure(e)
    }
}