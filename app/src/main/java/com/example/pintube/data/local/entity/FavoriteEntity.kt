package com.example.pintube.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true) val categoryId: Long = 0,
    val name: String
)

@Entity(tableName = "favorite_category_cross",
    primaryKeys = ["videoId", "categoryId"])
data class FavoriteCategoryCross(
    val videoId: String,
    val categoryId: Long
)

@Entity(tableName = "favorite_info")
data class FavoriteEntity(
    @ColumnInfo("video_id")
    @PrimaryKey val videoId: String,
    @ColumnInfo("is_bookmark") val isBookmark: Boolean = true
)
