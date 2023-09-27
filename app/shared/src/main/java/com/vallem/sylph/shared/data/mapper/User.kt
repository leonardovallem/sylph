package com.vallem.sylph.shared.data.mapper

import com.amazonaws.services.dynamodbv2.model.AttributeValue
import com.vallem.sylph.shared.data.dynamo.dto.User

fun User.toDynamoItem() = mutableMapOf<String, AttributeValue>().apply {
    this["user_id"] = AttributeValue(userId)
    this["name"] = AttributeValue(name)
    this["pic_url"] = AttributeValue(picUrl)
}

object UserMapper {
    fun fromDynamoItem(item: Map<String, AttributeValue>): User? {
        return User(
            name = item["name"]?.s ?: return null,
            picUrl = item["pic_url"]?.s,
            userId = item["user_id"]?.s ?: return null,
        )
    }
}