package com.example.fragmentsofmemory.Database

import androidx.room.*
import androidx.room.ForeignKey.CASCADE


@Entity(
    foreignKeys = [ForeignKey(entity = DrawerItems::class,
        parentColumns = ["uid"],
        childColumns = ["categoryID"], onDelete = ForeignKey.SET_NULL)]
)
data class UserCard(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "categoryID") val categoryID:Int?,
    @ColumnInfo(name = "content") val content: String,
    @ColumnInfo(name = "time") val time: String,
)

data class CategoryCardCount(
    val categoryID: Int,
    val count: Int
)