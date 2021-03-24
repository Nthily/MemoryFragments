package com.example.fragmentsofmemory.Database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DrawerItems(
    @PrimaryKey val uid: Int,
    @ColumnInfo(name = "DrawerItems") val drawerItems: String,
    @ColumnInfo(name = "DrawerItemsId") val drawerItemsId: Int
    //   @ColumnInfo(name = "userImg") val userImg: Image
)
