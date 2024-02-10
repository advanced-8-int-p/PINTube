package com.example.pintube.data.repository.local

import com.example.pintube.data.local.dao.CommentDAO
import com.example.pintube.data.local.entity.LocalCommentEntity
import com.example.pintube.domain.entitiy.CommentEntity
import com.example.pintube.domain.repository.LocalCommentRepository
import com.example.pintube.utill.convertLocalDateTime
import java.time.LocalDateTime
import javax.inject.Inject

class LocalCommentRepositoryImpl @Inject constructor(
    private val commentDAO: CommentDAO,
) : LocalCommentRepository {
    override suspend fun saveComment(
        item: CommentEntity?,
    ): LocalCommentEntity? = item?.convertToLocalCommentEntity().apply {
        this?.let {
            commentDAO.insert(it)
        }
    }

    override suspend fun findComment(
        videoId: String,
    ): List<LocalCommentEntity>? = commentDAO.findComment(
        videoId,
        LocalDateTime.now().minusHours(2).convertLocalDateTime()
    )

    override suspend fun findReplies(
        parentId: String
    ): List<LocalCommentEntity>? = commentDAO.findReplies(parentId)

    private fun CommentEntity.convertToLocalCommentEntity(): LocalCommentEntity? {
        if (this.videoId != null) {
            return LocalCommentEntity(
                    id = this.id,
                    channelId = this.channelId,
                    videoId = this.videoId,
                    textDisplay = this.textDisplay,
                    textOriginal = this.textOriginal,
                    authorDisplayName = this.authorDisplayName,
                    authorProfileImageUrl = this.authorProfileImageUrl,
                    authorChannelUrl = this.authorChannelUrl,
                    authorChannelId = this.authorChannelId,
                    canRate = this.canRate,
                    viewerRating = this.viewerRating,
                    likeCount = this.likeCount,
                    publishedAt = this.publishedAt,
                    updatedAt = this.updatedAt,
                    totalReplyCount = this.totalReplyCount,
                    parentId = this.parentId,
                    saveDate = LocalDateTime.now().convertLocalDateTime(),
                )
        } else return null
    }
}