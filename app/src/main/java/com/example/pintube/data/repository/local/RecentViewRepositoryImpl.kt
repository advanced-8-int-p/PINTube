package com.example.pintube.data.repository.local

import com.example.pintube.data.local.dao.RecentViewsDAO
import com.example.pintube.data.local.entity.RecentViewsEntity
import com.example.pintube.domain.repository.RecentViewRepository
import com.example.pintube.utill.convertLocalDateTime
import java.time.LocalDateTime
import javax.inject.Inject

class RecentViewRepositoryImpl @Inject constructor(
    private val recentViewsDAO: RecentViewsDAO,
): RecentViewRepository {
    override suspend fun addRecentView(id: String) {
        recentViewsDAO.insert(
            RecentViewsEntity(
                videoId = id,
                saveDate = LocalDateTime.now().convertLocalDateTime()
            )
        )
    }

    override suspend fun findRecentView(id: String) = recentViewsDAO.findRecentView(id)
}