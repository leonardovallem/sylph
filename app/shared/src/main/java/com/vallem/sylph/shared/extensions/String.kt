package com.vallem.sylph.shared.extensions

import com.fasterxml.jackson.databind.ObjectMapper

val objectMapper = ObjectMapper()

fun <T> T.serializeAsJson() = runCatching { objectMapper.writeValueAsString(this) }
    .onFailure { it.printStackTrace() }
    .getOrNull()

inline fun <reified T> String.deserializeAsJson() = runCatching {
    objectMapper.readValue(this, T::class.java)
}
    .onFailure { it.printStackTrace() }
    .getOrNull()

operator fun String.times(count: Int): String {
    var str = ""
    for (i in 0 until count) str += this
    return str
}

fun String.truncate(size: Int) = if (length < size) this else take(size).plus("...")
