package com.example.pintube.ui.detailpage

sealed interface DetailCommentsItem {
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
    ): DetailCommentsItem

    data object NoComments : DetailCommentsItem
}