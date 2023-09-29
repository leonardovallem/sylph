package com.vallem.sylph.shared.data.remote.impl

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient
import com.amazonaws.services.dynamodbv2.model.AttributeValue
import com.amazonaws.services.dynamodbv2.model.QueryRequest
import com.vallem.sylph.shared.data.dynamo.DynamoDbClientStore
import com.vallem.sylph.shared.data.dynamo.DynamoTables
import com.vallem.sylph.shared.data.dynamo.dto.EventVote
import com.vallem.sylph.shared.data.mapper.EventVoteMapper
import com.vallem.sylph.shared.data.mapper.toDynamoItem
import com.vallem.sylph.shared.data.remote.EventVotesRemoteDataSource

class DynamoEventVotesDataSource(
    private val client: AmazonDynamoDBClient? = DynamoDbClientStore.getClient()
) : EventVotesRemoteDataSource {
    override suspend fun vote(vote: EventVote) = client?.putItem(
        DynamoTables.EventUserVotes, vote.toDynamoItem()
    )

    override suspend fun retrieveVotesForEvent(eventId: String) = client?.query(
        QueryRequest(DynamoTables.EventUserVotes).apply {
            keyConditionExpression = "event_id = :eventId"
            expressionAttributeValues = mapOf(":eventId" to AttributeValue(eventId))
        }
    )
        ?.items
        ?.mapNotNull(EventVoteMapper::fromDynamoItem)

    override suspend fun retrieveVotesForUserEvents(userId: String) = client?.query(
        QueryRequest(DynamoTables.EventUserVotes).apply {
            keyConditionExpression = "user_id = :userId"
            expressionAttributeValues = mapOf(":userId" to AttributeValue(userId))
        }
    )
        ?.items
        ?.mapNotNull(EventVoteMapper::fromDynamoItem)
}