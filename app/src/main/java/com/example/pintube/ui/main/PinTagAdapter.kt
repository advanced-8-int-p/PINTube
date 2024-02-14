package com.example.pintube.ui.main

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.pintube.databinding.DialogSelectPinTagBinding
import com.example.pintube.databinding.ItemPinTagCheckboxBinding
import com.example.pintube.ui.detailpage.DetailCommentsItem
import com.example.pintube.ui.detailpage.adapter.DetailCommentAdapter

class PinTagAdapter: RecyclerView.Adapter<ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PinTagAdapter.ItemViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    inner class ItemViewHolder(private val binding : ItemPinTagCheckboxBinding): RecyclerView.ViewHolder(binding.root) {

    }

}
