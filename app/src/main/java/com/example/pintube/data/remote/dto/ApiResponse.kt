package com.example.pintube.data.remote.dto

data class GenericApiResponse<T>(
    val etag: String?,
    val items: List<T>?,
    val kind: String?,
    val regionCode: String?,
    val nextPageToken: String?,
    val prevPageToken: String?,
    val pageInfo: PageInfo?
)
data class PageInfo(
    val resultsPerPage: Int?,
    val totalResults: Int?
)