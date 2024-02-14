package com.example.pintube.ui.Search

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SearchData(
    var title: String? = null,
    var channelName: String? = null,
    var publishedAt: String? = null,
    var channelTitle: String? = null,
    var viewCount: String? = null,
    var channelThumbnailUri: String? = "https://picsum.photos/200/300",
    var videoThumbnailUri: String? = "https://picsum.photos/200/300",
    val id: String? = null,
    val channelId: String? = null,
) : Parcelable

/*
title = null,
channelName = null,
publishedAt = null,
channelTitle = null,
viewCount = null,
channelThumbnailUri = "https://picsum.photos/200/300",
videoThumbnailUri = "https://picsum.photos/200/300",
id = null,
channelId = null,
 */
