package com.example.fragmentsofmemory.Database

import android.graphics.Bitmap
import android.media.Image
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [ForeignKey(entity = DrawerItems::class,
        parentColumns = ["uid"],
        childColumns = ["lastSelected"], onDelete = ForeignKey.SET_NULL
    )]
)
data class UserInfo(
    @PrimaryKey(autoGenerate = true) val uid: Int,
    @ColumnInfo(name = "userName") val userName: String,
    @ColumnInfo(name = "lastSelected") val last: Int?,
    @ColumnInfo(name = "signature") val signature: String
)
