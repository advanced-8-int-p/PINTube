package com.example.pintube.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "recent_view")
data class RecentViewsEntity (
    @PrimaryKey
    @ColumnInfo("video_id")
    val videoId: String,
    @ColumnInfo("save_date")
    val saveDate: String,
)