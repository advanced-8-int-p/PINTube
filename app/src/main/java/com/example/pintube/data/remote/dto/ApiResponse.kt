package com.example.pintube.data.remote.dto

data class SearchApiResponse(
    val etag: String?,
    val items: List<SearchItemResponse>?,
    val kind: String?,
    val regionCode: String?,
    val nextPageToken: String?,
    val pageInfo: PageInfo?
)

data class ApiResponse(
    val etag: String?,
    val items: List<ItemResponse>?,
    val kind: String?,
    val regionCode: String?,
    val nextPageToken: String?,
    val pageInfo: PageInfo?
)
data class PageInfo(
    val resultsPerPage: Int?,
    val totalResults: Int?
)