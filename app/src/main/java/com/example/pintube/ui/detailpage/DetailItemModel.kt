package com.example.pintube.ui.detailpage


data class DetailItemModel(
    //video
    val id: String?,
    val publishedAt: String?,
    val title: String?,
    val description: String?,
    val thumbnailHigh: String?,
    val channelTitle: String?,
    val viewCount: String?,
    val likeCount: String?,
    val commentCount: String?,
    val player: String?,
    //comment
    val videoId: String?,
    val textDisplay: String?,
    val authorDisplayName: String?,
    val authorProfileImageUrl: String?,
    val totalReplyCount: Int?,
    //pin
    var isPinned: Boolean = false
)
