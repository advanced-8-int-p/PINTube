package com.example.pintube.data.repository.local

import com.example.pintube.data.local.dao.ChannelDAO
import com.example.pintube.data.local.dao.SearchDAO
import com.example.pintube.data.local.dao.VideoDAO
import com.example.pintube.data.local.entity.LocalSearchEntity
import com.example.pintube.domain.entitiy.SearchEntity
import com.example.pintube.domain.repository.LocalSearchRepository
import com.example.pintube.utill.convertLocalDateTime
import java.time.LocalDateTime
import javax.inject.Inject

class LocalSearchRepositoryImpl @Inject constructor(
    private val searchDAO: SearchDAO,
    private val videoDAO: VideoDAO,
    private val channelDAO: ChannelDAO,
): LocalSearchRepository {
    override suspend fun saveSearchResult(
        item: SearchEntity,
        query: String
    ): LocalSearchEntity? = item.convertToLocalSearchEntity(query).apply {
        this?.let { searchDAO.insert(it) }
    }

    override suspend fun findSearchRecord(
        query: String,
    ): List<VideoWithThumbnail>? = searchDAO.findSearchRecord(
        query,
        LocalDateTime.now().minusHours(12).convertLocalDateTime()
        )?.map {
        VideoWithThumbnail(
            video = videoDAO.findVideo(it.id),
            thumbnail = channelDAO.getChannelThumbnail(it.channelId)
        )
    }

    private fun SearchEntity.convertToLocalSearchEntity(query: String): LocalSearchEntity? {
        if (this.id != null) {
            return LocalSearchEntity(
                id = this.id,
                publishedAt = this.publishedAt,
                channelId = this.channelId,
                title = this.title,
                description = this.description,
                localizedTitle = this.localizedTitle,
                localizedDescription = this.localizedDescription,
                thumbnailHigh = this.thumbnailHigh,
                thumbnailMedium = this.thumbnailMedium,
                thumbnailLow = this.thumbnailLow,
                channelTitle = this.channelTitle,
                liveBroadcastContent = this.liveBroadcastContent,
                query = query,
                saveDate = LocalDateTime.now().convertLocalDateTime(),
            )
        } else return null
    }
}