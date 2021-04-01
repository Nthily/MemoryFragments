package com.example.fragmentsofmemory.Database

import android.graphics.Bitmap
import android.media.Image
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserInfo(
    @PrimaryKey(autoGenerate = true)val uid: Int,
    @ColumnInfo(name = "userName") val userName: String,
)
