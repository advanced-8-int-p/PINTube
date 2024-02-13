package com.example.pintube.domain.repository

import com.example.pintube.data.local.entity.RecentViewsEntity
import com.example.pintube.data.repository.local.VideoWithThumbnail

interface RecentViewRepository {
    suspend fun addRecentView(id: String)
    suspend fun findRecentView(id: String): List<RecentViewsEntity>?
}