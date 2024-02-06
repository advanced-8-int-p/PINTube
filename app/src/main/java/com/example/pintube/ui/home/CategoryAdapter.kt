package com.example.pintube.ui.home

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pintube.databinding.CategoryItemBinding

class CategoryAdapter(
    private val onItemClick: (view: View, position: Int) -> Unit
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    var items = ArrayList<String>()

    inner class CategoryViewHolder(private val binding: CategoryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(model: String) = binding.also { b ->
            Log.d("myTag:카테고리뷰홀더 onBind", model)
            b.tvCategoryName.text = model

            b.root.setOnClickListener {
                Log.d("myTag:Popular 아이템 클릭", "$layoutPosition: ${items[layoutPosition]}")
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
        holder.onBind(items[position])
    }

    override fun getItemCount(): Int = items.count()
}
