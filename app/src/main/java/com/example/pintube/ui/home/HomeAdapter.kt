package com.example.pintube.ui.home

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import coil.load
import coil.request.CachePolicy
import com.example.pintube.databinding.HomeItemCategoryBinding
import com.example.pintube.databinding.HomeItemPopularBinding
import com.example.pintube.databinding.ItemHeaderBinding
import com.example.pintube.databinding.ItemLoadingProgressBinding
import com.example.pintube.databinding.VideoItemBinding

sealed interface MultiView {

    enum class Type {
        HEADER,
        POPULAR,
        CATEGORY,
        VIDEO,
        LOADING,
    }

    val viewType: Type

    data object Header : MultiView {
        override val viewType: Type = Type.HEADER
    }

    data class Popular(
        val videoAdapter: PopularVideoAdapter
    ) : MultiView {
        override val viewType: Type = Type.POPULAR
    }

    data class Category(
        val categoryAdapter: CategoryAdapter,
    ) : MultiView {
        override val viewType: Type = Type.CATEGORY
    }

    data class Video(
        val videoItemData: VideoItemData,
    ) : MultiView {
        override val viewType: Type = Type.VIDEO
    }

    data object Loading : MultiView {
        override val viewType: Type = Type.LOADING
    }

}


class HomeAdapter(
    private val onCategorySettingClick: () -> Unit,
    private val onVideoClick: (item: VideoItemData) -> Int?
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var multiViews = mutableListOf<MultiView>()
    var tvCategoryEmptyText: TextView? = null

    inner class HeaderHolder(binding: ItemHeaderBinding) :
        RecyclerView.ViewHolder(binding.root)

    inner class PopularHolder(private val binding: HomeItemPopularBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(item: MultiView.Popular) = with(binding) {
//            Log.d("jj-홈어댑터 popular onBind", item.toString())  //ddd

            rvPopularVideos.adapter = item.videoAdapter
            rvPopularVideos.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        }
    }

    inner class CategoryHolder(private val binding: HomeItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.btnFlCategorySetting.setOnClickListener { onCategorySettingClick() }
            tvCategoryEmptyText = binding.tvCategoryEmptyText
        }

        fun onBind(item: MultiView.Category) = binding.also { b ->
            b.rvCategoryCategories.adapter = item.categoryAdapter
            b.tvCategoryEmptyText.isVisible = item.categoryAdapter.itemCount == 0
        }
    }

    inner class VideoHolder(private val binding: VideoItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(item: MultiView.Video) = binding.also { b ->

            item.videoItemData.videoThumbnailUri?.let {
                b.ivItemVideo.load(it) {
                    crossfade(true)
                    allowHardware(true)
                    if (position < 5) diskCachePolicy(CachePolicy.ENABLED)
                    else memoryCachePolicy(CachePolicy.ENABLED)
                }
            }
            item.videoItemData.channelThumbnailUri?.let {
                b.ivItemChannel.load(it) {
                    crossfade(true)
                    allowHardware(true)
                }
            }
            item.videoItemData.title?.let { b.tvItemTitle.text = it }
            item.videoItemData.channelName?.let { b.tvItemName.text = it }
            item.videoItemData.views?.let { b.tvItemViews.text = it }
            item.videoItemData.date?.let { b.tvItemDate.text = it }
            item.videoItemData.length?.let { b.tvPopularItemLength.text = it }

            b.root.setOnClickListener {
                Log.d("jj-홈 아이템(비디오) 클릭", "$layoutPosition: $item")
                onVideoClick(item.videoItemData)
            }
        }
    }

    inner class LoadingHolder(private val binding: ItemLoadingProgressBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind() {
//            Log.d("jj-LoadingHolder onBind", "${binding.root}")
            binding.root.isVisible = multiViews.size > 4
        }
    }

    override fun getItemCount(): Int = multiViews.size
    override fun getItemViewType(position: Int): Int = multiViews[position].viewType.ordinal

//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
//        return SealedMulti.Type.values()[viewType].onCreateViewHolder(parent)
//    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (MultiView.Type.values()[viewType]) {
            MultiView.Type.HEADER -> HeaderHolder(
                ItemHeaderBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false)
            )

            MultiView.Type.POPULAR -> PopularHolder(
                HomeItemPopularBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false)
            )

            MultiView.Type.CATEGORY -> CategoryHolder(
                HomeItemCategoryBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false)
            )

            MultiView.Type.VIDEO -> VideoHolder(
                VideoItemBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false)
            )

            MultiView.Type.LOADING -> LoadingHolder(
                ItemLoadingProgressBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false)
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = multiViews[position]) {
            MultiView.Header -> Unit

            is MultiView.Popular -> {
                (holder as PopularHolder).onBind(item)
                // TODO: holder.setIsRecyclable(false) 안쓰면 문제가 있나?
//                holder.setIsRecyclable(false)
            }

            is MultiView.Category -> {
                (holder as CategoryHolder).onBind(item)
//                holder.setIsRecyclable(false)
            }

            is MultiView.Video -> (holder as VideoHolder).onBind(item)

            MultiView.Loading -> (holder as LoadingHolder).onBind()
        }
    }

}
