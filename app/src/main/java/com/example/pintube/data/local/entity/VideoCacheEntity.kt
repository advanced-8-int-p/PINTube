package com.example.pintube.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class VideoCacheEntity (
    val thumbnail: String?,
    val title: String?,
    val description: String?,
    @ColumnInfo(name = "channel_title")
    val channelTitle: String?,
    @ColumnInfo(name = "channel_subscript")
    val channelSubscript: String?,
    @ColumnInfo(name = "published_date")
    val publishedDate: String?
){
    @PrimaryKey(autoGenerate = true) var key:Int = 0
}