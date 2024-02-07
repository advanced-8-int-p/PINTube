package com.example.pintube.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pintube.data.local.dao.ChannelDAO
import com.example.pintube.data.local.dao.CommentDAO
import com.example.pintube.data.local.dao.VideoDAO
import com.example.pintube.domain.repository.ApiRepository
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
        val videoEntities = repository.getPopularVideo()
        val videoItemDatas = videoEntities?.map {
            VideoItemData(
                videoThumbnailUri = it.thumbnailMedium,
                channelThumbnailUri = "https://picsum.photos/200/300",  // 채널 썸네일은 다시 가져와야하는건가
                title = it.title,
                channelName = it.channelTitle,
                views = it.viewCount,
                date = it.publishedAt,
                length = it.duration,
                isSaved = null,
                id = it.id,
            )
        }
        _populars.postValue(ArrayList(videoItemDatas))
    }

    fun searchResult(query: String) = viewModelScope.launch { repository.searchResult(query) }

    fun dddSearch(query: String) = viewModelScope.launch {
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
        _populars.postValue(ArrayList(videoItemDatas))
    }

    fun addAllToCategories(elements: Collection<String>) =
        _categories.value!!.addAll(elements.toList())

}
