package com.vallem.sylph.shared.domain.repository

import com.google.firebase.auth.FirebaseUser
import com.vallem.sylph.shared.domain.model.Result

interface AuthRepository {
    val currentUser: FirebaseUser?
    suspend fun login(email: String, password: String): Result<FirebaseUser>
    suspend fun signup(name: String, email: String, password: String): Result<FirebaseUser>
    fun logout()
}