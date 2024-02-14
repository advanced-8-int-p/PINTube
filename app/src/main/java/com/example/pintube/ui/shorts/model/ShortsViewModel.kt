package com.example.pintube.ui.shorts.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pintube.data.repository.local.VideoWithThumbnail
import com.example.pintube.domain.repository.ApiRepository
import com.example.pintube.domain.repository.LocalChannelRepository
import com.example.pintube.domain.repository.LocalCommentRepository
import com.example.pintube.domain.repository.LocalFavoriteRepository
import com.example.pintube.domain.repository.LocalSearchRepository
import com.example.pintube.domain.repository.LocalVideoRepository
import com.example.pintube.domain.usecase.GetCommentsUseCase
import com.example.pintube.domain.usecase.GetShortsUseCase
import com.example.pintube.utill.convertComment
import com.example.pintube.utill.convertViewCount
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShortsViewModel @Inject constructor(
    private val localSearchRepository: LocalSearchRepository,
    private val getShortsUseCase: GetShortsUseCase,
    private val getCommentsUseCase: GetCommentsUseCase,
    private val localFavoriteRepository: LocalFavoriteRepository,
) : ViewModel() {

    private val _videos: MutableLiveData<List<ShortsItem.Item>> = MutableLiveData()

    val videos: LiveData<List<ShortsItem.Item>> get() = _videos

    private val _comments: MutableLiveData<List<CommentsItem.Comments>> = MutableLiveData()
    val comments: LiveData<List<CommentsItem.Comments>> get() = _comments

    fun getShortsVideos() = viewModelScope.launch(Dispatchers.IO) {
        var result = localSearchRepository.findSearchRecord("#쇼츠 #shorts")

        if (result.isNullOrEmpty()) {
            result = getShortsUseCase()
        }
        _videos.postValue(result?.map {
            it.convertShortsItem()
        })
    }

    fun getComments(id: String) = viewModelScope.launch(Dispatchers.IO) {
        val comments = getCommentsUseCase(id)
        _comments.postValue(comments?.map { it.convertComment() })
    }

    fun onClickBookmark(id: String) = viewModelScope.launch(Dispatchers.IO) {
        if (localFavoriteRepository.checkIsBookmark(id)){
            removeBookmark(id)
        } else {
            addBookmark(id)
        }
    }
    private fun addBookmark(id: String) = viewModelScope.launch(Dispatchers.IO) {
        localFavoriteRepository.addBookmark(id)
        _videos.postValue( videos.value?.map {
            it.copy(
                isBookmark = localFavoriteRepository.checkIsBookmark(it.id ?: "")
            )
        })
    }

    private fun removeBookmark(id: String) = viewModelScope.launch(Dispatchers.IO) {
        localFavoriteRepository.deleteBookmark(id)
        _videos.postValue( videos.value?.map {
            it.copy(
                isBookmark = localFavoriteRepository.checkIsBookmark(it.id ?: "")
            )
        })
    }
    private fun VideoWithThumbnail.convertShortsItem() = ShortsItem.Item(
        id = this.video?.id,
        channelId = this.video?.channelId,
        channelTitle = this.video?.channelTitle,
        channelThumbnail = this.thumbnail?.thumbnailMedium,
        title = this.video?.title,
        commentCount = this.video?.commentCount?.convertViewCount(),
        player = this.video?.player
    )

}

