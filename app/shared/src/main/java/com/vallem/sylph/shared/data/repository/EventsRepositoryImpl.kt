package com.vallem.sylph.shared.data.repository

import com.vallem.sylph.shared.data.dynamo.DynamoDbInstantiationException
import com.vallem.sylph.shared.data.mapper.toDto
import com.vallem.sylph.shared.data.mapper.toEvent
import com.vallem.sylph.shared.data.remote.EventRemoteDataSource
import com.vallem.sylph.shared.domain.model.Result
import com.vallem.sylph.shared.domain.model.event.Event
import com.vallem.sylph.shared.domain.repository.EventsRepository

class EventsRepositoryImpl(
    private val dataSource: EventRemoteDataSource
) : EventsRepository {
    override suspend fun saveEvent(event: Event) = try {
        dataSource.save(event.toDto())
            ?.let { Result.Success(it) }
            ?: Result.Failure(DynamoDbInstantiationException())
    } catch (e: Exception) {
        Result.Failure(e)
    }

    override suspend fun retrieveUserEvents(userId: String) = try {
        val response = dataSource.retrieveUserEvents(userId)

        response
            ?.mapNotNull { it.toEvent() }
            ?.let { Result.Success(it) }
            ?: Result.Failure(DynamoDbInstantiationException())
    } catch (e: Exception) {
        Result.Failure(e)
    }
}