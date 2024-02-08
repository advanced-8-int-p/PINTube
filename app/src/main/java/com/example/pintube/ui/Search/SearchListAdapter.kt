package com.example.pintube.ui.Search

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pintube.R
import com.example.pintube.databinding.RecyclerviewSearchlistBinding
import org.w3c.dom.Text

class SearchListAdapter (private val searchHistoryList: MutableList<String>) : RecyclerView.Adapter<SearchListAdapter.ListViewHolder>() {

    interface ItemClick {
        fun onClick(view: View, position: Int)
        fun onRemoveClick(position: Int)
    }
    var itemClick : ItemClick? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = RecyclerviewSearchlistBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.itemView.setOnClickListener{
            itemClick?.onClick(it, position)
        }

        val searchQuery = searchHistoryList[position]
        holder.searchList.text = searchQuery

        holder.removeSearch.setOnClickListener {
            itemClick?.onRemoveClick(position) // 클릭된 항목의 위치를 전달

            SharedPrefManager.removeSearchHistoryItem(holder.itemView.context, searchQuery)

        }
    }

    override fun getItemCount(): Int {
        return searchHistoryList.size
    }


    inner class ListViewHolder (binding : RecyclerviewSearchlistBinding) : RecyclerView.ViewHolder (binding.root) {
        val searchList = binding.searchList
        val removeSearch = binding.removeSearch

    }
}