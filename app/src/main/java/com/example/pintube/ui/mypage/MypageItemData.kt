package com.example.pintube.ui.mypage

data class MypageItemData(
    val myAccountProfileUri: String?,
    val myAccountName: String?,
    val myAccountId: String?,
    val myAccountSubscriber: String?,
    val myAccountVideoCount: String?,
    val myAccountDescription: String?,
    val myPageRecentItems: MutableList<String>?,
    val myPagePinnedItems: MutableList<String>?,
    )
