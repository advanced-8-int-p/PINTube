package com.example.pintube.ui.mypage

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.pintube.databinding.ItemChannelProfileBinding
import com.example.pintube.databinding.ItemMypageHeaderBinding
import com.example.pintube.databinding.ItemMypagePinGroupBinding
import com.example.pintube.databinding.RecyclerviewPinnedGroupBinding
import com.example.pintube.databinding.RecyclerviewRecentVideosBinding
import com.example.pintube.databinding.VideoItemBinding

class MypageAdapter(private val mContext: Context): RecyclerView.Adapter<ViewHolder>() {

    var multiViewType = mutableListOf<MypageViewType>()

    companion object {
        private const val VIEW_TYPE_PROFILE = 1
        private const val VIEW_TYPE_TITLE = 2
        private const val VIEW_TYPE_RECENT = 3
        private const val VIEW_TYPE_PINNED = 4
    }

    interface ItemClick {
        fun onClick(view: View, position: Int, id: String)
    }

    var itemClick: ItemClick? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(mContext)
        return when (viewType) {
            VIEW_TYPE_PROFILE -> {
                val binding = ItemChannelProfileBinding.inflate(LayoutInflater.from(mContext), parent, false)
                MypageProfileViewHolder(binding)
            }
            VIEW_TYPE_TITLE -> {
                val binding = ItemMypageHeaderBinding.inflate(LayoutInflater.from(mContext), parent, false)
                MypageHeaderViewHolder(binding)
            }
            VIEW_TYPE_RECENT -> {
                val binding = RecyclerviewRecentVideosBinding.inflate(LayoutInflater.from(mContext), parent, false)
                MypageRecentItemViewHolder(binding)
            }
            else -> {
                val binding = RecyclerviewPinnedGroupBinding.inflate(LayoutInflater.from(mContext), parent, false)
                MypagePinItemViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (val items = multiViewType[position]) {
            is MypageViewType.Profile -> {
                (holder as MypageProfileViewHolder).userProfile.setImageURI(items.userProfile.toUri())
                holder.userName.text = items.userName
                holder.userId.text = items.userId
                holder.userInfo.text = "구독자" + items.userSubscriber.toString() + "명" + "  " + "영상"
            }
            is MypageViewType.Header -> {

            }
            is MypageViewType.RecentItems -> {

            }
            else -> {

            }
        }
    }

    override fun getItemCount(): Int {
        return multiViewType.size
    }

//    override fun getItemViewType(position: Int): Int {
//        return
//
//    }

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

    inner class MypageRecentItemViewHolder(binding: RecyclerviewRecentVideosBinding): RecyclerView.ViewHolder(binding.root) {
        val recyclerView = binding.rvRecyclerviewRecent.adapter
        val adapter = RecyclerviewRecentVideoAdapter(mContext)
//        val thumbnail = binding.ivItemVideo
//        val length = binding.tvPopularItemLength
//        val videoTitle = binding.tvItemTitle
//        val channelImg = binding.ivItemChannel
//        val channelName = binding.tvItemName
//        val videoViews = binding.tvItemViews
//        val uploadDate = binding.tvItemDate
        //새로 adapter binding recyclerview
    }

    inner class MypagePinItemViewHolder(binding: RecyclerviewPinnedGroupBinding): RecyclerView.ViewHolder(binding.root) {
        val recyclerView = binding.rvRecyclerviewPinned
        val adapter = RecyclerviewPinnedGroupAdapter()
//        val groupThumbnail = binding.ivPinGroupThumbnail
//        val groupVideoCount = binding.tvPinGroupCount
//        val groupName = binding.tvPinGroupName
    }

}