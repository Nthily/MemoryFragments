package com.example.fragmentsofmemory

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.fragmentsofmemory.Database.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@Database(entities = [UserCard::class, DrawerItems::class, UserInfo::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun notes(): UserCardContentDao
    abstract fun getDrawer(): DrawerItemsDao
    abstract fun getUserInfo(): UserInfoDao

    private class AppDataBaseCallback(
        private val scope: CoroutineScope
    ): RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    val dao = database.notes()
                    val drawer = database.getDrawer()
                    val info = database.getUserInfo()

                    drawer.insert(DrawerItems(0, "Home"))
                    dao.insert(UserCard(0, 1, "Hello World", "2021.3.28"))
                    info.insert(UserInfo(0, "Nthily", 1, "永远相信美好的事情即将发生"))
                }
            }
        }
    }

    companion object{
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context,
                        scope: CoroutineScope
        ): AppDatabase {

            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "App-DataBase"
                )   .addCallback(AppDataBaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}