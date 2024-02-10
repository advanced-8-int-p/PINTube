package com.example.pintube.ui.shorts.model

sealed interface CommentsItem {
    data class Comments(
        val id: String?,
        val textDisplay: String?,
        val textOriginal: String?,
        val userName: String?,
        val userProfileImage: String?,
        val likeCount: Int?,
        val publishedAt: String?,
        val isUpdate: Boolean?,
        val totalReplyCount: Int?,
        val replies: List<Comments?>?,
    ): CommentsItem
}