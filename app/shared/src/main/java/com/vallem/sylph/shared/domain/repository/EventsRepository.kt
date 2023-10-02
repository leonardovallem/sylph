package com.vallem.sylph.shared.domain.repository

import com.amazonaws.services.dynamodbv2.model.PutItemResult
import com.mapbox.geojson.FeatureCollection
import com.vallem.sylph.shared.domain.model.Result
import com.vallem.sylph.shared.domain.model.event.Event
import com.vallem.sylph.shared.domain.model.event.EventDetails

interface EventsRepository {
    suspend fun saveEvent(event: Event): Result<PutItemResult>
    suspend fun retrieveEventDetails(eventId: String): Result<EventDetails?>
    suspend fun retrieveUserEvents(userId: String): Result<List<Event>>
    suspend fun retrieveEventsFeatures(): Result<FeatureCollection>
}