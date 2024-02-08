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

    private var _categories: MutableLiveData<List<String>> = MutableLiveData(emptyList())
    val categories: LiveData<List<String>> get() = _categories

    private var _categoryVideos: MutableLiveData<List<VideoItemData>> =
        //MutableLiveData(emptyList())
        MutableLiveData(List(10) { VideoItemData() })  //ddd
    val categoryVideos: LiveData<List<VideoItemData>> get() = _categoryVideos

    fun updatePopulars() = viewModelScope.launch {
        val videoEntities = repository.getPopularVideo() ?: return@launch
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

    fun searchCategory(query: String) = viewModelScope.launch {
        val searchEntities = repository.searchResult(query) ?: return@launch

        val channelIdSet = searchEntities.mapTo(mutableSetOf()) { it.channelId!! }  // not-null 이겠지?
        val channelEntities = repository.getChannelDetails(channelIdSet.toList())
            ?: error("jj-홈뷰모델 - searchCategory - getChannelDetails == null")
        val channelThumbnailMap = channelEntities.associate { Pair(it.id!!, it.thumbnailMedium) }

        val videoIdSet = searchEntities.mapTo(mutableSetOf()) { it.id!! }  // not-null 이겠지?
        // getContentDetails 내용이 일부만 들어있음. duration은 있고 viewCount는 없다.
        val videoEntities = repository.getContentDetails(videoIdSet.toList())
            ?: error("jj-홈뷰모델 - searchCategory - getContentDetails == null")
        val videoEntityMap = videoEntities.associateBy { it.id!! }

        val videoItemDatas = searchEntities.map {
            VideoItemData(
                videoThumbnailUri = it.thumbnailMedium,
                channelThumbnailUri = channelThumbnailMap[it.channelId],
                title = it.title,
                channelName = it.channelTitle,
                views = "조회수 ${videoEntityMap[it.id]?.viewCount?.convertViewCount()}",
                date = it.publishedAt?.convertToDaysAgo(),
                length = videoEntityMap[it.id]?.duration?.convertDurationTime(),
                isSaved = null,
                id = it.id,
            )
        }
        _categoryVideos.postValue(videoItemDatas)
    }

    fun addAllToCategories(elements: Collection<String>) {
        _categories.value = _categories.value!!.toMutableList().apply { addAll(elements) }
    }


    private fun VideoWithThumbnail.convertVideoItemData() = VideoItemData(
        videoThumbnailUri = this.video?.thumbnailHigh,
        title = this.video?.title,
        channelThumbnailUri = this.thumbnail?.thumbnailMedium,
        channelName = this.video?.channelTitle,
        views = this.video?.viewCount?.convertViewCount(),
        date = this.video?.publishedAt?.convertToDaysAgo(),
        length = this.video?.duration?.convertDurationTime(),
        id = this.video?.id,
    )
}


/*
* val channelIds = mutableListOf<String>()
        val videoIds = mutableListOf<String>()
        var searchResult = localSearchRepository.findSearchRecord(query)
        if (searchResult == null) {
            repository.searchResult(query)?.forEach { item ->
                localSearchRepository.saveSearchResult(
                    item = item ,
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
* */