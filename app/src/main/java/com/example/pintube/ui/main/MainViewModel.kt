package com.example.pintube.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pintube.domain.repository.LocalFavoriteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val localFavoriteRepository: LocalFavoriteRepository,
): ViewModel() {
    private val _isBookmark : MutableLiveData<Boolean> = MutableLiveData()
    val  isBookmark: LiveData<Boolean> get() = _isBookmark

    fun onClickBookmark(id: String) = viewModelScope.launch(Dispatchers.IO) {
        if (localFavoriteRepository.checkIsBookmark(id)){
            removeBookmark(id)
        } else {
            addBookmark(id)
        }
    }
    private fun addBookmark(id: String) = viewModelScope.launch(Dispatchers.IO) {
        localFavoriteRepository.addBookmark(id)
        _isBookmark.postValue( true)
    }

    private fun removeBookmark(id: String) = viewModelScope.launch(Dispatchers.IO) {
        localFavoriteRepository.deleteBookmark(id)
        _isBookmark.postValue( false)
    }

}