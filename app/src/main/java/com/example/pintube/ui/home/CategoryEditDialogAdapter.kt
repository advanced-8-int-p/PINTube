package com.example.pintube.ui.home

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.pintube.databinding.CategoryDialogItemBinding

class CategoryEditDialogAdapter(
    private val onDeleteClick: (category: String) -> Unit
) : ListAdapter<String, CategoryEditDialogAdapter.ViewHolder>(object :
    DiffUtil.ItemCallback<String>() {
    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean =
        oldItem == newItem

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean =
        oldItem == newItem
}) {

    inner class ViewHolder(private val binding: CategoryDialogItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(item: String) = binding.also { b ->
            b.tvCategoryDialogItemName.text = item

            b.btnIvCategoryDialogItemX.setOnClickListener {
                Log.d("jj-카테고리 삭제 클릭", "$layoutPosition: $item")
                onDeleteClick(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.d("jj-카테고리편집 onCreateViewHolder", "viewType $viewType, parent: $parent\n")
        return ViewHolder(
            CategoryDialogItemBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }

}
