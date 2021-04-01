package com.example.fragmentsofmemory.Database

import android.graphics.Color
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey


@Entity
data class DrawerItems(
    @PrimaryKey(autoGenerate = true) val uid: Int,
    @ColumnInfo(name = "DrawerItems") val drawerItems: String,
)
