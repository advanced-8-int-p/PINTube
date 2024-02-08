package com.example.pintube.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pintube.data.local.dao.ChannelDAO
import com.example.pintube.data.local.dao.CommentDAO
import com.example.pintube.data.local.dao.VideoDAO
import com.example.pintube.domain.repository.ApiRepository
import com.example.pintube.domain.usecase.convertDurationTime
import com.example.pintube.domain.usecase.convertToDaysAgo
import com.example.pintube.domain.usecase.convertViewCount
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: ApiRepository,
    private val videoDao: VideoDAO,
    private val commentDao: CommentDAO,
    private val channelDao: ChannelDAO,
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

        val channelIdSet = videoEntities.mapTo(mutableSetOf()) { it.channelId!! }  // not-null 이겠지?
        val channelEntities = repository.getChannelDetails(channelIdSet.toList())
            ?: error("jj-홈뷰모델 - updatePopulars - getChannelDetails == null")
        val channelThumbnailMap = channelEntities.associate { Pair(it.id!!, it.thumbnailMedium) }

        val videoItemDatas = videoEntities.map {
            VideoItemData(
                videoThumbnailUri = it.thumbnailHigh,
                channelThumbnailUri = channelThumbnailMap[it.channelId],
                title = it.title,
                channelName = it.channelTitle,
                views = it.viewCount?.convertViewCount(),
                date = it.publishedAt?.convertToDaysAgo(),
                length = it.duration?.convertDurationTime(),
                isSaved = null,
                id = it.id,
            )
        }
        _populars.postValue(videoItemDatas)
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
                views = videoEntityMap[it.id]?.viewCount?.convertViewCount(),
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

}
