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

sealed interface SealedMulti {

    enum class Type {
        POPULAR,
        CATEGORY,
        VIDEO,
        HEADER,
        LOADING,
    }

    val viewType: Type

    data object Header : SealedMulti {
        override val viewType: Type = Type.HEADER
    }

    data class Popular(
        val videoAdapter: PopularVideoAdapter
    ) : SealedMulti {
        override val viewType: Type = Type.POPULAR
    }

    data class Category(
        val categoryAdapter: CategoryAdapter,
    ) : SealedMulti {
        override val viewType: Type = Type.CATEGORY
    }

    data class Video(
        val videoItemData: VideoItemData,
    ) : SealedMulti {
        override val viewType: Type = Type.VIDEO
    }

    data object Loading : SealedMulti {
        override val viewType: Type = Type.LOADING
    }

}


class HomeAdapter(
    private val onCategorySettingClick: () -> Unit,
    private val onVideoClick: (item: VideoItemData) -> Int?
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var sealedMultis = mutableListOf<SealedMulti>()
    var tvCategoryEmptyText: TextView? = null

    inner class HeaderHolder(binding: ItemHeaderBinding) :
        RecyclerView.ViewHolder(binding.root)

    inner class PopularHolder(private val binding: HomeItemPopularBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(item: SealedMulti.Popular) = with(binding) {
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

        fun onBind(item: SealedMulti.Category) = binding.also { b ->
            b.rvCategoryCategories.adapter = item.categoryAdapter
            b.tvCategoryEmptyText.isVisible = item.categoryAdapter.itemCount == 0
        }
    }

    inner class VideoHolder(private val binding: VideoItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(item: SealedMulti.Video) = binding.also { b ->

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
            binding.root.isVisible = sealedMultis.size > 4
        }
    }

    override fun getItemCount(): Int = sealedMultis.size
    override fun getItemViewType(position: Int): Int = sealedMultis[position].viewType.ordinal

//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
//        return SealedMulti.Type.values()[viewType].onCreateViewHolder(parent)
//    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (SealedMulti.Type.values()[viewType]) {
            SealedMulti.Type.HEADER -> HeaderHolder(
                ItemHeaderBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false)
            )

            SealedMulti.Type.POPULAR -> PopularHolder(
                HomeItemPopularBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false)
            )

            SealedMulti.Type.CATEGORY -> CategoryHolder(
                HomeItemCategoryBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false)
            )

            SealedMulti.Type.VIDEO -> VideoHolder(
                VideoItemBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false)
            )

            SealedMulti.Type.LOADING -> LoadingHolder(
                ItemLoadingProgressBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false)
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = sealedMultis[position]) {
            is SealedMulti.Popular -> {
                (holder as PopularHolder).onBind(item)
                // TODO: holder.setIsRecyclable(false) 안쓰면 문제가 있나?
//                holder.setIsRecyclable(false)
            }

            is SealedMulti.Category -> {
                (holder as CategoryHolder).onBind(item)
//                holder.setIsRecyclable(false)
            }

            SealedMulti.Header -> Unit

            is SealedMulti.Video -> (holder as VideoHolder).onBind(item)

            SealedMulti.Loading -> (holder as LoadingHolder).onBind()
        }
    }

}
