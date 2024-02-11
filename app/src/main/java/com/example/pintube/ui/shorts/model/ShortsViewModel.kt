package com.example.pintube.ui.shorts.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pintube.data.repository.local.VideoWithThumbnail
import com.example.pintube.domain.repository.ApiRepository
import com.example.pintube.domain.repository.LocalChannelRepository
import com.example.pintube.domain.repository.LocalCommentRepository
import com.example.pintube.domain.repository.LocalSearchRepository
import com.example.pintube.domain.repository.LocalVideoRepository
import com.example.pintube.utill.convertComment
import com.example.pintube.utill.convertViewCount
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShortsViewModel @Inject constructor(
    private val apiRepository: ApiRepository,
    private val localSearchRepository: LocalSearchRepository,
    private val localVideoRepository: LocalVideoRepository,
    private val localChannelRepository: LocalChannelRepository,
    private val localCommentRepository: LocalCommentRepository,
) : ViewModel() {

    private val _videos: MutableLiveData<List<ShortsItem.Item>> = MutableLiveData()

    val videos: LiveData<List<ShortsItem.Item>> get() = _videos

    private val _comments: MutableLiveData<List<CommentsItem.Comments>> = MutableLiveData()
    val comments: LiveData<List<CommentsItem.Comments>> get() = _comments

    fun getShortsVideos() = viewModelScope.launch(Dispatchers.IO) {
        var result = localSearchRepository.findSearchRecord("#쇼츠")

        if (result.isNullOrEmpty()) {
            result = getShortsApiResponse()
        }
        _videos.postValue(result?.map {
            it.convertShortsItem()
        })
    }

    fun getComments(id: String) = viewModelScope.launch(Dispatchers.IO) {
        var comments = localCommentRepository.findComment(id)

        if (comments.isNullOrEmpty()) {
            apiRepository.getComments(id)?.forEach { item ->
                    localCommentRepository.saveComment(item)
            }

            comments = localCommentRepository.findComment(id)
        }
        _comments.postValue(comments?.map { it.convertComment() })
    }

    private suspend fun getShortsApiResponse(): List<VideoWithThumbnail>? {
        val channelIds = mutableListOf<String>()
        val videoIds = mutableListOf<String>()

        apiRepository.getRandomShorts()?.forEach { item ->
            item.id?.let { videoIds.add(it) }
            item.channelId?.let { channelIds.add(it) }
            localSearchRepository.saveSearchResult(item, "#shorts")
        }
        apiRepository.getContentDetails(videoIds)?.forEach { item ->
            localVideoRepository.saveVideos(item)
        }
        apiRepository.getChannelDetails(channelIds)?.forEach { item ->
            localChannelRepository.saveChannel(item)
        }

        return localSearchRepository.findSearchRecord("#shorts")
    }

    private fun VideoWithThumbnail.convertShortsItem() = ShortsItem.Item(
        id = this.video?.id,
        channelId = this.video?.channelId,
        channelTitle = this.video?.channelTitle,
        channelThumbnail = this.thumbnail?.thumbnailMedium,
        title = this.video?.title,
        commentCount = this.video?.commentCount?.convertViewCount(),
    )

}

