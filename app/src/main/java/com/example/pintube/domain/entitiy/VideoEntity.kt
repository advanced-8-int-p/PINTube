package com.example.pintube.domain.entitiy

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class VideoEntity(
    val id: String?,
    val publishedAt: String?,
    val channelId: String?,
    val title: String?,
    val description: String?,
    val thumbnailHigh: String?,
    val thumbnailMedium: String?,
    val thumbnailLow: String?,
    val channelTitle: String?,
    val tags: List<String>?,
    val categoryId: String?,
    val liveBroadcastContent: String?,
    val defaultLanguage: String?,
    val localizedTitle: String?,
    val localizedDescription: String?,
    val defaultAudioLanguage: String?,
    val duration: String?,
    val dimension: String?,
    val definition: String?,
    val caption: String?,
    val licensedContent: Boolean?,
    val projection: String?,
    val viewCount: String?,
    val likeCount: String?,
    val favoriteCount: String?,
    val commentCount: String?,
    val player: String?,
    val topicDetails: List<String>?,
) : Parcelable
