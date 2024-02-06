package com.example.pintube.ui.home

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.pintube.databinding.VideoItemBinding

class CategoryVideoAdapter(
    private val onItemClick: (view: View, position: Int) -> Unit
) : RecyclerView.Adapter<CategoryVideoAdapter.CategoryVideoViewHolder>() {

    var items = ArrayList<VideoItemData>()

    inner class CategoryVideoViewHolder(private val binding: VideoItemBinding) :
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryVideoViewHolder {
        return CategoryVideoViewHolder(
            VideoItemBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: CategoryVideoViewHolder, position: Int) {
        holder.onBind(items[position])
    }

    override fun getItemCount(): Int = items.count()
}
