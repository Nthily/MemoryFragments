package com.example.fragmentsofmemory.Database

import android.media.Image
import androidx.room.*

@Dao
interface UserInfoDao {

    @Query("SELECT * FROM userinfo")
    fun getAll(): List<UserInfo>

    @Insert
    fun insert(note: UserInfo)
}
