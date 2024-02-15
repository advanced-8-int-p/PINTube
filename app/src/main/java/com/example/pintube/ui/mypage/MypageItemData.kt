package com.example.pintube.ui.mypage

data class MypageProfileData(
    var myAccountProfileUri: String?,
    var myAccountName: String?,
    var myAccountId: String?,
//    var myAccountSubscriber: String?,
//    var myAccountVideoCount: String?,
//    var myAccountDescription: String?,
)

/*
myAccountProfileUri = null,
myAccountName = null,
myAccountId = null,
myAccountSubscriber = null,
myAccountVideoCount = null,
myAccountDescription = null,
 */

data class VideoItem(
    val thumbnailUrl: String,
    val title: String,
    val length: String,
//    val channelThumbnail: String,
    val channelName: String,
//    val viewCount: String,
//    val likeCount: String
)


//data class PinItem(val pinGroupTag: String, val thumbnailUrl: String, val videoCount: String)
