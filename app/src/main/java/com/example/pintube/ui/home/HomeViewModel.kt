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
import com.example.pintube.domain.repository.LocalFavoriteRepository
import com.example.pintube.domain.repository.PageTokenPrefRepository
import com.example.pintube.domain.usecase.GetPopularVideosUseCase
import com.example.pintube.domain.usecase.GetSearchVideosUseCase
import com.example.pintube.utill.convertDurationTime
import com.example.pintube.utill.convertToDaysAgo
import com.example.pintube.utill.convertViewCount
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getPopularVideosUseCase: GetPopularVideosUseCase,
    private val getCategoryVideos: GetSearchVideosUseCase,
    private val localFavoriteRepository: LocalFavoriteRepository,
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
            val result = getPopularVideosUseCase()
            _populars.postValue(
                result.map {
                    it.convertVideoItemData()
                        .copy(isSaved = localFavoriteRepository.checkIsBookmark(it.video?.id ?: ""))
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
                    Log.d("Local Fa", "${it.video?.id}")
                    it.convertVideoItemData()
                        .copy(isSaved = localFavoriteRepository.checkIsBookmark(it.video?.id ?: ""))
                })
        } catch (error: Exception) {
            Log.e("HomeViewModel", "Error searching category: $query, ${error.message}", error)
        }
    }

    fun addBookmark(item: VideoItemData) = viewModelScope.launch(Dispatchers.IO) {
        item.id?.let { localFavoriteRepository.addBookmark(it) }
        _populars.postValue(populars.value?.map {
            it.copy(
                isSaved = localFavoriteRepository.checkIsBookmark(it.id ?: "")
            )
        })
    }

    fun removeBookmark(item: VideoItemData) = viewModelScope.launch(Dispatchers.IO) {
        item.id?.let { localFavoriteRepository.deleteBookmark(it) }
        _populars.postValue(populars.value?.map {
            it.copy(
                isSaved = localFavoriteRepository.checkIsBookmark(it.id ?: "")
            )
        })
    }

    fun addToCategories(category: String) {
        _categories.value = _categories.value!!.toMutableList().apply { add(category) }
    }

    fun removeFromCategories(category: String) {
        _categories.value = _categories.value!!.toMutableList().apply { remove(category) }
    }


//    fun addAllToCategories(elements: Collection<String>) {
//        _categories.value = _categories.value!!.toMutableList().apply { addAll(elements) }
//    }

    private fun VideoWithThumbnail.convertVideoItemData() = VideoItemData(
        videoThumbnailUri = this.video?.thumbnailHigh,
        title = this.video?.localizedTitle ?: this.video?.title,
        channelThumbnailUri = this.thumbnail?.thumbnailMedium,
        channelName = this.video?.channelTitle,
        views = " 조회수 " + this.video?.viewCount?.convertViewCount(),
        date = this.video?.publishedAt?.convertToDaysAgo(),
        length = this.video?.duration?.convertDurationTime(),
        id = this.video?.id,
    )
}
