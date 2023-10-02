package com.vallem.sylph.shared.data.repository

import androidx.datastore.core.DataStore
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.vallem.sylph.shared.data.datastore.AppSettings
import com.vallem.sylph.shared.domain.model.Result
import com.vallem.sylph.shared.domain.repository.AuthRepository
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val dataStore: DataStore<AppSettings>,
) : AuthRepository {
    override val currentUser: FirebaseUser?
        get() = auth.currentUser

    override suspend fun login(email: String, password: String) = try {
        val result = auth.signInWithEmailAndPassword(email, password).await()
        dataStore.updateData { AppSettings(isLoggedIn = true) }
        Result.Success(result.user!!)
    } catch (e: Exception) {
        e.printStackTrace()
        Result.Failure(e)
    }

    override suspend fun signup(
        name: String,
        email: String,
        password: String
    ) = withContext(Dispatchers.IO) {
        try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            result.user?.updateProfile(
                UserProfileChangeRequest.Builder().setDisplayName(name).build()
            )?.await()
            Result.Success(result.user!!)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.Failure(e)
        }
    }

    override suspend fun logout() {
        auth.signOut()
        dataStore.updateData { AppSettings() }
    }
}