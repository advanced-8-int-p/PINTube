package com.example.pintube.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pintube.data.repository.VideoWithThumbnail
import com.example.pintube.domain.repository.ApiRepository
import com.example.pintube.domain.repository.LocalChannelRepository
import com.example.pintube.domain.repository.LocalSearchRepository
import com.example.pintube.domain.repository.LocalVideoRepository
import com.example.pintube.utill.convertDurationTime
import com.example.pintube.utill.convertToDaysAgo
import com.example.pintube.utill.convertViewCount
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: ApiRepository,
    private val localSearchRepository: LocalSearchRepository,
    private val localVideoRepository: LocalVideoRepository,
    private val localChannelRepository: LocalChannelRepository,
) : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text

    private var _populars: MutableLiveData<ArrayList<VideoItemData>> = MutableLiveData(ArrayList())
    val populars: LiveData<ArrayList<VideoItemData>> get() = _populars

    private var _categories: MutableLiveData<ArrayList<String>> = MutableLiveData(ArrayList())
    val categories: LiveData<ArrayList<String>> get() = _categories

    // 카테고리 비디오 리스트

    fun updatePopulars() = viewModelScope.launch(Dispatchers.IO) {
        val channelIds = mutableListOf<String>()
        runCatching {
            var result = localVideoRepository.findPopularVideos()
            if (result?.size == 0) {
                repository.getPopularVideo()?.forEach {
                    localVideoRepository.saveVideos(
                        item = it,
                        isPopular = true
                    )
                    it.channelId?.let { channel -> channelIds.add(channel) }
                }
                repository.getChannelDetails(channelIds)?.forEach {
                    localChannelRepository.saveChannel(it)
                }
                result = localVideoRepository.findPopularVideos()
            }
            _populars.postValue(result?.map { it.convertVideoItemData() } as ArrayList<VideoItemData>?)
        }.onFailure { error ->
            Log.e("HomeViewModel", "Error fetching data: ${error.message}", error)
        }
    }

    fun searchResult(query: String) = viewModelScope.launch { repository.searchResult(query) }

    fun dddSearch(query: String) = viewModelScope.launch {
        val videoEntities = repository.searchResult(query)
        val videoItemDatas = videoEntities?.map {
            VideoItemData(
                videoThumbnailUri = it.thumbnailMedium,
                channelThumbnailUri = "https://picsum.photos/200/300",  // 채널 썸네일은 다시 가져와야하는건가
                title = it.title,
                channelName = it.channelTitle,
                views = null,
                date = it.publishedAt,
                length = null,
                isSaved = null,
                id = it.id,
            )
        }
        _populars.postValue(ArrayList(videoItemDatas))
    }

    fun addAllToCategories(elements: Collection<String>) =
        _categories.value!!.addAll(elements.toList())


    private fun VideoWithThumbnail.convertVideoItemData() = VideoItemData(
        videoThumbnailUri = this.video.thumbnailHigh,
        title = this.video.title,
        channelThumbnailUri = this.thumbnail?.thumbnailMedium,
        channelName = this.video.channelTitle,
        views = this.video.viewCount?.convertViewCount(),
        date = this.video.publishedAt?.convertToDaysAgo(),
        length = this.video.duration?.convertDurationTime(),
        id = this.video.id,
    )
}
