package com.example.pintube.ui.mypage

import com.example.pintube.domain.entitiy.VideoEntity
import com.example.pintube.ui.home.VideoItemData

sealed class MypageViewType {

    data class Profile(val userProfile: String, val userName: String, val userId: String, val userSubscriber: Int, val userVideoCount: Int, val userDescription: String): MypageViewType()

    data class Header(val title: String, val isRecent: Boolean): MypageViewType()

    data class RecentItems(val ids: MutableList<String>): MypageViewType()

    data class PinItems(val data: VideoItemData, val pinTag: String): MypageViewType()
}
