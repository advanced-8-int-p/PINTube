package com.example.pintube.ui.home

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class VideoItemData(
    var videoThumbnailUri: String? = "https://picsum.photos/200/300",
    var channelThumbnailUri: String? = "https://picsum.photos/200/300",
    var title: String? = null,
    var channelName: String? = null,
    var views: String? = null,
    var date: String? = null,
    var length: String? = null,
    var isSaved: Boolean = false,
    val id: String? = null,
) : Parcelable

/*
videoThumbnailUri = null,
channelThumbnailUri = null,
title = null,
channelName = null,
views = null,
date = null,
length = null,
isSaved = null,
id = null,
 */
