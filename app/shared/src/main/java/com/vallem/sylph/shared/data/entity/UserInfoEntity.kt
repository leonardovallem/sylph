package com.vallem.sylph.shared.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tb_user_info")
data class UserInfoEntity(
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "encoded_picture") val picture: String?,
    @PrimaryKey val id: String,
)
