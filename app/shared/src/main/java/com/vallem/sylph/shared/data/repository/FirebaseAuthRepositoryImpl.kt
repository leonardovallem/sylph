package com.vallem.sylph.shared.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.vallem.sylph.shared.auth.AuthData
import com.vallem.sylph.shared.domain.model.Result
import com.vallem.sylph.shared.domain.repository.FirebaseAuthRepository
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.tasks.await

@Singleton
class FirebaseAuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
) : FirebaseAuthRepository {
    override val currentUser: FirebaseUser?
        get() = auth.currentUser

    override suspend fun auth(data: AuthData) = try {
        Result.Success(
            when (data) {
                is AuthData.Email.Login -> auth.signInWithEmailAndPassword(
                    data.email,
                    data.password
                ).await().user!!

                is AuthData.Email.Register -> auth.createUserWithEmailAndPassword(
                    data.email,
                    data.password
                ).await().user?.apply {
                    updateProfile(
                        UserProfileChangeRequest.Builder()
                            .setDisplayName(data.name)
                            .build()
                    ).await()
                }!!

                is AuthData.Google.OneTapSignIn -> auth.signInWithCredential(data.credential)
                    .await()
                    .user!!
            }
        )
    } catch (e: Exception) {
        e.printStackTrace()
        Result.Failure(e)
    }

    override suspend fun logout() {
        auth.signOut()
    }
}