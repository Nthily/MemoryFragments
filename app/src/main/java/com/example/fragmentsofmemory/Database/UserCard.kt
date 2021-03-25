package com.example.fragmentsofmemory.Database

import android.media.Image
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class UserCard(
    @PrimaryKey(autoGenerate = true) var id: Int,
    @ColumnInfo(name = "content") var content: String,
    @ColumnInfo(name = "time") val time: String,

)
