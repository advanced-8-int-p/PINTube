package com.example.pintube.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "search_info")
data class LocalSearchEntity(
    val query: String,
    val id: String?,
    val publishedAt: String?,
    val channelId: String?,
    val title: String?,
    val description: String?,
    val thumbnailHigh: String?,
    val thumbnailMedium: String?,
    val thumbnailLow: String?,
    val channelTitle: String?,
    val liveBroadcastContent: String?,
){
    @PrimaryKey(autoGenerate = true) var key:Int = 0
}
