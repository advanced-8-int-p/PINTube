package com.example.pintube.ui.Search

import androidx.lifecycle.ViewModel
import com.example.pintube.domain.repository.LocalFavoriteRepository
import com.example.pintube.domain.usecase.GetPopularVideosUseCase
import com.example.pintube.domain.usecase.GetSearchVideosUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchViewModel@Inject constructor(
    private val getPopularVideosUseCase: GetPopularVideosUseCase,
    private val getCategoryVideos: GetSearchVideosUseCase,
    private val localFavoriteRepository: LocalFavoriteRepository,
) : ViewModel() {

}

