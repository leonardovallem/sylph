package com.vallem.sylph.shared.data.indentity

import com.amazonaws.auth.AWSCredentialsProvider
import com.amazonaws.auth.CognitoCredentialsProvider
import com.amazonaws.regions.Regions
import com.vallem.sylph.shared.BuildConfig

object CredentialsProviderStore {
    private var credentialsProvider: AWSCredentialsProvider? = null

    fun getCredentialsProvider(): AWSCredentialsProvider? {
        if (credentialsProvider != null) return credentialsProvider

        return CognitoCredentialsProvider(
            BuildConfig.COGNITO_IDENTITY_ID,
            Regions.US_WEST_2
        )
    }
}