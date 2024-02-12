package com.example.pintube.ui.home

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.request.CachePolicy
import com.example.pintube.databinding.PopularItemBinding

class PopularVideoAdapter(
    private val onItemClick: (item: VideoItemData) -> Unit
) : ListAdapter<VideoItemData, PopularVideoAdapter.PopularViewHolder>(object :
    DiffUtil.ItemCallback<VideoItemData>() {
    override fun areItemsTheSame(oldItem: VideoItemData, newItem: VideoItemData): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: VideoItemData, newItem: VideoItemData): Boolean =
        oldItem == newItem
}) {

    inner class PopularViewHolder(private val binding: PopularItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(item: VideoItemData) = binding.also { b ->
            item.videoThumbnailUri?.let {
                b.ivPopularItemVideo.load(it){
                    crossfade(true)
                    allowHardware(true)
                    if (position < 5) diskCachePolicy(CachePolicy.ENABLED)
                    else memoryCachePolicy(CachePolicy.ENABLED)
                } }
            item.channelThumbnailUri?.let {
                b.ivPopularItemChannel.load(it){
                crossfade(true)
                    allowHardware(true)
                    if (position < 5) diskCachePolicy(CachePolicy.ENABLED)
                    else memoryCachePolicy(CachePolicy.ENABLED)
            } }
            item.title?.let { b.tvPopularItemTitle.text = it }
            item.channelName?.let { b.tvPopularItemName.text = it }
            item.date?.let { b.tvPopularItemDate.text = it }
            item.views?.let { b.tvPopularItemViews.text = it }
            item.length?.let { b.tvPopularItemLength.text = it }

            b.root.setOnClickListener {
                Log.d("jj-Popular 아이템 클릭", "$layoutPosition: ${getItem(layoutPosition)}")
                onItemClick(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularViewHolder {
        return PopularViewHolder(
            PopularItemBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: PopularViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }

}
