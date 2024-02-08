package com.example.pintube.domain.repository

interface LocalRepository {
    suspend fun savePopularVideos()
}