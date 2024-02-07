package com.example.pintube.data.remote.dto

data class SnippetResponse(
    val videoId: String?,
    val categoryId: String?,
    val channelId: String?,
    val channelTitle: String?,
    val defaultAudioLanguage: String?,
    val defaultLanguage: String?,
    val description: String?,
    val liveBroadcastContent: String?,
    val localized: Localized?,
    val publishedAt: String?,
    val tags: List<String>?,
    val thumbnails: ThumbnailsResponse?,
    val title: String?,
    val customUrl: String?,
    val country: String?,
    val topLevelComment: SearchItemResponse?,
    val textDisplay: String?,
    val textOriginal: String?,
    val authorDisplayName: String?,
    val authorChannelUrl: String?,
    val authorProfileImageUrl: String?,
    val authorChannelId: AuthorChannelId?,
    val canRate: Boolean,
    val viewerRating: String,
    val likeCount: Int,
    val updatedAt: String,
    val canReply: Boolean,
    val totalReplyCount: Int,
    val isPublic: Boolean,
)

data class AuthorChannelId(
    val value: String?,
)

data class Localized(
    val description: String?,
    val title: String?
)