package com.example.pintube.data.model.dto

data class VideoDTO(
    val etag: String?,
    val items: List<ItemDTO>?,
    val kind: String?,
    val nextPageToken: String?,
    val pageInfo: PageInfo?
)

data class SearchDTO(
    val etag: String?,
    val items: List<ItemDTO>?,
    val kind: String?,
    val regionCode: String?,
    val nextPageToken: String?,
    val pageInfo: PageInfo?
)
data class PageInfo(
    val resultsPerPage: Int?,
    val totalResults: Int?
)