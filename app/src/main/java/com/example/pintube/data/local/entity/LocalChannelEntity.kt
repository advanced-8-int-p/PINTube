package com.example.pintube.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "channel_info")
data class LocalChannelEntity (
    @PrimaryKey
    val id: String,
    val title: String?,
    val description: String?,
    val customUrl: String?,
    val publishedAt: String?,
    val thumbnailHigh: String?,
    val thumbnailMedium: String?,
    val thumbnailLow: String?,
    val defaultLanguage: String?,
    val localizedTitle: String?,
    val localizedDescription: String?,
    val country: String?,
    val like: String?,
    val uploads: String?,
    val viewCount: String?,
    val subscriberCount: String?,
    val videoCount: String?,
    val bannerImageUrl: String?,
)