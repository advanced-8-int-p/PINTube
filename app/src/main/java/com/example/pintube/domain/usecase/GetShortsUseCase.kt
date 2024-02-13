package com.example.pintube.domain.usecase

import com.example.pintube.data.repository.local.VideoWithThumbnail
import com.example.pintube.domain.repository.ApiRepository
import com.example.pintube.domain.repository.LocalChannelRepository
import com.example.pintube.domain.repository.LocalSearchRepository
import com.example.pintube.domain.repository.LocalVideoRepository
import javax.inject.Inject

class GetShortsUseCase @Inject constructor(
    private val apiRepository: ApiRepository,
    private val localSearchRepository: LocalSearchRepository,
    private val localVideoRepository: LocalVideoRepository,
    private val localChannelRepository: LocalChannelRepository,
) {
    suspend operator fun invoke(): List<VideoWithThumbnail>? {
        val channelIds = mutableListOf<String>()
        val videoIds = mutableListOf<String>()

        apiRepository.getRandomShorts()?.forEach { item ->
            item.id?.let { videoIds.add(it) }
            item.channelId?.let { channelIds.add(it) }
            localSearchRepository.saveSearchResult(item, "#쇼츠 #shorts")
        }
        apiRepository.getContentDetails(videoIds)?.forEach { item ->
            localVideoRepository.saveVideos(item)
        }
        apiRepository.getChannelDetails(channelIds)?.forEach { item ->
            localChannelRepository.saveChannel(item)
        }

        return localSearchRepository.findSearchRecord("#쇼츠 #shorts")
    }
}