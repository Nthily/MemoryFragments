package com.example.fragmentsofmemory

import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.fragmentsofmemory.Database.DrawerItems
import com.example.fragmentsofmemory.Database.UserCardContentDao
import com.example.fragmentsofmemory.Database.UserInfo
import com.example.fragmentsofmemory.Database.UserInfoDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@Database(entities = [UserInfo::class], version = 1)
abstract class UserInfoDataBase : RoomDatabase() {
    abstract fun getInfo(): UserInfoDao


    private class UserInfoDataBaseCallback(
        private val scope: CoroutineScope
    ): RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    val dao = database.getInfo()
                    dao.insert(UserInfo(0,"Nthily"))
                }
            }
        }
    }

    companion object{
        @Volatile
        private var INSTANCE: UserInfoDataBase? = null

        fun getDatabase(context: Context,
                        scope: CoroutineScope
        ): UserInfoDataBase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    UserInfoDataBase::class.java,
                    "database-userInfo"
                )   .addCallback(UserInfoDataBaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}