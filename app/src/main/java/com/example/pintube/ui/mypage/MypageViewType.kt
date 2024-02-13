package com.example.pintube.ui.mypage

import com.example.pintube.data.local.dao.ChannelThumbnail
import com.example.pintube.domain.entitiy.VideoEntity
import com.example.pintube.ui.home.VideoItemData

sealed class MypageViewType {

    data class Profile(val myProfile: MypageProfileData) : MypageViewType()

    data class Header(val title: String, val isRecent: Boolean) : MypageViewType()

    data class RecentItems(val items: MutableList<RecentItem>) : MypageViewType()

    data class PinItems(val items: MutableList<PinItem>) : MypageViewType()
}

