package com.example.pintube.ui.Search

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pintube.data.repository.local.VideoWithThumbnail
import com.example.pintube.domain.repository.LocalFavoriteRepository
import com.example.pintube.domain.usecase.GetSearchVideosUseCase
import com.example.pintube.utill.convertToDaysAgo
import com.example.pintube.utill.convertViewCount
import dagger.hilt.android.internal.Contexts.getApplication
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

    fun searchVideo(query: String) = viewModelScope.launch(Dispatchers.IO) {
        try {
            val searchResults = getSearchVideosUseCase(query)
            if (searchResults.isNotEmpty()) {
                _noSearchResults.postValue(false) // 검색 결과가 있음을 알림
                _searchDataList.postValue(createSearchDataList(searchResults))
            } else {
                _noSearchResults.postValue(true) // 검색 결과가 없음을 알림
            }
        } catch (e: Exception) {
            Log.e("jj-서치뷰모델 searchVideo", "검색 도중 오류 발생: ${e.message}")
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
                // TODO: 이게 채널 썸네일 맞는지 모르겠음.
                channelThumbnailUri = searchData.thumbnail?.thumbnailHigh,
                videoThumbnailUri = searchData.video?.thumbnailHigh,
                id = searchData.video?.channelId,
                channelId = searchData.video?.id
            )
        }

}
