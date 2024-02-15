package com.example.pintube.domain.repository

import com.example.pintube.data.local.entity.LocalSearchEntity
import com.example.pintube.data.repository.local.VideoWithThumbnail
import com.example.pintube.domain.entitiy.SearchEntity

interface LocalSearchRepository {
    suspend fun saveSearchResult(item: SearchEntity, query: String)
    suspend fun findSearchRecord(query: String): List<VideoWithThumbnail>?
}