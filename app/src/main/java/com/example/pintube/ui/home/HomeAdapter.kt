package com.example.pintube.ui.home

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pintube.databinding.HomeItemCategoryBinding
import com.example.pintube.databinding.HomeItemPopularBinding
import com.example.pintube.databinding.ItemHeaderBinding

class HomeAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var sealedMultis = mutableListOf<SealedMulti>()

    class HeaderHolder(private val binding: ItemHeaderBinding) : RecyclerView.ViewHolder(binding.root)
    inner class MultiViewHolderPopular(private val binding: HomeItemPopularBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(item: SealedMulti.Popular) {
            Log.d("myTag:홈어댑터 popular onBind", item.toString())  //ddd

            binding.rvPopularVideos.adapter = item.videoAdapter
        }
    }

    inner class MultiViewHolderCategory(private val binding: HomeItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(item: SealedMulti.Category) = binding.also { b ->
            b.rvCategoryCategories.adapter = item.categoryAdapter
            b.rvCategoryVideos.adapter = item.videoAdapter
        }
    }

    override fun getItemCount(): Int = sealedMultis.size
    override fun getItemViewType(position: Int): Int = sealedMultis[position].viewType

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            MULTI_HEADER -> HeaderHolder(ItemHeaderBinding.inflate(LayoutInflater.from(parent.context),parent, false))
            MULTI_POPULAR ->
                MultiViewHolderPopular(
                    HomeItemPopularBinding
                        .inflate(LayoutInflater.from(parent.context), parent, false)
                )

            // MULTI_CATEGORY
            else ->
                MultiViewHolderCategory(
                    HomeItemCategoryBinding
                        .inflate(LayoutInflater.from(parent.context), parent, false)
                )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = sealedMultis[position]) {
            is SealedMulti.Popular -> {
                (holder as MultiViewHolderPopular).onBind(item)
                // TODO: holder.setIsRecyclable(false) 안쓰면 문제가 있나?
//                holder.setIsRecyclable(false)
            }

            is SealedMulti.Category -> {
                (holder as MultiViewHolderCategory).onBind(item)
//                holder.setIsRecyclable(false)
            }

            is SealedMulti.Header -> {
                (holder as HeaderHolder)
            }
        }
    }

}
