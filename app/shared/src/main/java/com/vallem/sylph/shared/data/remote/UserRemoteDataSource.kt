package com.vallem.sylph.shared.data.remote

import com.amazonaws.services.dynamodbv2.model.PutItemResult
import com.vallem.sylph.shared.data.dynamo.dto.User

interface UserRemoteDataSource {
    suspend fun save(user: User): PutItemResult?
    suspend fun retrieveDetails(userId: String): User?
}