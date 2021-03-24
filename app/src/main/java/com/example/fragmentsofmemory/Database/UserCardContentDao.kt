package com.example.fragmentsofmemory.Database


import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.fragmentsofmemory.Database.UserCard


@Dao
interface UserCardContentDao {

    @Query("SELECT * FROM usercard")
    fun getAll(): LiveData<List<UserCard>>

    @Insert
    fun insert(note: UserCard)

    @Query("DELETE FROM usercard WHERE id = :idx")
    fun delete(idx: Int)

}