package com.example.fragmentsofmemory.Database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface DrawerItemsDao {

    @Query("SELECT * FROM draweritems")
    fun getAll(): LiveData<List<DrawerItems>>

    @Insert
    fun insert(note: DrawerItems)

    @Query("DELETE FROM DrawerItems WHERE uid = :idx")
    fun delete(idx: Int)


    @Update
    fun update(drawer: DrawerItems)

}