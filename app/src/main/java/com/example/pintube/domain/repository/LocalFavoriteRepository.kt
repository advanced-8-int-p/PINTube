package com.example.pintube.domain.repository

interface LocalFavoriteRepository {
    suspend fun checkIsBookmark(videoId: String): Boolean
    suspend fun addBookmark(videoId: String, category: String = "기본")
    suspend fun addCategoryToFavorite(videoId: String, category: String)
    suspend fun findCategoryVideos(category: String): List<String>?
    suspend fun deleteBookmark(videoId: String)
    suspend fun removeCategoryToFavorite(videoId: String, category: String)
}
