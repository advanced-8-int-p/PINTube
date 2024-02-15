package com.example.pintube.ui.mypage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pintube.data.repository.local.VideoWithThumbnail
import com.example.pintube.domain.repository.LocalFavoriteRepository
import com.example.pintube.domain.repository.RecentViewRepository
import com.example.pintube.domain.usecase.GetVideosUseCase
import com.example.pintube.ui.mypage.viewtype.MypageVideoItem
import com.example.pintube.ui.mypage.viewtype.MypageViewType
import com.example.pintube.utill.convertDurationTime
import com.example.pintube.utill.convertToDaysAgo
import com.example.pintube.utill.convertViewCount
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MypageViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getVideosUseCase: GetVideosUseCase,
    private val recentViewRepository: RecentViewRepository,
    private val favoriteRepository: LocalFavoriteRepository,
) : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is notifications Fragment"
    }
    val text: LiveData<String> = _text

    private val _recentView: MutableLiveData<List<MypageVideoItem?>?> = MutableLiveData()
    val recentView: LiveData<List<MypageVideoItem?>?> get() = _recentView

    private val _favorite: MutableLiveData<List<MypageVideoItem?>?> = MutableLiveData()
    val favorite: LiveData<List<MypageVideoItem?>?> get() = _favorite

    private val _currentUser: MutableLiveData<MypageViewType.MyPageProfile> = MutableLiveData()
    val currentUser: LiveData<MypageViewType.MyPageProfile> get() = _currentUser


    init {
        getRecentView()
        getFavorite()
    }

    fun getRecentView() = viewModelScope.launch(Dispatchers.IO) {
        val ids = recentViewRepository.findRecentView()?.map { it.videoId }
        _recentView.postValue(ids?.let { getVideosUseCase(it) }?.map { it?.convertRecentItemData() })
    }

    fun getFavorite() = viewModelScope.launch(Dispatchers.IO) {
        val ids = favoriteRepository.findCategoryVideos()
        _favorite.postValue(ids?.let {
            getVideosUseCase(it)
        }?.map {
            it?.convertRecentItemData()
        })
    }

    fun getCurrentUser(user: MypageViewType.MyPageProfile) {
        _currentUser.value = user
    }

    private fun VideoWithThumbnail.convertRecentItemData() = MypageVideoItem(
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

