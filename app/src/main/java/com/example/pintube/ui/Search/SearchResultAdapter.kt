package com.example.pintube.ui.Search

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pintube.data.local.entity.LocalSearchEntity
import com.example.pintube.data.local.entity.VideoCacheEntity
import com.example.pintube.databinding.RecyclerviewResultBinding
import com.example.pintube.domain.entitiy.SearchEntity


class SearchResultAdapter(private val mContext : Context, private val items : MutableList<SearchEntity>) : RecyclerView.Adapter<SearchResultAdapter.SearchResultViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultViewHolder {
        val binding = RecyclerviewResultBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchResultViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchResultViewHolder, position: Int) {
        val item = items[position]




        holder.chTitle.text = item.channelTitle
        holder.uploadDate.text = item.publishedAt

        Glide.with(mContext)
            .load(items[position].thumbnailHigh)
            .into(holder.mainImage)
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
}