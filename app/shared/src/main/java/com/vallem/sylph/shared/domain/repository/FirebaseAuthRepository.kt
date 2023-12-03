package com.vallem.sylph.shared.domain.repository

import com.google.firebase.auth.FirebaseUser
import com.vallem.sylph.shared.auth.AuthData
import com.vallem.sylph.shared.domain.model.Result

interface FirebaseAuthRepository {
    val currentUser: FirebaseUser?
    suspend fun auth(data: AuthData): Result<FirebaseUser>
    suspend fun logout()
}
