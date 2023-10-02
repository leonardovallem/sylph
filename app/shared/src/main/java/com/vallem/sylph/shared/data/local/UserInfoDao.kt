package com.vallem.sylph.shared.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.vallem.sylph.shared.data.entity.UserInfoEntity

@Dao
interface UserInfoDao {
    @Insert
    fun insert(user: UserInfoEntity)

    @Query("select * from tb_user_info where id = :id limit 1")
    fun getUser(id: String): UserInfoEntity?

    @Query("delete from tb_user_info")
    fun deleteAll()
}