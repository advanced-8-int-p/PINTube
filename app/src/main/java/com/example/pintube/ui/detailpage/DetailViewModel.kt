package com.example.pintube.ui.detailpage

import androidx.lifecycle.ViewModel
import com.example.pintube.data.local.dao.ChannelDAO
import com.example.pintube.data.local.dao.CommentDAO
import com.example.pintube.data.local.dao.VideoDAO
import com.example.pintube.domain.repository.ApiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repository: ApiRepository,
) : ViewModel() {

}