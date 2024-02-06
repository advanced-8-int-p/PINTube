package com.example.pintube.ui.detailpage

import com.example.pintube.data.repository.entitiy.CommentEntity

data class DetailItemModel(
    val id : String?,
    val publishedAt: String?,
    val title: String?,
    val description: String?,
    val thumbnailHigh: String?,
    val thumbnailMedium: String?,
    val thumbnailLow: String?,
    val channelTitle: String?,
    val viewCount: String?,
    val likeCount: String?,
    val commentCount: String?,
    val player: String?,

    val channelId: String?,
    val videoId: String?,
    val textDisplay: String?,
    val textOriginal: String?,
    val authorDisplayName: String?,
    val authorProfileImageUrl: String?,
    val totalReplyCount: Int?,
    var replies: List<CommentEntity>? = null,
)
