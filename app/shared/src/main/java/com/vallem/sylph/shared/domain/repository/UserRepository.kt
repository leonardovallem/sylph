package com.vallem.sylph.shared.domain.repository

import com.vallem.sylph.shared.domain.model.UserDetails
import com.vallem.sylph.shared.domain.model.Result

interface UserRepository {
    suspend fun save(id: String, name: String, picUrl: String?): Result<Unit>
    suspend fun retrieveDetails(userId: String): Result<UserDetails>
}