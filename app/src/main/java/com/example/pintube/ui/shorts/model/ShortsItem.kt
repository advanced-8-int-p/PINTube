package com.example.pintube.ui.shorts.model

sealed interface ShortsItem {
    data class Item(
        val id: String?,
    ) : ShortsItem
}