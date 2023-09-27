package com.vallem.sylph.shared.data.remote.impl

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient
import com.amazonaws.services.dynamodbv2.model.AttributeValue
import com.amazonaws.services.dynamodbv2.model.GetItemRequest
import com.vallem.sylph.shared.data.dynamo.DynamoDbClientStore
import com.vallem.sylph.shared.data.dynamo.DynamoTables
import com.vallem.sylph.shared.data.dynamo.dto.User
import com.vallem.sylph.shared.data.mapper.UserMapper
import com.vallem.sylph.shared.data.mapper.toDynamoItem
import com.vallem.sylph.shared.data.remote.UserRemoteDataSource

class DynamoUserDataSource(
    private val client: AmazonDynamoDBClient? = DynamoDbClientStore.getClient()
) : UserRemoteDataSource {
    override suspend fun save(user: User) = client?.putItem(
        DynamoTables.Users, user.toDynamoItem()
    )

    override suspend fun retrieveDetails(userId: String) = client?.getItem(
        GetItemRequest(DynamoTables.Users, mapOf("user_id" to AttributeValue(userId)))
    )
        ?.item
        ?.let(UserMapper::fromDynamoItem)
}