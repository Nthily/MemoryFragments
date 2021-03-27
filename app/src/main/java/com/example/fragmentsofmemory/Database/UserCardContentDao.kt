package com.example.fragmentsofmemory.Database


import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.fragmentsofmemory.Database.UserCard


@Dao
interface UserCardContentDao {

    @Query("SELECT * FROM UserCard")
    fun getAll(): LiveData<List<UserCard>>

    @Insert
    fun insert(note: UserCard)

    @Query("DELETE FROM UserCard WHERE id = :idx")
    fun delete(idx: Int)

    @Update
    fun update(note: UserCard)

    @Query("select DrawerItems.uid as uid, count(UserCard.categoryID) as count from DrawerItems left join UserCard on DrawerItems.uid = UserCard.categoryID group by DrawerItems.uid")
    fun searchCardNum(): LiveData<List<Pair<Int, Int>>>

}