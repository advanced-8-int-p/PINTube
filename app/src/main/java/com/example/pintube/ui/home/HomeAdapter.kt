package com.example.pintube.ui.home

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import coil.load
import com.example.pintube.R
import com.example.pintube.databinding.HomeItemCategoryBinding
import com.example.pintube.databinding.HomeItemPopularBinding
import com.example.pintube.databinding.ItemHeaderBinding
import com.example.pintube.databinding.ItemLoadingProgressBinding
import com.example.pintube.databinding.VideoItemBinding

class HomeAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var sealedMultis = mutableListOf<SealedMulti>()

    class HeaderHolder(private val binding: ItemHeaderBinding) :
        RecyclerView.ViewHolder(binding.root)

    inner class MultiViewHolderPopular(private val binding: HomeItemPopularBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(item: SealedMulti.Popular) = with(binding){
            Log.d("jj-홈어댑터 popular onBind", item.toString())  //ddd

            rvPopularVideos.adapter = item.videoAdapter
            rvPopularVideos.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        }
    }

    inner class MultiViewHolderCategory(private val binding: HomeItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(item: SealedMulti.Category) = binding.also { b ->
            b.rvCategoryCategories.adapter = item.categoryAdapter
        }
    }

    inner class MultiViewHolderVideo(private val binding: VideoItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(item: SealedMulti.Video) = binding.also { b ->

            item.videoItemData.videoThumbnailUri?.let {
                b.ivItemVideo.load(it){
                    crossfade(true)
                }
            }
            item.videoItemData.channelThumbnailUri?.let {
                b.ivItemChannel.load(it){
                    crossfade(true)
                }
            }
            item.videoItemData.title?.let { b.tvItemTitle.text = it }
            item.videoItemData.channelName?.let { b.tvItemName.text = it }
            item.videoItemData.views?.let { b.tvItemViews.text = it }
            item.videoItemData.date?.let { b.tvItemDate.text = it }
            item.videoItemData.length?.let { b.tvPopularItemLength.text = it }

//            b.root.setOnClickListener {
//                Log.d("jj-홈 아이템(비디오) 클릭", "$layoutPosition: ${getItem(layoutPosition)}")
//                onItemClick(it, layoutPosition)
//            }
        }
    }

    class LoadingHolder(private val binding: ItemLoadingProgressBinding):
    RecyclerView.ViewHolder(binding.root)

    override fun getItemCount(): Int = sealedMultis.size
    override fun getItemViewType(position: Int): Int = sealedMultis[position].viewType

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            MULTI_HEADER ->
                HeaderHolder(
                    ItemHeaderBinding
                        .inflate(LayoutInflater.from(parent.context), parent, false)
                )

            MULTI_POPULAR ->
                MultiViewHolderPopular(
                    HomeItemPopularBinding
                        .inflate(LayoutInflater.from(parent.context), parent, false)
                )

            MULTI_CATEGORY ->
                MultiViewHolderCategory(
                    HomeItemCategoryBinding
                        .inflate(LayoutInflater.from(parent.context), parent, false)
                )

            MULTI_VIDEO ->
                MultiViewHolderVideo(
                    VideoItemBinding
                        .inflate(LayoutInflater.from(parent.context), parent, false)
                )

            MULTI_LOADING ->
                LoadingHolder(
                    ItemLoadingProgressBinding
                        .inflate(LayoutInflater.from(parent.context), parent, false)
                )

            else -> error("jj-HomeAdapter.kt onCreateViewHolder viewType error")
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

            is SealedMulti.Header -> Unit

            is SealedMulti.Video -> (holder as MultiViewHolderVideo).onBind(item)

            is SealedMulti.Loading -> Unit
        }
    }

}
