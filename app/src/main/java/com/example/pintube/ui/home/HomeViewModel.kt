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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: ApiRepository,
    private val videoDao: VideoDAO,
    private val commentDao: CommentDAO,
    private val channelDao: ChannelDAO,
) : ViewModel() {

    private var _populars: MutableLiveData<List<VideoItemData>> = MutableLiveData(emptyList())
    val populars: LiveData<List<VideoItemData>> get() = _populars

    private var _categories: MutableLiveData<List<String>> = MutableLiveData(emptyList())
    val categories: LiveData<List<String>> get() = _categories

    private var _categoryVideos: MutableLiveData<List<VideoItemData>> = MutableLiveData(emptyList())
    val categoryVideos: LiveData<List<VideoItemData>> get() = _categoryVideos

    fun updatePopulars() = viewModelScope.launch(Dispatchers.Default) {
        val videoEntities = repository.getPopularVideo() ?: return@launch
        val videoItemDatas = videoEntities.map {
            VideoItemData(
                videoThumbnailUri = it.thumbnailHigh,
                channelThumbnailUri = null,  // 채널 썸네일은 다시 가져와야하는건가
                title = it.title,
                channelName = it.channelTitle,
                views = it.viewCount?.convertViewCount(),
                date = it.publishedAt?.convertToDaysAgo(),
                length = it.duration?.convertDurationTime(),
                isSaved = null,
                id = it.id,
            )
        }
        _populars.postValue(ArrayList(videoItemDatas))
    }

    fun searchCategory(query: String) = viewModelScope.launch(Dispatchers.Default) {
        val videoEntities = repository.searchResult(query) ?: return@launch
        val videoItemDatas = videoEntities.map {
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
        _categoryVideos.postValue(videoItemDatas)
    }

    fun addAllToCategories(elements: Collection<String>) {
        _categories.value = _categories.value!!.toMutableList().apply { addAll(elements.toList()) }
    }

}
