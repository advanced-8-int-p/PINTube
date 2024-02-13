package com.example.pintube.domain.usecase

import com.example.pintube.data.repository.local.VideoWithThumbnail
import com.example.pintube.domain.repository.ApiRepository
import com.example.pintube.domain.repository.LocalChannelRepository
import com.example.pintube.domain.repository.LocalVideoRepository
import javax.inject.Inject

class GetPopularVideosUseCase @Inject constructor(
        private val apiRepository: ApiRepository,
        private val localVideoRepository: LocalVideoRepository,
        private val localChannelRepository: LocalChannelRepository,
) {
    suspend operator fun invoke(): List<VideoWithThumbnail> {
        var result = localVideoRepository.findPopularVideos()
        if (result.isNullOrEmpty()) {
            val channelIds = apiRepository.getPopularVideo()?.mapNotNull { video ->
                localVideoRepository.saveVideos(video, true)
                video.channelId
            }.orEmpty()
            apiRepository.getChannelDetails(channelIds)?.forEach { localChannelRepository.saveChannel(it) }
            result = localVideoRepository.findPopularVideos()
        }
        return result ?: emptyList()
    }
}