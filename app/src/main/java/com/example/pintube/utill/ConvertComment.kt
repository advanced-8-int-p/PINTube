package com.example.pintube.utill

import com.example.pintube.data.local.entity.LocalCommentEntity
import com.example.pintube.ui.detailpage.DetailCommentsItem
import com.example.pintube.ui.shorts.model.CommentsItem

fun LocalCommentEntity.convertComment() : CommentsItem.Comments {
    return CommentsItem.Comments(
        id = this.id,
        textDisplay = this.textDisplay,
        textOriginal = this.textOriginal,
        userName = this.authorDisplayName,
        userProfileImage = this.authorProfileImageUrl,
        likeCount = this.likeCount,
        publishedAt = this.publishedAt?.convertToDaysAgo(),
        isUpdate = (this.updatedAt != this.publishedAt),
        totalReplyCount = this.totalReplyCount,
        replies = null,
    )
}

fun LocalCommentEntity.convertDetailComment() : DetailCommentsItem.Comments {
    return DetailCommentsItem.Comments(
        id = this.id,
        textDisplay = this.textDisplay,
        textOriginal = this.textOriginal,
        userName = this.authorDisplayName,
        userProfileImage = this.authorProfileImageUrl,
        likeCount = this.likeCount,
        publishedAt = this.publishedAt?.convertToDaysAgo(),
        isUpdate = (this.updatedAt != this.publishedAt),
        totalReplyCount = this.totalReplyCount,
        replies = null,
    )
}