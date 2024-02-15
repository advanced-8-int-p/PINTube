package com.example.pintube.ui.mypage.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import coil.load
import com.example.pintube.databinding.VideoItemBinding
import com.example.pintube.ui.mypage.viewtype.MypageVideoItem
import com.example.pintube.utill.convertDurationTime

class RecyclerviewRecentVideoAdapter(
    private var mItems : List<MypageVideoItem>,
    private val onVideoClick: (MypageVideoItem) -> Int?
) : RecyclerView.Adapter<ViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = VideoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding, onVideoClick)
    }

    override fun getItemCount(): Int = mItems.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        (holder as ItemViewHolder).thumbnail.load(mItems[position].videoThumbnailUri)
        holder.length.text = mItems[position].length?.convertDurationTime()
        holder.videoTitle.text = mItems[position].title
        holder.channelImg.load(mItems[position].channelThumbnailUri)
        holder.channelName.text = mItems[position].channelName
        holder.videoViews.text = mItems[position].views
        holder.uploadDate.text = mItems[position].date
        holder.duration.text = mItems[position].length
        holder.root.setOnClickListener { onVideoClick(mItems[position]) }
    }

    inner class ItemViewHolder(binding: VideoItemBinding, private val onVideoClick: (MypageVideoItem) -> Int?): ViewHolder(binding.root) {
        val thumbnail = binding.ivItemVideo
        val length = binding.tvPopularItemLength
        val videoTitle = binding.tvItemTitle
        val channelImg = binding.ivItemChannel
        val channelName = binding.tvItemName
        val videoViews = binding.tvItemViews
        val uploadDate = binding.tvItemDate
        val duration = binding.tvPopularItemLength
        val root = binding.root
    }

    fun updateItem(newItems: List<MypageVideoItem?>) {
        mItems = newItems as List<MypageVideoItem>

        notifyDataSetChanged()
    }
}
