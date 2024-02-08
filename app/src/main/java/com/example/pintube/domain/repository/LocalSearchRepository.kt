package com.example.pintube.domain.repository

import com.example.pintube.data.local.entity.LocalChannelEntity
import com.example.pintube.data.local.entity.LocalCommentEntity
import com.example.pintube.data.local.entity.LocalSearchEntity
import com.example.pintube.data.local.entity.LocalVideoEntity
import com.example.pintube.data.repository.VideoWithThumbnail
import com.example.pintube.domain.entitiy.ChannelEntity
import com.example.pintube.domain.entitiy.CommentEntity
import com.example.pintube.domain.entitiy.SearchEntity
import com.example.pintube.domain.entitiy.VideoEntity

interface LocalSearchRepository {
    suspend fun saveSearchResult(item: SearchEntity, query: String) : LocalSearchEntity?
    suspend fun findSearchRecord(query: String): List<VideoWithThumbnail>?
}