package com.example.pintube.ui.shorts.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.ListAdapter
import com.example.pintube.databinding.ItemShortsBinding
import com.example.pintube.databinding.UnknownItemBinding
import com.example.pintube.ui.shorts.ShortsViewType
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShortsViewHolder =
        when(ShortsViewType.from(viewType)) {
            ShortsViewType.ITEM -> ShortsItemViewHolder(
                ItemShortsBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            )

            else -> ShortsUnknownViewHolder(
                UnknownItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
        }

    override fun onBindViewHolder(holder: ShortsViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }

    class ShortsItemViewHolder (
        private val binding: ItemShortsBinding,
    ) : ShortsViewHolder(binding.root) {
        override fun onBind(item: ShortsItem) = with(binding) {
            if (item !is ShortsItem.Item) {
                return@with
            }
        }

    }

    class ShortsUnknownViewHolder(
        private val binding: UnknownItemBinding,
    ): ShortsViewHolder(binding.root) {
        override fun onBind(item: ShortsItem) = Unit
    }

}
