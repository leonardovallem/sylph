package com.vallem.sylph.shared.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.vallem.sylph.shared.data.entity.UserInfoEntity

@Database(entities = [UserInfoEntity::class], version = 1)
abstract class SylphDatabase : RoomDatabase() {
    companion object {
        const val Name = "sylph_db"
    }

    abstract val userInfoDao: UserInfoDao
}