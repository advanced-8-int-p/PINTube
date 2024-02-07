package com.example.pintube.ui.Search

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SearchData(
    val publishedAt: String?,
    val title: String?,
    val thumbnailHigh: String?,
    val channelTitle: String?,
) : Parcelable
