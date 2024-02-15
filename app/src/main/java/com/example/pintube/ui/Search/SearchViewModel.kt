package com.example.pintube.ui.Search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pintube.data.repository.local.VideoWithThumbnail
import com.example.pintube.domain.repository.LocalFavoriteRepository
import com.example.pintube.domain.usecase.GetSearchVideosUseCase
import com.example.pintube.utill.convertViewCount
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val getSearchVideosUseCase: GetSearchVideosUseCase,
    private val localFavoriteRepository: LocalFavoriteRepository,
) : ViewModel() {

    private var _searchDataList: MutableLiveData<List<SearchData>> =
        MutableLiveData(emptyList())

    val searchDataList: LiveData<List<SearchData>> get() = _searchDataList


    private val _noSearchResults: MutableLiveData<Boolean> = MutableLiveData(false)
    val noSearchResults: LiveData<Boolean> get() = _noSearchResults

    private var _currentWord: String? = null
    private var _currentPage: Int = -1

    fun searchVideo(query: String) = viewModelScope.launch(Dispatchers.IO) {
        try {
            _currentWord = query
            _currentPage = 0
            val searchResults = getSearchVideosUseCase(query)
            if (searchResults.isNotEmpty()) {
                _noSearchResults.postValue(false) // 검색 결과가 있음을 알림
                _searchDataList.postValue(createSearchDataList(searchResults))
            } else {
                _noSearchResults.postValue(true) // 검색 결과가 없음을 알림
            }
        } catch (e: Exception) {
            Log.e("jj-서치뷰모델 searchVideo", "검색 도중 오류 발생: ${e.message}")
            _currentWord = null
            _currentPage = -1
        }
    }


    private fun createSearchDataList(searchResults: List<VideoWithThumbnail>) =
        searchResults.map { searchData ->
            SearchData(
                title = searchData.video?.localizedTitle,
                channelName = searchData.video?.channelTitle,
                publishedAt = searchData.video?.publishedAt,
                channelTitle = searchData.video?.channelTitle,
                viewCount = searchData.video?.viewCount?.convertViewCount(),
                channelThumbnailUri = searchData.thumbnail?.thumbnailHigh,
                videoThumbnailUri = searchData.video?.thumbnailHigh,
                id = searchData.video?.channelId,
                channelId = searchData.video?.id
            )
        }

    fun nextSearch() = viewModelScope.launch(Dispatchers.IO) {
        try {
            Log.d(
                "jj-서치뷰모델 nextSearch",
                "currentWord: $_currentWord, currentPage: $_currentPage"
                        + '\n' +
                        "searchDataList.value!!.size: ${searchDataList.value!!.size}"
            )
            if (_currentWord.isNullOrBlank()) {
                Log.e("jj-서치뷰모델 nextSearch", "_currentWord.isNullOrBlank: $_currentWord")
                return@launch
            }
            val searchResult =
                getSearchVideosUseCase.invoke(_currentWord!!, (++_currentPage).toString())
            Log.d(
                "jj-서치뷰모델 nextSearch",
                "첫제목: ${searchResult.firstOrNull()?.video?.title}"
            )
            _searchDataList.postValue(
                _searchDataList.value!!.toMutableList()
                    .apply { addAll(createSearchDataList(searchResult)) }
            )

        } catch (error: Exception) {
            Log.e(
                "SearchViewModel",
                "Error searching category: $_currentWord, ${error.message}",
                error
            )
        }
    }

}
