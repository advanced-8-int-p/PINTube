package com.example.pintube.ui.detailpage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pintube.data.local.dao.ChannelDAO
import com.example.pintube.data.local.dao.CommentDAO
import com.example.pintube.data.local.dao.VideoDAO
import com.example.pintube.data.remote.api.retrofit.YouTubeApi
import com.example.pintube.data.remote.api.retrofit.YoutubeSearchService
import com.example.pintube.data.repository.ApiRepositoryImpl
import com.example.pintube.domain.repository.ApiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repository: ApiRepository,
) : ViewModel() {

    private val _media: MutableLiveData<DetailItemModel> = MutableLiveData()
    val media: LiveData<DetailItemModel> get() = _media

    fun getData() = viewModelScope.launch(Dispatchers.Default) {
        val client = YoutubeSearchService
        client
    }

}