package com.example.pintube.ui.home

import android.graphics.drawable.GradientDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.pintube.R
import com.example.pintube.databinding.CategoryItemBinding
import com.example.pintube.utill.dpToPx

class CategoryAdapter(
    private val onItemClick: (query: String) -> Unit
) : ListAdapter<String, CategoryAdapter.CategoryViewHolder>(object :
    DiffUtil.ItemCallback<String>() {
    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean = true
    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean =
        oldItem == newItem
}) {
    private var selectedItem: String? = null

    inner class CategoryViewHolder(private val binding: CategoryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(item: String) = binding.also { b ->
//            Log.d("jj-카테고리뷰홀더 onBind", model)  //ddd
            if (selectedItem == null) selectedItem = item
            updateUI(item == selectedItem, binding)
            b.tvCategoryName.text = item

            b.root.setOnClickListener {
                selectedItem = item
                Log.d("jj-카테고리어댑터 아이템 클릭", "$layoutPosition: $item")
                onItemClick(item)
                notifyDataSetChanged()  // 모든 항목을 다시 바인딩하여 선택 상태를 업데이트합니다.
            }
        }

        private fun updateUI(
            isSelected: Boolean,
            binding: CategoryItemBinding
        ) = with(binding){
            val context = binding.root.context
            val drawable = GradientDrawable()
            drawable.shape = GradientDrawable.RECTANGLE
            drawable.cornerRadius = 256.dpToPx(context).toFloat()

            if (!isSelected) {
                drawable.setStroke(2.dpToPx(context), context.getColor(R.color.main_color))
                drawable.setColor(context.getColor(R.color.white))
                tvCategoryName.setTextColor(context.getColor(R.color.main_color))
            } else {
                drawable.setColor(context.getColor(R.color.main_color))
                tvCategoryName.setTextColor(context.getColor(R.color.white))
            }

            binding.btnCategoryBtn.background = drawable
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder(
            CategoryItemBinding
                .inflate(LayoutInflater.from(parent.context), parent, false),
        )
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }

}
