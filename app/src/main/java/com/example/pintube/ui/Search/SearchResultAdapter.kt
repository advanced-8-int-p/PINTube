package com.example.pintube.ui.Search

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.bumptech.glide.Glide
import com.example.pintube.databinding.RecyclerviewResultBinding
import com.example.pintube.domain.entitiy.SearchEntity
import com.example.pintube.domain.entitiy.VideoEntity
import com.example.pintube.utill.convertToDaysAgo
import com.example.pintube.utill.convertViewCount
import java.time.LocalDateTime


class SearchResultAdapter( private val items : MutableList<SearchEntity>) : RecyclerView.Adapter<SearchResultAdapter.SearchResultViewHolder>(){

    interface ItemClick {
        fun onClick(position: Int)
    }
    var itemClick : ItemClick? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultViewHolder {
        val binding = RecyclerviewResultBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchResultViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchResultViewHolder, position: Int) {
        holder.itemView.setOnClickListener{
            itemClick?.onClick(position)
        }

        val item = items[position]

        holder.mainTitle.text = item.title?.replace("&#39;", "'")
        holder.chTitle.text = item.channelTitle
        holder.uploadDate.text = item.publishedAt?.convertToDaysAgo()
        holder.mainImage.load(items[position].thumbnailHigh)


    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class SearchResultViewHolder(binding: RecyclerviewResultBinding) :RecyclerView.ViewHolder(binding.root) {
        var mainImage = binding.rvSearchImage
        var chImage = binding.rvSearchChImage
        var mainTitle = binding.rvSearchTitle
        var chTitle = binding.rvSearchChTitle
        var uploadDate = binding.rvSearchUploadDate
        var viewCount =binding.rvSearchViewCount
    }

    fun sortByAscending() {
        items.sortBy { it.publishedAt?.replace("Z", "").let { date -> LocalDateTime.parse(date) } }
        notifyDataSetChanged()
    }
    fun sortByDescending() {
        items.sortByDescending { it.publishedAt?.replace("Z", "").let { date -> LocalDateTime.parse(date) } }
        notifyDataSetChanged()
    }
}