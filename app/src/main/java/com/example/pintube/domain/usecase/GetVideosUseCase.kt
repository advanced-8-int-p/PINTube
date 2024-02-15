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
        val loadIds = mutableListOf<String>()
        idList.map {
            localVideoRepository.findVideoDetail(it).let { item ->
                if (item == null){
                    loadIds.add(it)
                }
            }
        }
        if (loadIds.isNotEmpty()) {
            apiRepository.getContentDetails(loadIds)?.forEach {
                localVideoRepository.saveVideos(it)
            }
        }
        val result = idList.map {item ->
                localVideoRepository.findVideoDetail(item)
            }
        return result
    }
}