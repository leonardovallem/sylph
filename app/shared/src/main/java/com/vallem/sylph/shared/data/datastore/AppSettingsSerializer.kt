package com.vallem.sylph.shared.data.datastore

import androidx.datastore.core.Serializer
import com.fasterxml.jackson.databind.ObjectMapper
import java.io.InputStream
import java.io.OutputStream

class AppSettingsSerializer : Serializer<AppSettings> {
    private val mapper = ObjectMapper()

    override val defaultValue = AppSettings()

    override suspend fun readFrom(input: InputStream) = runCatching {
        input.use {
            mapper.readValue(it.readBytes().decodeToString(), AppSettings::class.java)
        }
    }.onFailure {
        it.printStackTrace()
    }.getOrNull() ?: defaultValue

    override suspend fun writeTo(t: AppSettings, output: OutputStream) {
        output.use {
            mapper.writeValueAsString(t)
        }
    }
}