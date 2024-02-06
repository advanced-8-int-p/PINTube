package com.example.pintube.data.remote.dto

data class ContentDetailsResponse(
    val duration: String?,
    val dimension: String?,
    val definition: String?,
    val caption: String?,
    val licensedContent: Boolean?,
    val projection: String?,
    val likes: String?,
    val uploads: String?,
    val relatedPlaylists: RelatedPlaylistsResponse?,
)

data class RelatedPlaylistsResponse (
    val like: String?,
    val uploads: String?,
)