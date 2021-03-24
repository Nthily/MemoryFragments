package com.example.fragmentsofmemory

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.fragmentsofmemory.Database.UserCard
import com.example.fragmentsofmemory.Database.UserCardContentDao


@Database(entities = [UserCard::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun notes(): UserCardContentDao

    companion object{
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {

            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "database-cardContent"
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}