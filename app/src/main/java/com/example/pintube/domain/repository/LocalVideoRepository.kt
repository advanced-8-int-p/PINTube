package com.example.pintube.domain.repository

import com.example.pintube.data.local.entity.LocalVideoEntity
import com.example.pintube.data.repository.VideoWithThumbnail
import com.example.pintube.domain.entitiy.VideoEntity

interface LocalVideoRepository {

    suspend fun saveVideos(item: VideoEntity, isPopular: Boolean?): LocalVideoEntity?
    suspend fun findVideoDetail(videoId: String): LocalVideoEntity?
    suspend fun findPopularVideos(): List<VideoWithThumbnail>?
}
