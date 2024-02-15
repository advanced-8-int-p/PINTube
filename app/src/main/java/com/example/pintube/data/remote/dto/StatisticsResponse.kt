package com.example.pintube.data.remote.dto

data class StatisticsResponse (
    val viewCount: String?,
    val likeCount: String?,
    val favoriteCount: String?,
    val commentCount: String?,
    val subscriberCount: String?,
    val hiddenSubscriberCount: Boolean?,
    val videoCount: String?,
)