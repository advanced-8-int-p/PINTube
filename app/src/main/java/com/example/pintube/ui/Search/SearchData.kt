package com.example.pintube.ui.Search

import android.os.Parcelable
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import com.example.pintube.data.local.dao.ChannelThumbnail
import com.example.pintube.data.local.entity.LocalVideoEntity
import com.example.pintube.domain.repository.ApiRepository
import com.example.pintube.domain.repository.LocalChannelRepository
import com.example.pintube.domain.repository.LocalSearchRepository
import com.example.pintube.domain.repository.LocalVideoRepository
import kotlinx.android.parcel.Parcelize
import kotlinx.coroutines.launch
import javax.inject.Inject
@Parcelize
data class SearchData (
    var title: String? = null,
    var channelName: String? = null,
    var publishedAt: String? = null,
    var channelTitle: String? = null,
    var viewCount: String? = null,
    var videoThumbnailUri: String? = "https://picsum.photos/200/300",
    var channelThumbnailUri: String? = "https://picsum.photos/200/300",
    val thumbnailHigh: String? = null,
    val video: String? = null,
    val thumbnail: String? = null
) : Parcelable
