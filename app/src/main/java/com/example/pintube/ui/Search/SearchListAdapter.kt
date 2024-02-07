package com.example.pintube.ui.Search

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ListView
import androidx.recyclerview.widget.RecyclerView
import com.example.pintube.databinding.RecyclerviewSearchlistBinding

class SearchListAdapter (private val context: Context, private val searchHistoryList : List<String>) : RecyclerView.Adapter<SearchListAdapter.ListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = RecyclerviewSearchlistBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val searchHistoryItem = searchHistoryList[position]

    }

    override fun getItemCount(): Int {
        return searchHistoryList.size
    }

    inner class ListViewHolder (binding : RecyclerviewSearchlistBinding) : RecyclerView.ViewHolder (binding.root) {
        var searchHistory = binding.searchList


    }
}