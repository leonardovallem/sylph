package com.vallem.sylph.shared.data.remote

import com.amazonaws.services.dynamodbv2.model.PutItemResult
import com.mapbox.geojson.FeatureCollection
import com.vallem.sylph.shared.data.dynamo.dto.Event

interface EventRemoteDataSource {
    suspend fun save(event: Event): PutItemResult?
    suspend fun retrieveEventDetails(id: String): Event?
    suspend fun retrieveUserEvents(userId: String): List<Event>?
    suspend fun retrieveAllEvents(): FeatureCollection?
}