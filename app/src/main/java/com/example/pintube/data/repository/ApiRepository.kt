package com.example.pintube.data.repository

import com.example.pintube.data.remote.api.retrofit.YouTubeApi
import com.example.pintube.data.remote.dto.ItemResponse
import com.example.pintube.data.repository.entitiy.ChannelEntity
import com.example.pintube.data.repository.entitiy.CommentEntity
import com.example.pintube.data.repository.entitiy.SearchEntity
import com.example.pintube.data.repository.entitiy.VideoEntity

interface ApiRepository {
    suspend fun searchResult(query: String): List<SearchEntity>?
    suspend fun getPopularVideo(): List<VideoEntity>?
    suspend fun getContentDetails(idList: List<String>): List<VideoEntity>?
    suspend fun getChannelDetails(idList: List<String>): List<ChannelEntity>?
    suspend fun getComments(videoId: String): List<CommentEntity>?
}

class ApiRepositoryImpl : ApiRepository {

    override suspend fun searchResult(
        query: String
    ): List<SearchEntity>? {
        return YouTubeApi.youtubeNetwork.searchResult(query = query).items?.map { item ->
            convertSearchEntity(item)
        }
    }

    override suspend fun getPopularVideo(): List<VideoEntity>? {
        return YouTubeApi.youtubeNetwork.getPopularVideo().items?.map { item ->
            convertVideoEntity(item)
        }
    }

    override suspend fun getContentDetails(idList: List<String>): List<VideoEntity>? {
        return YouTubeApi.youtubeNetwork.getContentDetails(ids = idList).items?.map { item ->
            convertVideoEntity(item)
        }
    }

    override suspend fun getChannelDetails(idList: List<String>): List<ChannelEntity>? {
        return YouTubeApi.youtubeNetwork.getChannelDetails(id = idList).items?.map { item ->
            convertChannelEntity(item)
        }
    }

    override suspend fun getComments(videoId: String): List<CommentEntity>? {
        return YouTubeApi.youtubeNetwork.getComments(videoId = videoId).items?.map { item ->
            convertCommentRepliesEntity(item)
        }
    }

    private fun convertSearchEntity(item: ItemResponse): SearchEntity {
        return SearchEntity(
            id = item.id?.videoId?: "",
            publishedAt = item.snippet?.publishedAt?: "",
            channelId = item.snippet?.channelId?: "",
            title = item.snippet?.localized?.title?: "",
            description = item.snippet?.localized?.description?: "",
            thumbnailHigh = item.snippet?.thumbnails?.high?.url?: "",
            thumbnailMedium = item.snippet?.thumbnails?.medium?.url?: "",
            thumbnailLow = item.snippet?.thumbnails?.default?.url?: "",
            channelTitle = item.snippet?.channelTitle?: "",
            liveBroadcastContent = item.snippet?.liveBroadcastContent?: ""
        )
    }
    private fun convertVideoEntity(item: ItemResponse): VideoEntity {
        return VideoEntity(
            id = item.id?.toString(),
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
            id = item.id?.toString(),
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