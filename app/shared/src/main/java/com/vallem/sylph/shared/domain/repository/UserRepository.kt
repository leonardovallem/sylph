package com.vallem.sylph.shared.domain.repository

import com.vallem.componentlibrary.domain.model.UserInfo
import com.vallem.sylph.shared.domain.model.Result
import com.vallem.sylph.shared.domain.model.UserDetails

interface UserRepository {
    suspend fun save(id: String, name: String, picUrl: String?): Result<Unit>
    suspend fun retrieveUserInfo(userId: String): Result<UserInfo>
    suspend fun retrieveDetails(userId: String): Result<UserDetails>
}