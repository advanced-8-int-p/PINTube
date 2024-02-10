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

    private var _populars: MutableLiveData<List<VideoItemData>> =
        //MutableLiveData(emptyList())
        MutableLiveData(List(10) { VideoItemData() })  //ddd
    val populars: LiveData<List<VideoItemData>> get() = _populars

    private var _categories: MutableLiveData<List<String>> =
        //MutableLiveData(emptyList())
        MutableLiveData(listOf("박보영", "ytn", "안드로이드 스튜디오", "jazz bgm", "#shorts"))  //ddd
    val categories: LiveData<List<String>> get() = _categories

    private var _categoryVideos: MutableLiveData<List<VideoItemData>> =
        //MutableLiveData(emptyList())
        MutableLiveData(List(10) { VideoItemData() })  //ddd
    val categoryVideos: LiveData<List<VideoItemData>> get() = _categoryVideos

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
            _populars.postValue(result?.map { it.convertVideoItemData() })
        }.onFailure { error ->
            Log.e("HomeViewModel", "Error fetching data: ${error.message}", error)
        }
    }

    fun searchCategory(query: String) = viewModelScope.launch(Dispatchers.IO) {
        val channelIds = mutableListOf<String>()
        val videoIds = mutableListOf<String>()
        var searchResult = localSearchRepository.findSearchRecord(query)
        if (searchResult?.size == 0) {
            repository.searchResult(query)?.forEach { item ->
                localSearchRepository.saveSearchResult(
                    item = item,
                    query = query
                )
                item.id?.let { videoIds.add(it) }
                item.channelId?.let { channelIds.add(it) }
            }
            repository.getContentDetails(videoIds)?.forEach {
                localVideoRepository.saveVideos(it)
            }
            repository.getChannelDetails(channelIds)?.forEach {
                localChannelRepository.saveChannel(it)
            }
            searchResult = localSearchRepository.findSearchRecord(query)
        }
        _categoryVideos.postValue(searchResult?.map { it.convertVideoItemData() })
    }

//    fun addAllToCategories(elements: Collection<String>) {
//        _categories.value = _categories.value!!.toMutableList().apply { addAll(elements) }
//    }

    private fun VideoWithThumbnail.convertVideoItemData() = VideoItemData(
        videoThumbnailUri = this.video?.thumbnailHigh,
        title = this.video?.localizedTitle?: this.video?.title,
        channelThumbnailUri = this.thumbnail?.thumbnailMedium,
        channelName = this.video?.channelTitle,
        views = "· 조회수 " + this.video?.viewCount?.convertViewCount(),
        date = this.video?.publishedAt?.convertToDaysAgo(),
        length = this.video?.duration?.convertDurationTime(),
        id = this.video?.id,
    )
}
