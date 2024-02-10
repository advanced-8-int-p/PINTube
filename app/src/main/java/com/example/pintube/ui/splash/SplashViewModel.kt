package com.example.pintube.ui.splash

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pintube.domain.repository.ApiRepository
import com.example.pintube.domain.repository.LocalChannelRepository
import com.example.pintube.domain.repository.LocalSearchRepository
import com.example.pintube.domain.repository.LocalVideoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val repository: ApiRepository,
    private val localSearchRepository: LocalSearchRepository,
    private val localVideoRepository: LocalVideoRepository,
    private val localChannelRepository: LocalChannelRepository,
) : ViewModel() {

    private val _isLoading: MutableLiveData<Int> = MutableLiveData(0)
    val isLoading: LiveData<Int> get() = _isLoading

    fun updatePopulars() = viewModelScope.launch(Dispatchers.IO) {
        val channelIds = mutableListOf<String>()
        runCatching {
            var result = localVideoRepository.findPopularVideos()
            if (result?.size == 0) {
                repository.getPopularVideo()?.forEach {
                    localVideoRepository.saveVideos(
                        item = it,
                        isPopular = true
                    )
                    it.channelId?.let { channel -> channelIds.add(channel) }
                }
                repository.getChannelDetails(channelIds)?.forEach {
                    localChannelRepository.saveChannel(it)
                }
                _isLoading.value = _isLoading.value?.plus(1)
            }
        }.onFailure { error ->
            Log.e("HomeViewModel", "Error fetching data: ${error.message}", error)
        }
    }

    fun searchCategory(query: String) = viewModelScope.launch(Dispatchers.IO) {
        val channelIds = mutableListOf<String>()
        val videoIds = mutableListOf<String>()
        var searchResult = localSearchRepository.findSearchRecord(query)
        if (searchResult?.size == 0) {
            repository.searchResult(query)?.forEach { item ->
                localSearchRepository.saveSearchResult(
                    item = item,
                    query = query
                )
                item.id?.let { videoIds.add(it) }
                item.channelId?.let { channelIds.add(it) }
            }
            repository.getContentDetails(videoIds)?.forEach {
                localVideoRepository.saveVideos(it)
            }
            repository.getChannelDetails(channelIds)?.forEach {
                localChannelRepository.saveChannel(it)
            }
            searchResult = localSearchRepository.findSearchRecord(query)
        }
        _isLoading.value = _isLoading.value?.plus(1)
    }
}