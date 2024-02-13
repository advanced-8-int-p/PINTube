package com.example.pintube.ui.mypage

data class MypageProfileData(
    val myAccountProfileUri: String?,
    val myAccountName: String?,
    val myAccountId: String?,
    val myAccountSubscriber: String?,
    val myAccountVideoCount: String?,
    val myAccountDescription: String?,
    )

data class RecentItem(
    val thumbnailUrl: String,
    val title: String,
    val length: String,
//    val channelThumbnail: String,
    val channelName: String,
//    val viewCount: String,
//    val likeCount: String
)


data class PinItem(val pinGroupTag: String, val thumbnailUrl: String, val videoCount: String)
