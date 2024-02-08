package com.example.pintube.data.repository

import com.example.pintube.data.local.dao.ChannelDAO
import com.example.pintube.data.local.entity.LocalChannelEntity
import com.example.pintube.domain.entitiy.ChannelEntity
import com.example.pintube.domain.repository.LocalChannelRepository
import javax.inject.Inject

class LocalChannelRepositoryImpl @Inject constructor(
    private val channelDAO: ChannelDAO,
) : LocalChannelRepository {

    override suspend fun saveChannel(
        item: ChannelEntity,
    ): LocalChannelEntity? = item.convertToLocalChannelEntity().apply {
        channelDAO.insert(this?: return@apply)
    }

    override suspend fun findChannel(
        channelId: String,
    ): LocalChannelEntity? = channelDAO.findChannel(channelId)
    private fun ChannelEntity.convertToLocalChannelEntity(): LocalChannelEntity? {

        if (this.id == null) return null

        return LocalChannelEntity(
            id = this.id,
            title = this.title,
            description = this.description,
            customUrl = this.customUrl,
            publishedAt = this.publishedAt,
            thumbnailHigh = this.thumbnailHigh,
            thumbnailMedium = this.thumbnailMedium,
            thumbnailLow = this.thumbnailLow,
            defaultLanguage = this.defaultLanguage,
            localizedTitle = this.localizedTitle,
            localizedDescription = this.localizedDescription,
            country = this.country,
            like = this.like,
            uploads = this.uploads,
            viewCount = this.viewCount,
            subscriberCount = this.subscriberCount,
            videoCount = this.videoCount,
            bannerImageUrl = this.bannerImageUrl
        )
    }

}