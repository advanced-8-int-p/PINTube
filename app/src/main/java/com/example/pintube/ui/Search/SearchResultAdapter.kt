package com.example.pintube.ui.Search

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pintube.data.model.VideoModel
import com.example.pintube.databinding.RecyclerviewResultBinding

class SearchResultAdapter(private val mContext : Context) : RecyclerView.Adapter<SearchResultAdapter.SearchResultViewHolder>(){

    var items = ArrayList<VideoModel>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultViewHolder {
        val binding = RecyclerviewResultBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchResultViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchResultViewHolder, position: Int) {
        val item = items[position]

        holder.mainTitle.text = item.title
        holder.chTitle.text = item.channelTitle
        holder.uploadDate.text = item.publishedDate

        Glide.with(mContext)
            .load(items[position].thumbnail)
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
    }
}