package com.example.pintube.domain.repository

import com.example.pintube.domain.entitiy.ChannelEntity
import com.example.pintube.domain.entitiy.CommentEntity
import com.example.pintube.domain.entitiy.SearchEntity
import com.example.pintube.domain.entitiy.VideoEntity

interface ApiRepository {
    suspend fun searchResult(query: String): List<SearchEntity>?
    suspend fun getPopularVideo(): List<VideoEntity>?
    suspend fun getContentDetails(idList: List<String>): List<VideoEntity>?
    suspend fun getChannelDetails(idList: List<String>): List<ChannelEntity>?
    suspend fun getComments(videoId: String): List<CommentEntity>?
}