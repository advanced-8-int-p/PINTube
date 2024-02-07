package com.example.pintube.ui.home

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.pintube.databinding.VideoItemBinding

class PopularVideoAdapter(
    private val onItemClick: (view: View, position: Int) -> Unit
) : RecyclerView.Adapter<PopularVideoAdapter.PopularViewHolder>() {

    var items = ArrayList<VideoItemData>()

    inner class PopularViewHolder(private val binding: VideoItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(model: VideoItemData) = binding.also { b ->
            model.videoThumbnailUri?.let { b.ivItemVideo.load(it) }
            model.channelThumbnailUri?.let { b.ivItemChannel.load(it) }
            model.title?.let { b.tvItemTitle.text = it }
            model.channelName?.let { b.tvItemName.text = it }

            b.root.setOnClickListener {
                Log.d("myTag:Popular 아이템 클릭", "$layoutPosition: ${items[layoutPosition]}")
                onItemClick(it, layoutPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularViewHolder {
        return PopularViewHolder(
            VideoItemBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: PopularViewHolder, position: Int) {
        holder.onBind(items[position])
    }

    override fun getItemCount(): Int = items.count()
}
