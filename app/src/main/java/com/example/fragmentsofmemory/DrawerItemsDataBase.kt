package com.example.fragmentsofmemory

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.fragmentsofmemory.Database.DrawerItems
import com.example.fragmentsofmemory.Database.DrawerItemsDao
import com.example.fragmentsofmemory.Database.UserCard
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@Database(entities = [DrawerItems::class], version = 1)
abstract class DrawerItemsDataBase : RoomDatabase() {
    abstract fun getDao(): DrawerItemsDao



    private class DrawerItemsDataBaseCallback(
        private val scope: CoroutineScope
    ): RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    val dao = database.getDao()
                    dao.insert(DrawerItems(0,"Home"))
                }
            }
        }
    }

    companion object{
        @Volatile
        private var INSTANCE: DrawerItemsDataBase? = null

        fun getDatabase(context: Context,
                        scope: CoroutineScope
        ): DrawerItemsDataBase {

            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DrawerItemsDataBase::class.java,
                    "database-DrawerItems"
                ) //  .addCallback(DrawerItemsDataBaseCallback(scope))
                    .build()

                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}