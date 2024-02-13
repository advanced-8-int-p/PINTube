package com.example.pintube.domain.usecase

import android.util.Log
import com.example.pintube.data.repository.local.VideoWithThumbnail
import com.example.pintube.domain.repository.ApiRepository
import com.example.pintube.domain.repository.LocalChannelRepository
import com.example.pintube.domain.repository.LocalSearchRepository
import com.example.pintube.domain.repository.LocalVideoRepository
import com.example.pintube.domain.repository.PageTokenPrefRepository
import javax.inject.Inject

class GetSearchVideosUseCase @Inject constructor(
    private val apiRepository: ApiRepository,
    private val localSearchRepository: LocalSearchRepository,
    private val localVideoRepository: LocalVideoRepository,
    private val localChannelRepository: LocalChannelRepository,
    private val pageTokenPrefRepository: PageTokenPrefRepository,
) {
    suspend operator fun invoke(query: String, page: String = "0"): List<VideoWithThumbnail> {
        var searchResult = localSearchRepository.findSearchRecord("$query$page")
        Log.d(
            "jj-검색유즈 invoke",
            "$query$page 첫제목: ${searchResult?.firstOrNull()?.video?.title}"
        )
        if (searchResult.isNullOrEmpty()) {
            val videoIds = mutableListOf<String>()
            val channelIds = mutableListOf<String>()
            val token = pageTokenPrefRepository.getPageToken(query, page)
            Log.d("jj-검색유즈 invoke", "$query$page getPageToken: $token")

            apiRepository.searchResult(query = query, pageToken = token)?.forEach { item ->
                localSearchRepository.saveSearchResult(item, "$query$page")
                item.id?.let { videoIds.add(it) }
                item.channelId?.let { channelIds.add(it) }
            }

            apiRepository.getContentDetails(videoIds)?.forEach {
                localVideoRepository.saveVideos(it)
            }
            apiRepository.getChannelDetails(channelIds)?.forEach {
                localChannelRepository.saveChannel(it)
            }
            pageTokenPrefRepository.saveNextPageToken(
                query = query,
                page = (page.toInt() + 1).toString(),
                token = apiRepository.getNextPageToken()
            )
            Log.d(
                "jj-검색유즈 invoke",
                "apiRepository.getNextPageToken: ${apiRepository.getNextPageToken()}"
            )

            searchResult = localSearchRepository.findSearchRecord("$query$page")
        }
        return searchResult ?: emptyList()
    }
}