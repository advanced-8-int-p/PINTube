package com.example.pintube.ui.shorts.model

sealed interface ShortsItem {
    data class Item(
        val id: String?,
        val channelId: String?,
        val channelTitle: String?,
        val channelThumbnail: String?,
        val title: String?,
        val commentCount: String?,
        val isBookmark: Boolean = false,
        val player: String?,
    ) : ShortsItem
}