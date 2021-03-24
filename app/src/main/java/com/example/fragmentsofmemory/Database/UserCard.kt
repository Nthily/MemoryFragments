package com.example.fragmentsofmemory.Database

import android.media.Image
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class UserCard(
    @ColumnInfo(name = "content") var content: String,

    @PrimaryKey(autoGenerate = true) var id: Int,
    @ColumnInfo(name = "time") val time: String,

)
