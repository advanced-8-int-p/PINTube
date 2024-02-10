package com.example.pintube.ui.detailpage

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pintube.data.local.entity.FavoriteEntity
import com.example.pintube.data.local.entity.LocalVideoEntity
import com.example.pintube.domain.repository.ApiRepository
import com.example.pintube.domain.repository.LocalChannelRepository
import com.example.pintube.domain.repository.LocalCommentRepository
import com.example.pintube.domain.repository.LocalVideoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repository: ApiRepository,
    private val localVideoRepository: LocalVideoRepository,
    private val localChannelRepository: LocalChannelRepository,
    private val localCommentRepository: LocalCommentRepository
) : ViewModel() {

    private var _media: MutableLiveData<DetailItemModel> = MutableLiveData()
    val media: LiveData<DetailItemModel> get() = _media

    fun getData(id: String) = viewModelScope.launch(Dispatchers.IO) {
        val result = localVideoRepository.findVideoDetail(id)
        if (result == null) {
            repository.getContentDetails(listOf(id))?.forEach {
                localVideoRepository.saveVideos(it)
            }
        }
        repository.getChannelDetails(listOf(result?.channelId!!))?.forEach {
            localChannelRepository.saveChannel(it)
        }

        Log.d("viewModel", "get data $_media")
        _media.postValue(result.convertToDetailItem())
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
        channelProfile = repository.getChannelDetails(listOf(this.channelId!!))?.get(0)?.thumbnailMedium,
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