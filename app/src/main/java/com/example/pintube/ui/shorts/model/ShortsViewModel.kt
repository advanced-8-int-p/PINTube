package com.example.pintube.ui.shorts.model

import androidx.lifecycle.ViewModel
import com.example.pintube.domain.repository.ApiRepository
import com.example.pintube.domain.repository.LocalChannelRepository
import com.example.pintube.domain.repository.LocalCommentRepository
import com.example.pintube.domain.repository.LocalVideoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ShortsViewModel @Inject constructor(
    private val apiRepository: ApiRepository,
    private val localVideoRepository: LocalVideoRepository,
    private val localChannelRepository: LocalChannelRepository,
    private val localCommentRepository: LocalCommentRepository,
) : ViewModel() {


}