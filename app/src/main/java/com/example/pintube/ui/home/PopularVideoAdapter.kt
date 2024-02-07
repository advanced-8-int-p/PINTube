package com.example.pintube.ui.home

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.pintube.databinding.PopularItemBinding

class PopularVideoAdapter(
    private val onItemClick: (view: View, position: Int) -> Unit
) : RecyclerView.Adapter<PopularVideoAdapter.PopularViewHolder>() {

    var items = ArrayList<VideoItemData>()

    inner class PopularViewHolder(private val binding: PopularItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(item: VideoItemData) = binding.also { b ->
            item.videoThumbnailUri?.let { b.ivPopularItemVideo.load(it) }
            item.channelThumbnailUri?.let { b.ivPopularItemChannel.load(it) }
            item.title?.let { b.tvPopularItemTitle.text = it }
            item.channelName?.let { b.tvPopularItemName.text = it }
            item.length?.let { b.tvPopularItemLength.text = it }
            // item.isSaved

            b.root.setOnClickListener {
                Log.d("myTag:Popular 아이템 클릭", "$layoutPosition: ${items[layoutPosition]}")
                onItemClick(it, layoutPosition)
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
        holder.onBind(items[position])
    }

    override fun getItemCount(): Int = items.count()
}
