package com.example.pintube.ui.mypage

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.pintube.databinding.ItemChannelProfileBinding
import com.example.pintube.databinding.ItemMypageHeaderBinding
import com.example.pintube.databinding.RecyclerviewPinnedGroupBinding
import com.example.pintube.databinding.RecyclerviewRecentVideosBinding
import com.example.pintube.utill.Constants
import com.example.pintube.utill.convertViewCount

class MypageAdapter(private val mContext: Context, private val multiViewType: MutableList<MypageViewType>): RecyclerView.Adapter<ViewHolder>() {

//    var multiViewType = mutableListOf<MypageViewType>()

    companion object {
        private const val VIEW_TYPE_PROFILE = 0
        private const val VIEW_TYPE_TITLE = 1
        private const val VIEW_TYPE_RECENT = 2
        private const val VIEW_TYPE_PINNED = 3
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


    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (val items = multiViewType[position]) {
            is MypageViewType.Profile -> {
                (holder as MypageProfileViewHolder).userProfile.setImageURI(items.myProfile.myAccountProfileUri.toString().toUri())
                holder.userName.text = items.myProfile.myAccountName.toString()
                holder.userId.text = items.myProfile.myAccountId.toString()
                holder.userInfo.text = "구독자" + items.myProfile.myAccountSubscriber.toString().convertViewCount() + "명" + "  " + "동영상" + items.myProfile.myAccountVideoCount.toString().convertViewCount() + "개"
                holder.userDescription.text = items.myProfile.myAccountDescription.toString()
            }
            is MypageViewType.Header -> {
                (holder as MypageHeaderViewHolder).title.text = items.title
                holder.btn.isVisible = items.isRecent
            }
            is MypageViewType.RecentItems -> {
                val adapter = RecyclerviewRecentVideoAdapter(mContext, Constants.recentItemsList)
                (holder as MypageRecentItemViewHolder).recyclerView.adapter = adapter
                holder.recyclerView.layoutManager = LinearLayoutManager(mContext)
            }
            is MypageViewType.PinItems -> {
                val adapter = RecyclerviewPinnedGroupAdapter(mContext, items.items)
                (holder as MypagePinItemViewHolder).recyclerView.adapter = adapter
                holder.recyclerView.layoutManager = GridLayoutManager(mContext, 2)
            }
        }
    }

    override fun getItemCount(): Int {
        return multiViewType.size
    }

    override fun getItemViewType(position: Int): Int {
        return when(multiViewType[position]) {
            is MypageViewType.Profile -> VIEW_TYPE_PROFILE
            is MypageViewType.Header -> VIEW_TYPE_TITLE
            is MypageViewType.RecentItems -> VIEW_TYPE_RECENT
            is MypageViewType.PinItems -> VIEW_TYPE_PINNED
        }

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

    inner class MypageRecentItemViewHolder(binding: RecyclerviewRecentVideosBinding): RecyclerView.ViewHolder(binding.root) {
        val recyclerView = binding.rvRecyclerviewRecent

    }

    inner class MypagePinItemViewHolder(binding: RecyclerviewPinnedGroupBinding): RecyclerView.ViewHolder(binding.root) {
        val recyclerView = binding.rvRecyclerviewPinned

    }

}