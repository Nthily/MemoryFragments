package com.example.fragmentsofmemory

import android.content.Context
import androidx.room.*
import com.example.fragmentsofmemory.Database.UserCardContentDao
import com.example.fragmentsofmemory.Database.UserInfo
import com.example.fragmentsofmemory.Database.UserInfoDao


@Database(entities = [UserInfo::class], version = 1)
abstract class UserInfoDataBase : RoomDatabase() {
    abstract fun getInfo(): UserInfoDao

    companion object{
        @Volatile
        private var INSTANCE: UserInfoDataBase? = null

        fun getDatabase(context: Context): UserInfoDataBase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    UserInfoDataBase::class.java,
                    "database-userInfo"
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}