package com.example.pintube.ui.home

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.pintube.databinding.VideoItemBinding

class CategoryVideoAdapter(
    private val onItemClick: (view: View, position: Int) -> Unit
) : ListAdapter<VideoItemData, CategoryVideoAdapter.CategoryVideoViewHolder>(object :
    DiffUtil.ItemCallback<VideoItemData>() {
    override fun areItemsTheSame(oldItem: VideoItemData, newItem: VideoItemData): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: VideoItemData, newItem: VideoItemData): Boolean =
        oldItem == newItem
}) {

    inner class CategoryVideoViewHolder(private val binding: VideoItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(model: VideoItemData) = binding.also { b ->
            model.videoThumbnailUri?.let { b.ivItemVideo.load(it) }
            model.channelThumbnailUri?.let { b.ivItemChannel.load(it) }
            model.title?.let { b.tvItemTitle.text = it }
            model.channelName?.let { b.tvItemName.text = it }

            b.root.setOnClickListener {
                Log.d("jj-Popular 아이템 클릭", "$layoutPosition: ${getItem(layoutPosition)}")
                onItemClick(it, layoutPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryVideoViewHolder {
        return CategoryVideoViewHolder(
            VideoItemBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: CategoryVideoViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }
}
