package com.example.pintube.ui.detailpage

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pintube.data.local.entity.LocalVideoEntity
import com.example.pintube.data.repository.local.VideoWithThumbnail
import com.example.pintube.domain.repository.LocalChannelRepository
import com.example.pintube.domain.repository.LocalFavoriteRepository
import com.example.pintube.domain.repository.LocalVideoRepository
import com.example.pintube.domain.repository.RecentViewRepository
import com.example.pintube.domain.usecase.GetCommentsUseCase
import com.example.pintube.utill.convertDetailComment
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val localVideoRepository: LocalVideoRepository,
    private val localChannelRepository: LocalChannelRepository,
    private val recentViewRepository: RecentViewRepository,
    private val getCommentsUseCase: GetCommentsUseCase,
    private val localFavoriteRepository: LocalFavoriteRepository,
) : ViewModel() {

    private var _media: MutableLiveData<DetailItemData> = MutableLiveData()
    val media: LiveData<DetailItemData> get() = _media

    private var _comments: MutableLiveData<List<DetailCommentsItem.Comments>> = MutableLiveData()
    val comments: LiveData<List<DetailCommentsItem.Comments>> get() = _comments

    val videoId: String
        get() = try{
            savedStateHandle["video_id"]!!
        } catch (error: Exception) {
            Log.e("DetailViewModel", "Error Null ID: ${error.message}", error)
            ""
        }

    init {
        getData()
        viewModelScope.launch(Dispatchers.IO) {
            recentViewRepository.addRecentView(videoId)
        }
    }

    fun initBookmark() = viewModelScope.launch(Dispatchers.IO) {
        _media.postValue(
            media.value?.copy(
                isPinned = localFavoriteRepository.checkIsBookmark(videoId).not()
            )
        )
    }

    fun onClickBookmark(id: String) = viewModelScope.launch(Dispatchers.IO) {
        if (localFavoriteRepository.checkIsBookmark(id)){
            removeBookmark(id)
        } else {
            addBookmark(id)
        }
        initBookmark()
    }
    private fun addBookmark(id: String) = viewModelScope.launch(Dispatchers.IO) {
        localFavoriteRepository.addBookmark(id)
    }

    private fun removeBookmark(id: String) = viewModelScope.launch(Dispatchers.IO) {
        localFavoriteRepository.deleteBookmark(id)
    }
    private fun getData() = viewModelScope.launch(Dispatchers.IO) {
        val result = videoId.let { localVideoRepository.findVideoDetail(it) }
        result?.let { it.video?.id?.let { it1 -> getComment(id = it1) } }

        _media.postValue(result?.convertToDetailItem())
    }

    private fun getComment(id: String) = viewModelScope.launch(Dispatchers.IO) {
        runCatching {
            val results = getCommentsUseCase(id)
            _comments.postValue(results?.map { it.convertDetailComment() })
        }.onFailure {
            Log.e("DetailViewModel getComment", "Error fetching data: $it")
        }
    }

    private suspend fun VideoWithThumbnail.convertToDetailItem() = DetailItemData(
        id = this.video?.id,
        publishedAt = this.video?.publishedAt,
        title = this.video?.title,
        description = this.video?.description,
        thumbnailHigh = this.video?.thumbnailHigh,
        channelTitle = this.video?.channelTitle,
        viewCount = this.video?.viewCount,
        likeCount = this.video?.likeCount,
        commentCount = this.video?.commentCount,
        player = this.video?.player,
        channelProfile = this.thumbnail?.thumbnailMedium,
        isPinned = localFavoriteRepository.checkIsBookmark(this.video?.id ?: "")
    )


}