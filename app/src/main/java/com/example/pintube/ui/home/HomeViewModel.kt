package com.example.pintube.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pintube.data.repository.local.VideoWithThumbnail
import com.example.pintube.domain.repository.ApiRepository
import com.example.pintube.domain.repository.LocalChannelRepository
import com.example.pintube.domain.repository.LocalSearchRepository
import com.example.pintube.domain.repository.LocalVideoRepository
import com.example.pintube.domain.repository.CategoryPrefRepository
import com.example.pintube.domain.repository.PageTokenPrefRepository
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
    private val categoryPrefRepository: CategoryPrefRepository,
    private val pageTokenPrefRepository: PageTokenPrefRepository,
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

    init {
        updatePopulars()
        categories.value?.let { searchCategory(it.first()) }
    }

    private fun updatePopulars() = viewModelScope.launch(Dispatchers.IO) {
        try {
            val result = getPopularVideos()
            _populars.postValue(
                result.map {
                    it.convertVideoItemData()
                })
        } catch (error: Exception) {
            Log.e("HomeViewModel", "Error popular videos: ${error.message}", error)
        }
    }

    fun searchCategory(query: String) = viewModelScope.launch(Dispatchers.IO) {
        try {
            val searchResult = getCategoryVideos(query)
            _categoryVideos.postValue(
                searchResult.map {
                    it.convertVideoItemData()
                })
        } catch (error: Exception) {
            Log.e("HomeViewModel", "Error searching category: $query, ${error.message}", error)
        }
    }

    private suspend fun getPopularVideos(): List<VideoWithThumbnail> {
        var result = localVideoRepository.findPopularVideos()
        if (result.isNullOrEmpty()) {
            val channelIds = repository.getPopularVideo()?.mapNotNull { video ->
                localVideoRepository.saveVideos(video, true)
                video.channelId
            }.orEmpty()
            repository.getChannelDetails(channelIds)?.forEach { localChannelRepository.saveChannel(it) }
            result = localVideoRepository.findPopularVideos()
        }
        return result ?: emptyList()
    }

    private suspend fun getCategoryVideos(query: String): List<VideoWithThumbnail> {
        val page = (_categoryVideos.value?.size?.div(50)?: 0).toString()
        var searchResult = localSearchRepository.findSearchRecord(query)
        if (searchResult.isNullOrEmpty()) {
            val videoIds = mutableListOf<String>()
            val channelIds = mutableListOf<String>()
            val token = pageTokenPrefRepository.getPageToken(query,page)

            repository.searchResult(query = query, pageToken = token)?.forEach { item ->
                localSearchRepository.saveSearchResult(item, query)
                item.id?.let { videoIds.add(it) }
                item.channelId?.let { channelIds.add(it) }
            }

            repository.getContentDetails(videoIds)?.forEach {
                localVideoRepository.saveVideos(it)
            }
            repository.getChannelDetails(channelIds)?.forEach {
                localChannelRepository.saveChannel(it)
            }
            pageTokenPrefRepository.saveNextPageToken(
                query = query,
                page = page,
                token = repository.getNextPageToken()
            )

            searchResult = localSearchRepository.findSearchRecord(query)
        }
        return searchResult ?: emptyList()
    }

//    fun addAllToCategories(elements: Collection<String>) {
//        _categories.value = _categories.value!!.toMutableList().apply { addAll(elements) }
//    }

    private fun VideoWithThumbnail.convertVideoItemData() = VideoItemData(
        videoThumbnailUri = this.video?.thumbnailHigh,
        title = this.video?.localizedTitle?: this.video?.title,
        channelThumbnailUri = this.thumbnail?.thumbnailMedium,
        channelName = this.video?.channelTitle,
        views = " 조회수 " + this.video?.viewCount?.convertViewCount(),
        date = this.video?.publishedAt?.convertToDaysAgo(),
        length = this.video?.duration?.convertDurationTime(),
        id = this.video?.id,
    )
}
