package com.example.pintube.ui.Search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.pintube.databinding.RecyclerviewResultBinding
import com.example.pintube.utill.convertToDaysAgo
import com.example.pintube.utill.convertViewCount
import java.time.LocalDateTime


class SearchResultAdapter(private val items: MutableList<SearchData>) :
    RecyclerView.Adapter<SearchResultAdapter.SearchResultViewHolder>() {

    interface ItemClick {
        fun onClick(position: Int)
    }

    var itemClick: ItemClick? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultViewHolder {
        val binding = RecyclerviewResultBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchResultViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchResultViewHolder, position: Int) {
        holder.onBind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class SearchResultViewHolder(private val binding: RecyclerviewResultBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(item: SearchData) = binding.also { b ->
            itemView.setOnClickListener {
                itemClick?.onClick(position)
            }

            b.rvSearchTitle.text = item.title?.replace("&#39;", "'")
            b.rvSearchChTitle.text = item.channelTitle
            b.rvSearchUploadDate.text = item.publishedAt?.convertToDaysAgo()
            b.rvSearchImage.load(items[position].videoThumbnailUri)
            b.rvSearchViewCount.text = item.viewCount?.convertViewCount()

        }
    }

    fun sortByAscending() {
        items.sortBy { it.publishedAt?.replace("Z", "").let { date -> LocalDateTime.parse(date) } }
        notifyDataSetChanged()
    }

    fun sortByDescending() {
        items.sortByDescending {
            it.publishedAt?.replace("Z", "").let { date -> LocalDateTime.parse(date) }
        }
        notifyDataSetChanged()
    }
}