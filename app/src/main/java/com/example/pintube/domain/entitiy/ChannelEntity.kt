package com.example.pintube.domain.entitiy

data class ChannelEntity (
    val id: String?,
    val title: String?,
    val description: String?,
    val customUrl: String?,
    val publishedAt: String?,
    val thumbnailHigh: String?,
    val thumbnailMedium: String?,
    val thumbnailLow: String?,
    val defaultLanguage: String?,
    val localizedTitle: String?,
    val localizedDescription: String?,
    val country: String?,
    val like: String?,
    val uploads: String?,
    val viewCount: String?,
    val subscriberCount: String?,
    val videoCount: String?,
    val bannerImageUrl: String?,
)