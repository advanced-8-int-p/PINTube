package com.example.pintube.domain.usecase

import com.example.pintube.data.repository.local.VideoWithThumbnail
import com.example.pintube.domain.repository.ApiRepository
import com.example.pintube.domain.repository.LocalVideoRepository
import javax.inject.Inject

class GetVideosUseCase @Inject constructor(
    private val apiRepository: ApiRepository,
    private val localVideoRepository: LocalVideoRepository,
) {
    suspend operator fun invoke(idList: List<String>): List<VideoWithThumbnail?> {
        var result = idList.map {
            localVideoRepository.findVideoDetail(it)
        }
        if (result.isEmpty()) apiRepository.getContentDetails(idList)?.forEach {
            localVideoRepository.saveVideos(it)
            result = idList.map {item ->
                localVideoRepository.findVideoDetail(item)
            }
        }
        return result
    }
}