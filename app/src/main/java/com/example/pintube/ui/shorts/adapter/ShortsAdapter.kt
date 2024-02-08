/*
package com.example.pintube.ui.shorts.adapter

import android.view.View
import android.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.pintube.ui.shorts.model.ShortsItem

class ShortsAdapter(
    private val onBookmarkChecked: (ShortsItem) -> Unit,
    private val onSharedChecked: (ShortsItem) -> Unit,
) : ListAdapter<ShortsItem, ShortsAdapter.ShortsViewHolder>(

    object : DiffUtil.ItemCallback<ShortsItem>() {
        override fun areItemsTheSame(
            oldItem: ShortsItem,
            newItem: ShortsItem
        ): Boolean = if (oldItem is ShortsItem.Item && newItem is ShortsItem.Item) {
            oldItem.id == newItem.id
        } else {
            oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: ShortsItem,
            newItem: ShortsItem
        ): Boolean = oldItem == newItem

    }
) {

    abstract class ShortsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        abstract fun onBind(item: ShortsItem)
    }

}*/
