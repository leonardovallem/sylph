package com.vallem.sylph.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.vallem.sylph.domain.model.Result
import com.vallem.sylph.domain.repository.AuthRepository
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.tasks.await

@Singleton
class AuthRepositoryImpl @Inject constructor(private val auth: FirebaseAuth) : AuthRepository {
    override val currentUser: FirebaseUser?
        get() = auth.currentUser

    override suspend fun login(email: String, password: String) = try {
        val result = auth.signInWithEmailAndPassword(email, password).await()
        Result.Success(result.user!!)
    } catch (e: Exception) {
        e.printStackTrace()
        Result.Failure(e)
    }

    override suspend fun signup(
        name: String,
        email: String,
        password: String
    ) = try {
        val result = auth.createUserWithEmailAndPassword(email, password).await()
        result.user?.updateProfile(
            UserProfileChangeRequest.Builder().setDisplayName(name).build()
        )?.await()
        Result.Success(result.user!!)
    } catch (e: Exception) {
        e.printStackTrace()
        Result.Failure(e)
    }

    override fun logout() = auth.signOut()
}