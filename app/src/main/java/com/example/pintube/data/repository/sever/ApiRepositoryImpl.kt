package com.example.pintube.data.repository.sever

import com.example.pintube.data.remote.retrofit.YoutubeSearchService
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
    ): List<SearchEntity>? = remoteDataSource
        .searchResult(query = query).items?.map { item ->
            convertSearchEntity(item)
        }

    override suspend fun getRandomShorts(): List<SearchEntity>?  = remoteDataSource
        .searchResult(query = "#shorts", videoDuration = "short").items?.map { item ->
            convertSearchEntity(item)
        }

    override suspend fun getPopularVideo()
            : List<VideoEntity>? = remoteDataSource.getPopularVideo().items?.map { item ->
        convertVideoEntity(item)
    }

    override suspend fun getContentDetails(
        idList: List<String>
    ): List<VideoEntity>? = remoteDataSource.getContentDetails(ids = idList).items?.map { item ->
        convertVideoEntity(item)
    }

    override suspend fun getChannelDetails(
        idList: List<String>
    ): List<ChannelEntity>? = remoteDataSource.getChannelDetails(id = idList).items?.map { item ->
        convertChannelEntity(item)
    }

    override suspend fun getComments(
        videoId: String
    ): List<CommentEntity?>? = remoteDataSource.getComments(videoId = videoId).items?.map { item ->
        convertCommentRepliesEntity(item)
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
            viewCount = item.statistics?.viewCount?: "",
            likeCount = item.statistics?.likeCount?: "",
            favoriteCount = item.statistics?.favoriteCount?: "",
            commentCount = item.statistics?.commentCount?: "",
            player = item.player?.embedHtml?: "",
            topicDetails = item.topicDetails?.topicCategories?.toList()
        )
    }

    private fun convertChannelEntity(
        item: ItemResponse
    ): ChannelEntity = ChannelEntity(
        id = item.id ?: "",
        title = item.brandingSettings?.channel?.title ?: "",
        description = item.brandingSettings?.channel?.description ?: "",
        customUrl = item.snippet?.customUrl ?: "",
        publishedAt = item.snippet?.publishedAt ?: "",
        thumbnailHigh = item.snippet?.thumbnails?.high?.url ?: "",
        thumbnailMedium = item.snippet?.thumbnails?.medium?.url ?: "",
        thumbnailLow = item.snippet?.thumbnails?.default?.url ?: "",
        defaultLanguage = item.snippet?.defaultLanguage ?: "",
        localizedTitle = item.snippet?.localized?.title ?: "",
        localizedDescription = item.snippet?.localized?.description ?: "",
        country = item.snippet?.country ?: "",
        like = item.contentDetails?.relatedPlaylists?.like ?: "",
        uploads = item.contentDetails?.relatedPlaylists?.uploads ?: "",
        viewCount = item.statistics?.viewCount ?: "",
        subscriberCount = item.statistics?.subscriberCount ?: "",
        videoCount = item.statistics?.videoCount ?: "",
        bannerImageUrl = item.brandingSettings?.image?.bannerExternalUrl ?: ""
    )

    private fun convertCommentRepliesEntity(
        item: ItemResponse
    ): CommentEntity? {
        val topLevelComment = item.snippet?.topLevelComment?.let { convertCommentEntity(it) }

        val replies = item.replies?.comments?.map { replyItem ->
            convertCommentEntity(replyItem).copy(parentId = item.id)
        } ?: emptyList()

        return topLevelComment?.copy(replies = replies, totalReplyCount = item.snippet.totalReplyCount)
    }

    private fun convertCommentEntity(
        item: ItemResponse,
        parentId: String? = null
    ): CommentEntity = CommentEntity(
        id = item.id?: "",
        channelId = item.snippet?.channelId ?: "",
        videoId = item.snippet?.videoId ?: "",
        textDisplay = item.snippet?.textDisplay ?: "",
        textOriginal = item.snippet?.textOriginal ?: "",
        authorDisplayName = item.snippet?.authorDisplayName ?: "",
        authorProfileImageUrl = item.snippet?.authorProfileImageUrl ?: "",
        authorChannelId = item.snippet?.authorChannelId?.value ?: "",
        authorChannelUrl = item.snippet?.authorChannelUrl ?: "",
        canRate = item.snippet?.canRate ?: false,
        viewerRating = item.snippet?.viewerRating ?: "",
        likeCount = item.snippet?.likeCount ?: 0,
        publishedAt = item.snippet?.publishedAt ?: "",
        updatedAt = item.snippet?.updatedAt ?: "",
        totalReplyCount = item.snippet?.totalReplyCount ?: 0,
        replies = emptyList(),
        parentId = parentId
    )
}