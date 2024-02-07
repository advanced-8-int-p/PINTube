package com.example.pintube.ui.home

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.pintube.databinding.CategoryItemBinding

class CategoryAdapter(
    private val onItemClick: (view: View, position: Int) -> Unit
) : ListAdapter<String, CategoryAdapter.CategoryViewHolder>(object :
    DiffUtil.ItemCallback<String>() {
    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean = true
    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean =
        oldItem == newItem
}) {

    inner class CategoryViewHolder(private val binding: CategoryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(item: String) = binding.also { b ->
//            Log.d("myTag:카테고리뷰홀더 onBind", model)  //ddd
            b.tvCategoryName.text = item

            b.root.setOnClickListener {
                Log.d("myTag:카테고리어댑터 아이템 클릭", "$layoutPosition: $item")
                onItemClick(it, layoutPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder(
            CategoryItemBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }

}
