package com.vallem.sylph.shared.data.mapper

import com.vallem.componentlibrary.domain.model.UserInfo
import com.vallem.sylph.shared.data.entity.UserInfoEntity

fun UserInfoEntity.toDomain() = UserInfo(name, picture)
fun UserInfo.toEntity(id: String) = UserInfoEntity(name, picture, id)