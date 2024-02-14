package com.example.pintube.ui.mypage

sealed class MypageViewType {

//    data class Profile(val myProfile: MypageProfileData) : MypageViewType()

    data class Header(val title: String, val isRecent: Boolean) : MypageViewType()

    data class RecentItems(
        val recentAdapter: RecyclerviewRecentVideoAdapter
    ) : MypageViewType()

    data class PinItems(
//        val items: MutableList<PinItem>,
        val pinAdapter: RecyclerviewPinnedGroupAdapter
    ) : MypageViewType()
}

