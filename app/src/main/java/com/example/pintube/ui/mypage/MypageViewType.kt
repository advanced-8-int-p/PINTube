package com.example.pintube.ui.mypage

sealed interface MypageViewType {

    //    data class Profile(val myProfile: MypageProfileData) : MypageViewType()
    data object TopHeader : MypageViewType

    data class Header(val title: String, val isRecent: Boolean) : MypageViewType

    data class RecentItems(
//        val items: MutableList<RecentItem>,
        val recentAdapter: RecyclerviewRecentVideoAdapter
    ) : MypageViewType

    data class PinItems(
//        val items: MutableList<PinItem>,
        val pinAdapter: RecyclerviewPinnedGroupAdapter
    ) : MypageViewType

    data class MyPageProfile(
        var channelThumbnail: String? = null,
        var channelName: String? = null,
        var channelId: String? = null,
        var isLogin: Boolean? = null,
    ) : MypageViewType

}
