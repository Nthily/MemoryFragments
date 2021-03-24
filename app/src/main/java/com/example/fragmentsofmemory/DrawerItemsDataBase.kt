package com.example.fragmentsofmemory

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.fragmentsofmemory.Database.DrawerItems
import com.example.fragmentsofmemory.Database.DrawerItemsDao



@Database(entities = [DrawerItems::class], version = 1)
abstract class DrawerItemsDataBase : RoomDatabase() {
    abstract fun getItems(): DrawerItemsDao

    companion object{
        @Volatile
        private var INSTANCE: DrawerItemsDataBase? = null

        fun getDatabase(context: Context): DrawerItemsDataBase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DrawerItemsDataBase::class.java,
                    "database-DrawerItems"
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}