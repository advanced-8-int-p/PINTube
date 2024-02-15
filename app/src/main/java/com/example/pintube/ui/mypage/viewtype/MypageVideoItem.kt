package com.example.pintube.ui.mypage.viewtype

data class MypageVideoItem(
    var videoThumbnailUri: String? = "https://picsum.photos/200/300",
    var channelThumbnailUri: String? = "https://picsum.photos/200/300",
    var title: String? = null,
    var channelName: String? = null,
    var views: String? = null,
    var date: String? = null,
    var length: String? = null,
    var isSaved: Boolean = false,
    val id: String? = null,
    val channelId: String? = null,
)
