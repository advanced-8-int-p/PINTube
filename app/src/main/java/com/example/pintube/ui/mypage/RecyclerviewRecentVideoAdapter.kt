package com.example.pintube.ui.mypage

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.pintube.databinding.RecyclerviewRecentVideosBinding
import com.example.pintube.databinding.VideoItemBinding
import com.example.pintube.utill.Constants
import com.example.pintube.utill.convertDurationTime

class RecyclerviewRecentVideoAdapter() : RecyclerView.Adapter<ViewHolder>() {

    val mItems = mutableListOf<RecentItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = VideoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return mItems.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        (holder as ItemViewHolder).thumbnail.setImageURI(mItems[position].thumbnailUrl.toUri())
        holder.length.text = mItems[position].length.convertDurationTime()
        holder.videoTitle.text = mItems[position].title
        holder.channelImg.isVisible = false
        holder.channelName.text = mItems[position].channelName
        holder.videoViews.isVisible = false
        holder.uploadDate.isVisible = false
    }

    inner class ItemViewHolder(binding: VideoItemBinding): RecyclerView.ViewHolder(binding.root) {

        val thumbnail = binding.ivItemVideo
        val length = binding.tvPopularItemLength
        val videoTitle = binding.tvItemTitle
        val channelImg = binding.ivItemChannel
        val channelName = binding.tvItemName
        val videoViews = binding.tvItemViews
        val uploadDate = binding.tvItemDate

    }

}
