package com.vallem.sylph.shared.data.dynamo

import com.amazonaws.ClientConfiguration
import com.amazonaws.regions.Region
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient
import com.vallem.sylph.shared.data.indentity.CredentialsProviderStore

object DynamoDbClientStore {
    private var client: AmazonDynamoDBClient? = null

    fun getClient(): AmazonDynamoDBClient? {
        if (client == null) client = Region.getRegion("us-west-2").createClient(
            AmazonDynamoDBClient::class.java,
            CredentialsProviderStore.getCredentialsProvider(),
            ClientConfiguration()
        )

        return client
    }
}