package com.example.pintube.ui.detailpage

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pintube.data.local.entity.LocalVideoEntity
import com.example.pintube.domain.repository.ApiRepository
import com.example.pintube.domain.repository.LocalChannelRepository
import com.example.pintube.domain.repository.LocalCommentRepository
import com.example.pintube.domain.repository.LocalVideoRepository
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
    private val getCommentsUseCase: GetCommentsUseCase,
) : ViewModel() {

    private var _media: MutableLiveData<DetailItemModel> = MutableLiveData()
    val media: LiveData<DetailItemModel> get() = _media

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
    }

    private fun getData() = viewModelScope.launch(Dispatchers.IO) {
        val result = videoId.let { localVideoRepository.findVideoDetail(it) }
        result?.let { getComment(id = it.id) }

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

    private suspend fun LocalVideoEntity.convertToDetailItem() = DetailItemModel(
        id = this.id,
        publishedAt = this.publishedAt,
        title = this.title,
        description = this.description,
        thumbnailHigh = this.thumbnailHigh,
        channelTitle = this.channelTitle,
        viewCount = this.viewCount,
        likeCount = this.likeCount,
        commentCount = this.commentCount,
        player = this.player,
        channelProfile = this.channelId?.let { localChannelRepository.findChannel(it)?.thumbnailMedium },
        isPinned = isPinned()
    )

    private fun isPinned():Boolean {
        //FavoriteEntity?
//        if () {
//            return true
//        } else
            return false
    }

}