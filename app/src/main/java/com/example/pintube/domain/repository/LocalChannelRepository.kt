package com.example.pintube.domain.repository

import com.example.pintube.data.local.entity.LocalChannelEntity
import com.example.pintube.domain.entitiy.ChannelEntity

interface LocalChannelRepository {
    suspend fun saveChannel(item: ChannelEntity): LocalChannelEntity?

    suspend fun findChannel(channelId: String): LocalChannelEntity?
}
