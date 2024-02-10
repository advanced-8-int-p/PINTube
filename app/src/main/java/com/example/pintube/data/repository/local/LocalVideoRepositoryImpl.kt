package com.example.pintube.data.repository.local

import com.example.pintube.data.local.dao.ChannelDAO
import com.example.pintube.data.local.dao.ChannelThumbnail
import com.example.pintube.data.local.dao.VideoDAO
import com.example.pintube.data.local.entity.LocalVideoEntity
import com.example.pintube.domain.entitiy.VideoEntity
import com.example.pintube.domain.repository.LocalVideoRepository
import com.example.pintube.utill.convertLocalDateTime
import java.time.LocalDateTime
import javax.inject.Inject

class LocalVideoRepositoryImpl @Inject constructor(
    private val videoDAO: VideoDAO,
    private val channelDAO: ChannelDAO,
) : LocalVideoRepository {
    override suspend fun saveVideos(
        item: VideoEntity,
        isPopular: Boolean?,
    ): LocalVideoEntity? = item.convertToLocalVideoEntity(isPopular).apply {
        this?.let {
            videoDAO.insert(it)
        }
    }

    override suspend fun findVideoDetail(
        videoId: String,
    ): LocalVideoEntity? = videoDAO.findVideo(videoId)

    override suspend fun findPopularVideos()
            : List<VideoWithThumbnail>? =
        videoDAO.findPopularVideos(
            LocalDateTime.now().minusHours(12).convertLocalDateTime()
        )?.map { video ->
            VideoWithThumbnail(
                video = video,
                thumbnail = channelDAO.getChannelThumbnail(video.channelId)
            )
        }

    private fun VideoEntity.convertToLocalVideoEntity(popular: Boolean?): LocalVideoEntity? {
        if (this.id != null) {
            return LocalVideoEntity(
                id = this.id,
                publishedAt = this.publishedAt,
                channelId = this.channelId,
                title = this.title,
                description = this.description,
                thumbnailHigh = this.thumbnailHigh,
                thumbnailMedium = this.thumbnailMedium,
                thumbnailLow = this.thumbnailLow,
                channelTitle = this.channelTitle,
                tags = this.tags,
                categoryId = this.categoryId,
                liveBroadcastContent = this.liveBroadcastContent,
                defaultLanguage = this.defaultLanguage,
                localizedTitle = this.localizedTitle,
                localizedDescription = this.localizedDescription,
                defaultAudioLanguage = this.defaultAudioLanguage,
                duration = this.duration,
                dimension = this.dimension,
                definition = this.definition,
                caption = this.caption,
                licensedContent = this.licensedContent,
                projection = this.projection,
                viewCount = this.viewCount,
                likeCount = this.likeCount,
                favoriteCount = this.favoriteCount,
                commentCount = this.commentCount,
                player = this.player,
                topicDetails = this.topicDetails,
                saveDate = LocalDateTime.now().convertLocalDateTime(),
                isPopular = popular
            )
        } else return null
    }
}

data class VideoWithThumbnail(
    val video: LocalVideoEntity?,
    val thumbnail: ChannelThumbnail?
)