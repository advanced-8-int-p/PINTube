package com.example.pintube.ui.mypage

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.pintube.databinding.FragmentMypageBinding
import com.example.pintube.databinding.ItemChannelProfileBinding
import com.example.pintube.databinding.ItemHeaderBinding
import com.example.pintube.databinding.ItemMypageHeaderBinding
import com.example.pintube.databinding.ItemMypagePinGroupBinding
import com.example.pintube.databinding.VideoItemBinding

class MypageAdapter(private val mContext: Context): RecyclerView.Adapter<ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_PROFILE = 1
        private const val VIEW_TYPE_TITLE = 2
        private const val VIEW_TYPE_RECENT = 3
        private const val VIEW_TYPE_PINNED = 4
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    inner class MypageProfileViewHolder(binding: ItemChannelProfileBinding): RecyclerView.ViewHolder(binding.root) {
        val userProfile = binding.ivMypageProfile
        val userName = binding.tvMypageChannelName
        val userId = binding.tvMypageChannelId
        val userInfo = binding.tvMypageChannelInfo
        val userDescription = binding.tvMypageChannelDescription

    }

    inner class MypageHeaderViewHolder(binding: ItemMypageHeaderBinding): RecyclerView.ViewHolder(binding.root) {
        val title = binding.tvMyHeaderTitle
        val btn = binding.tvMyHeaderBtn
    }

    inner class MypageRecentItemViewHolder(binding: VideoItemBinding): RecyclerView.ViewHolder(binding.root) {
        val thumbnail = binding.ivItemVideo
        val length = binding.tvPopularItemLength
        val videoTitle = binding.tvItemTitle
        val channelImg = binding.ivItemChannel
        val channelName = binding.tvItemName
        val videoViews = binding.tvItemViews
        val uploadDate = binding.tvItemDate
    }

    inner class MypagePinItemViewHolder(binding: ItemMypagePinGroupBinding): RecyclerView.ViewHolder(binding.root) {
        val groupThumbnail = binding.ivPinGroupThumbnail
        val groupVideoCount = binding.tvPinGroupCount
        val groupName = binding.tvPinGroupName
    }

}