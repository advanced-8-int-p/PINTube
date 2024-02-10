package com.example.pintube.domain.repository

import com.example.pintube.data.local.entity.LocalCommentEntity
import com.example.pintube.domain.entitiy.CommentEntity

interface LocalCommentRepository {
    suspend fun saveComment(item: CommentEntity?): LocalCommentEntity?
    suspend fun findComment(videoId: String): List<LocalCommentEntity>?
}