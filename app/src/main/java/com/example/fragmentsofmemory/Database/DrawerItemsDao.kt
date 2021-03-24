package com.example.fragmentsofmemory.Database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface DrawerItemsDao {

    @Query("SELECT * FROM draweritems")
    fun getAll(): List<DrawerItems>

    @Insert
    fun insert(note: DrawerItems)

    @Delete
    fun delete(note: DrawerItems)
}