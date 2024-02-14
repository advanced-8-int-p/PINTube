package com.example.pintube.ui.mypage

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import coil.load
import com.example.pintube.databinding.ItemMypagePinGroupBinding

class RecyclerviewPinnedGroupAdapter() : RecyclerView.Adapter<ViewHolder>() {

    val mItems = mutableListOf<PinItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMypagePinGroupBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun getItemCount(): Int = mItems.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        (holder as ItemViewHolder).groupThumbnail.load(mItems[position].thumbnailUrl)
        holder.groupVideoCount.text = mItems[position].videoCount
        holder.groupName.text = mItems[position].pinGroupTag
    }

    inner class ItemViewHolder(binding: ItemMypagePinGroupBinding): RecyclerView.ViewHolder(binding.root) {

        val groupThumbnail = binding.ivPinGroupThumbnail
        val groupVideoCount = binding.tvPinGroupCount
        val groupName = binding.tvPinGroupName
    }


}
