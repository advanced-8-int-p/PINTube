package com.example.pintube.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pintube.data.repository.local.VideoWithThumbnail
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
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getPopularVideosUseCase: GetPopularVideosUseCase,
    private val getCategoryVideos: GetSearchVideosUseCase,
    private val localFavoriteRepository: LocalFavoriteRepository,
    private val pageTokenPrefRepository: PageTokenPrefRepository,
) : ViewModel() {
    private data object key {
        const val category = "category"
    }

    private var _populars: MutableLiveData<List<VideoItemData>> =
        MutableLiveData(emptyList())
    val populars: LiveData<List<VideoItemData>> get() = _populars

    private var _categories: MutableLiveData<List<String>> =
        MutableLiveData(emptyList())
    val categories: LiveData<List<String>> get() = _categories

    private var _categoryVideos: MutableLiveData<List<VideoItemData>> =
        MutableLiveData(emptyList())
    val categoryVideos: LiveData<List<VideoItemData>> get() = _categoryVideos

    private var _currentWord: String? = null
    private var _currentPage: Int = -1

    init {
        loadCategories()
        updatePopulars()
        if (categories.value!!.isEmpty().not())
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
//                    Log.d("Local Fa", "${it.video?.id}")
                    it.convertVideoItemData()
                        .copy(isSaved = localFavoriteRepository.checkIsBookmark(it.video?.id ?: ""))
                })
        } catch (error: Exception) {
            Log.e("HomeViewModel", "Error searching category: $query, ${error.message}", error)
        }
    }

    fun categoryNextSearch(query: String) = viewModelScope.launch(Dispatchers.IO) {
        try {
            if (query != _currentWord) {
                _currentWord = query
                _currentPage = 0
            }
            Log.d(
                "jj-홈뷰모델 categoryNextSearch",
                "currentWord: $_currentWord, currentPage: $_currentPage"
                        + '\n' +
                        "categoryVideos.value!!.size: ${categoryVideos.value!!.size}"
            )
            val searchResult = getCategoryVideos.invoke(query, (++_currentPage).toString())
            Log.d(
                "jj-홈뷰모델 categoryNextSearch",
                "첫제목: ${searchResult.firstOrNull()?.video?.title}"
            )
            _categoryVideos.postValue(
                _categoryVideos.value!!.toMutableList()
                    .apply { addAll(searchResult.map { it.convertVideoItemData() }) }
            )

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
        pageTokenPrefRepository.saveCategory(key.category, categories.value!!)
    }

    fun removeFromCategories(category: String) {
        _categories.value = _categories.value!!.toMutableList().apply { remove(category) }
        pageTokenPrefRepository.saveCategory(key.category, categories.value!!)
    }

    fun loadCategories() {
        _categories.value = pageTokenPrefRepository.loadCategory(key.category)
    }

    private fun VideoWithThumbnail.convertVideoItemData() = VideoItemData(
        videoThumbnailUri = this.video?.thumbnailHigh,
        title = this.video?.localizedTitle ?: this.video?.title,
        channelThumbnailUri = this.thumbnail?.thumbnailMedium,
        channelName = this.video?.channelTitle,
        views = " 조회수 " + this.video?.viewCount?.convertViewCount(),
        date = this.video?.publishedAt?.convertToDaysAgo(),
        length = this.video?.duration?.convertDurationTime(),
        id = this.video?.id,
        channelId = this.video?.channelId,
    )
}
