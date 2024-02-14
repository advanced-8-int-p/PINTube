package com.example.pintube.ui.Search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pintube.data.repository.local.VideoWithThumbnail
import com.example.pintube.domain.repository.LocalFavoriteRepository
import com.example.pintube.domain.usecase.GetSearchVideosUseCase
import com.example.pintube.utill.convertToDaysAgo
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

    fun searchVideo(query: String) = viewModelScope.launch(Dispatchers.IO) {
        val searchResults = getSearchVideosUseCase(query)
        Log.d("jj-서치뷰모델 searchVideo", "첫제목: ${searchResults[0].video?.title}")
        _searchDataList.postValue(createSearchDataList(searchResults))
    }

    private fun createSearchDataList(searchResults: List<VideoWithThumbnail>) =
        searchResults.map { searchData ->
            SearchData(
                title = searchData.video?.localizedTitle,
                channelName = searchData.video?.channelTitle,
                publishedAt = searchData.video?.publishedAt?.convertToDaysAgo(),
                channelTitle = searchData.video?.channelTitle,
                viewCount = searchData.video?.viewCount?.convertViewCount(),
                // TODO: 이게 채널 썸네일 맞는지 모르겠음.
                channelThumbnailUri = searchData.thumbnail?.thumbnailHigh,
                videoThumbnailUri = searchData.video?.thumbnailHigh,
                id = searchData.video?.channelId,
                channelId = searchData.video?.channelId
            )
        }

}
