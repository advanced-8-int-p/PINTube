package com.example.pintube.data.model.dto

data class ItemDTO(
    val etag: String?,
    val id: idDTO?,
    val kind: String?,
    val snippet: Snippet?
)

data class Snippet(
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
    val thumbnails: ThumbnailsDTO?,
    val title: String?
)
data class Localized(
    val description: String?,
    val title: String?
)