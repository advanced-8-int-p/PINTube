package com.example.pintube.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "comment_info")
data class LocalCommentEntity(
    @PrimaryKey
    val id: String,
    val channelId: String?,
    val videoId: String?,
    val textDisplay: String?,
    val textOriginal: String?,
    val authorDisplayName: String?,
    val authorProfileImageUrl: String?,
    val authorChannelUrl: String?,
    val authorChannelId: String?,
    val canRate: Boolean?,
    val viewerRating: String?,
    val likeCount: Int?,
    val publishedAt: String?,
    val updatedAt: String?,
    val totalReplyCount: Int?,
    var replies: List<LocalCommentEntity?> = emptyList(),
    val saveDate: String?,
)
