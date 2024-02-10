package com.example.pintube.ui.mypage

import com.example.pintube.domain.entitiy.VideoEntity
import com.example.pintube.ui.home.VideoItemData

sealed class MypageViewType {

    data class Profile(val userId: String): MypageViewType()

    data class Header(val title: String): MypageViewType()

    data class RecentItems(val data: VideoItemData): MypageViewType()

    data class PinItems(val data: VideoItemData): MypageViewType()
}
