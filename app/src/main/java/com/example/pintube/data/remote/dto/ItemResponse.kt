package com.example.pintube.data.remote.dto

data class ItemResponse(
    val etag: String?,
    val id: IdResponse?,
    val kind: String?,
    val snippet: SnippetResponse?,
    val contentDetails: ContentDetailsResponse?,
    val statics: StatisticsResponse?,
    val player: PlayerResponse?,
    val topicDetails: TopicDetailsResponse?,
    val brandingSettings: BrandingSettingsResponse?,
    val replies: Replies?,
)

data class Replies (
    val comments: List<ItemResponse>?,
)
