package com.example.pintube.domain.entitiy

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SearchEntity(
    val id: String?,
    val publishedAt: String?,
    val channelId: String?,
    val title: String?,
    val description: String?,
    val thumbnailHigh: String?,
    val thumbnailMedium: String?,
    val thumbnailLow: String?,
    val channelTitle: String?,
    val liveBroadcastContent: String?,
) : Parcelable
