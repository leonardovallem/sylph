package com.vallem.sylph.shared.auth

import android.app.PendingIntent
import android.content.Intent
import android.os.Parcel
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.OAuthCredential
import kotlinx.coroutines.tasks.await

interface GoogleSignInClient {
    fun getCredentialsFromIntent(intent: Intent): AuthCredential
    suspend fun getGoogleSignInResult(): BeginSignInResult
}

class GoogleSignInClientImpl(
    private val signInClient: SignInClient,
    private val signInRequest: BeginSignInRequest,
) : GoogleSignInClient {
    override fun getCredentialsFromIntent(intent: Intent) =
        signInClient.getSignInCredentialFromIntent(intent).let {
            GoogleAuthProvider.getCredential(it.googleIdToken, null)
        }

    override suspend fun getGoogleSignInResult(): BeginSignInResult = signInClient
        .beginSignIn(signInRequest)
        .await()
}

class FakeGoogleSignInClient : GoogleSignInClient {
    override fun getCredentialsFromIntent(intent: Intent) = object : OAuthCredential() {
        override fun writeToParcel(dest: Parcel, flags: Int) = Unit
        override fun getProvider() = ""
        override fun getSignInMethod() = ""
        override fun zza() = this
        override fun getAccessToken() = ""
        override fun getIdToken() = ""
        override fun getSecret() = ""
    }

    override suspend fun getGoogleSignInResult() = BeginSignInResult(
        PendingIntent.readPendingIntentOrNullFromParcel(Parcel.obtain())!!
    )
}
