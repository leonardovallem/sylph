package com.vallem.sylph.shared.data.remote.impl

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient
import com.amazonaws.services.dynamodbv2.model.AttributeValue
import com.amazonaws.services.dynamodbv2.model.PutItemRequest
import com.amazonaws.services.dynamodbv2.model.ScanRequest
import com.amazonaws.services.dynamodbv2.model.Select
import com.mapbox.geojson.Feature
import com.mapbox.geojson.FeatureCollection
import com.vallem.sylph.shared.data.dynamo.DynamoDbClientStore
import com.vallem.sylph.shared.data.dynamo.DynamoTables
import com.vallem.sylph.shared.data.dynamo.dto.Event
import com.vallem.sylph.shared.data.mapper.EventMapper
import com.vallem.sylph.shared.data.mapper.toDynamoItem
import com.vallem.sylph.shared.data.remote.EventRemoteDataSource

class DynamoEventDataSource(
    private val client: AmazonDynamoDBClient? = DynamoDbClientStore.getClient()
) : EventRemoteDataSource {
    override suspend fun save(event: Event) = client?.putItem(
        PutItemRequest(DynamoTables.Events, event.toDynamoItem())
    )

    override suspend fun retrieveUserEvents(userId: String) = client?.scan(
        ScanRequest(DynamoTables.Events)
            .withSelect(Select.ALL_ATTRIBUTES)
            .apply {
                filterExpression = "publisher_id = :id"
                expressionAttributeValues = mapOf(":id" to AttributeValue(userId))
            }
    )
        ?.items
        ?.mapNotNull(EventMapper::fromDynamoItem)

    override suspend fun retrieveAllEvents() = client?.scan(
        ScanRequest(DynamoTables.Events).withProjectionExpression("feature")
    )
        ?.items
        ?.mapNotNull { it.values.firstOrNull()?.s }
        ?.map { Feature.fromJson(it) }
        ?.let(FeatureCollection::fromFeatures)
}