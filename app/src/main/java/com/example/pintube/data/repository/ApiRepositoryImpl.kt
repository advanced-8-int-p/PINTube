package com.example.pintube.data.repository

import com.example.pintube.data.remote.api.retrofit.YoutubeSearchService
import com.example.pintube.data.remote.dto.ItemResponse
import com.example.pintube.data.remote.dto.SearchItemResponse
import com.example.pintube.domain.entitiy.ChannelEntity
import com.example.pintube.domain.entitiy.CommentEntity
import com.example.pintube.domain.entitiy.SearchEntity
import com.example.pintube.domain.entitiy.VideoEntity
import com.example.pintube.domain.repository.ApiRepository
import javax.inject.Inject

class ApiRepositoryImpl @Inject constructor(
    private val remoteDataSource: YoutubeSearchService,
) : ApiRepository {

    override suspend fun searchResult(
        query: String
    ): List<SearchEntity>? {
        return remoteDataSource.searchResult(query = query).items?.map { item ->
            convertSearchEntity(item)
        }
    }

    override suspend fun getPopularVideo(): List<VideoEntity>? {
        return remoteDataSource.getPopularVideo().items?.map { item ->
            convertVideoEntity(item)
        }
    }

    override suspend fun getContentDetails(idList: List<String>): List<VideoEntity>? {
        return remoteDataSource.getContentDetails(ids = idList).items?.map { item ->
            convertVideoEntity(item)
        }
    }

    override suspend fun getChannelDetails(idList: List<String>): List<ChannelEntity>? {
        return remoteDataSource.getChannelDetails(id = idList).items?.map { item ->
            convertChannelEntity(item)
        }
    }

    override suspend fun getComments(videoId: String): List<CommentEntity>? {
        return remoteDataSource.getComments(videoId = videoId).items?.map { item ->
            convertCommentRepliesEntity(item)
        }
    }

    private fun convertSearchEntity(item: SearchItemResponse): SearchEntity {
        return SearchEntity(
            id = item.id?.videoId?: "",
            publishedAt = item.snippet?.publishedAt?: "",
            channelId = item.snippet?.channelId?: "",
            title = item.snippet?.title?: "",
            description = item.snippet?.description?: "",
            localizedTitle = item.snippet?.localized?.title?: "",
            localizedDescription = item.snippet?.localized?.description?: "",
            thumbnailHigh = item.snippet?.thumbnails?.high?.url?: "",
            thumbnailMedium = item.snippet?.thumbnails?.medium?.url?: "",
            thumbnailLow = item.snippet?.thumbnails?.default?.url?: "",
            channelTitle = item.snippet?.channelTitle?: "",
            liveBroadcastContent = item.snippet?.liveBroadcastContent?: ""
        )
    }
    private fun convertVideoEntity(item: ItemResponse): VideoEntity {
        return VideoEntity(
            id = item.id?: "",
            publishedAt = item.snippet?.publishedAt?: "",
            channelId = item.snippet?.channelId?: "",
            title = item.snippet?.title?: "",
            description = item.snippet?.description?: "",
            thumbnailHigh = item.snippet?.thumbnails?.high?.url?: "",
            thumbnailMedium = item.snippet?.thumbnails?.medium?.url?: "",
            thumbnailLow = item.snippet?.thumbnails?.default?.url?: "",
            channelTitle = item.snippet?.channelTitle?: "",
            tags = item.snippet?.tags?.toList(),
            categoryId = item.snippet?.categoryId?: "",
            liveBroadcastContent = item.snippet?.liveBroadcastContent?: "",
            defaultLanguage = item.snippet?.defaultLanguage?: "",
            localizedTitle = item.snippet?.localized?.title?: "",
            localizedDescription = item.snippet?.localized?.description?: "",
            defaultAudioLanguage = item.snippet?.defaultAudioLanguage?: "",
            duration = item.contentDetails?.duration?: "",
            dimension = item.contentDetails?.dimension?: "",
            definition = item.contentDetails?.definition?: "",
            caption = item.contentDetails?.caption?: "",
            licensedContent = item.contentDetails?.licensedContent,
            projection = item.contentDetails?.projection?: "",
            viewCount = item.statics?.viewCount?: "",
            likeCount = item.statics?.likeCount?: "",
            favoriteCount = item.statics?.favoriteCount?: "",
            commentCount = item.statics?.commentCount?: "",
            player = item.player?.embedHtml?: "",
            topicDetails = item.topicDetails?.topicCategories?.toList()
        )
    }

    private fun convertChannelEntity(item: ItemResponse): ChannelEntity {
        return ChannelEntity(
            id = item.id?: "",
            title = item.brandingSettings?.channel?.title?: "",
            description = item.brandingSettings?.channel?.description?: "",
            customUrl = item.snippet?.customUrl?: "",
            publishedAt = item.snippet?.publishedAt?: "",
            thumbnailHigh = item.snippet?.thumbnails?.high?.url?: "",
            thumbnailMedium = item.snippet?.thumbnails?.medium?.url?: "",
            thumbnailLow = item.snippet?.thumbnails?.default?.url?: "",
            defaultLanguage = item.snippet?.defaultLanguage?: "",
            localizedTitle = item.snippet?.localized?.title?: "",
            localizedDescription = item.snippet?.localized?.description?: "",
            country = item.snippet?.country?: "",
            like = item.contentDetails?.relatedPlaylists?.like?: "",
            uploads = item.contentDetails?.relatedPlaylists?.uploads?: "",
            viewCount = item.statics?.viewCount?: "",
            subscriberCount = item.statics?.subscriberCount?: "",
            videoCount = item.statics?.videoCount?: "",
            bannerImageUrl = item.brandingSettings?.image?.bannerExternalUrl?: ""
        )
    }

    private fun convertCommentRepliesEntity(item: ItemResponse): CommentEntity {
        val comment = convertCommentEntity(item)
        if (item.replies?.comments != null){
            val replies = item.replies.comments.map {
                convertCommentEntity(it)
            }
            comment.replies = replies
        }
        return comment
    }
    private fun convertCommentEntity(item: ItemResponse): CommentEntity {
        return CommentEntity(
            channelId = item.snippet?.topLevelComment?.snippet?.channelId?: "",
            videoId = item.snippet?.topLevelComment?.snippet?.videoId?: "",
            textDisplay = item.snippet?.topLevelComment?.snippet?.textDisplay?: "",
            textOriginal = item.snippet?.topLevelComment?.snippet?.textOriginal?: "",
            authorDisplayName = item.snippet?.topLevelComment?.snippet?.authorDisplayName?: "",
            authorProfileImageUrl = item.snippet?.topLevelComment?.snippet?.authorProfileImageUrl?: "",
            authorChannelId = item.snippet?.topLevelComment?.snippet?.authorChannelId?.value?: "",
            authorChannelUrl =item.snippet?.topLevelComment?.snippet?.authorChannelUrl?: "",
            canRate = item.snippet?.topLevelComment?.snippet?.canRate?: false,
            viewerRating = item.snippet?.topLevelComment?.snippet?.viewerRating?: "",
            likeCount = item.snippet?.topLevelComment?.snippet?.likeCount?: 0,
            publishedAt = item.snippet?.topLevelComment?.snippet?.publishedAt?: "",
            updatedAt = item.snippet?.topLevelComment?.snippet?.updatedAt?: "",
            totalReplyCount = item.snippet?.totalReplyCount?: 0,
        )
    }
}