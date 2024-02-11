package com.example.pintube.ui.shorts.adapter

import android.content.Context
import android.graphics.Color
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.view.marginBottom
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.ListAdapter
import coil.load
import com.example.pintube.databinding.ItemShortsBinding
import com.example.pintube.databinding.UnknownItemBinding
import com.example.pintube.ui.shorts.model.ShortsViewType
import com.example.pintube.ui.shorts.model.ShortsItem
import com.example.pintube.utill.dpToPx
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener

class ShortsAdapter(
    private val onBookmarkChecked: (ShortsItem) -> Unit,
    private val onSharedChecked: (ShortsItem) -> Unit,
    private val onCommentChecked: (ShortsItem.Item) -> Unit,
) : ListAdapter<ShortsItem, ShortsAdapter.ShortsViewHolder>(

    object : DiffUtil.ItemCallback<ShortsItem>() {
        override fun areItemsTheSame(
            oldItem: ShortsItem,
            newItem: ShortsItem
        ): Boolean = if (oldItem is ShortsItem.Item && newItem is ShortsItem.Item) {
            oldItem.id == newItem.id
        } else {
            oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: ShortsItem,
            newItem: ShortsItem
        ): Boolean = oldItem == newItem

    }
) {

    abstract class ShortsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        abstract fun onBind(item: ShortsItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShortsViewHolder =
        when(ShortsViewType.from(viewType)) {
            ShortsViewType.ITEM -> ShortsItemViewHolder(
                binding = ItemShortsBinding.inflate(LayoutInflater.from(parent.context), parent, false),
                onBookmarkChecked = onBookmarkChecked,
                onSharedChecked = onSharedChecked,
                onCommentChecked = onCommentChecked,
            )

            else -> ShortsUnknownViewHolder(
                UnknownItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
        }

    override fun onBindViewHolder(holder: ShortsViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }

    class ShortsItemViewHolder (
        private val binding: ItemShortsBinding,
        private val onBookmarkChecked: (ShortsItem) -> Unit,
        private val onSharedChecked: (ShortsItem) -> Unit,
        private val onCommentChecked: (ShortsItem.Item) -> Unit,
    ) : ShortsViewHolder(binding.root) {
        override fun onBind(item: ShortsItem) = with(binding) {
            if (item !is ShortsItem.Item) {
                return@with
            }
            tvShortsCommentCount.text = item.commentCount
            tvShortsTitle.text = item.title
            tvShortsUser.text = item.channelTitle
            ivShortsImage.load(item.channelThumbnail)
            ivShortsComments.setOnClickListener {
                onCommentChecked(item)
            }

            val windowManager = itemView.context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val displayMetrics = DisplayMetrics()
            windowManager.defaultDisplay.getMetrics(displayMetrics)
            val height = displayMetrics.heightPixels
            val width = displayMetrics.widthPixels
            val layoutParams = vvShortsVideo.layoutParams
            val extraHeight = 80.dpToPx(vvShortsVideo.context)

            layoutParams.height = layoutParams.height + extraHeight + height
            layoutParams.width = width
            vvShortsVideo.layoutParams = layoutParams
            vvShortsVideo.setBackgroundColor(Color.parseColor("#000000"))

            vvShortsVideo.addYouTubePlayerListener(
                object : AbstractYouTubePlayerListener() {
                    override fun onReady(youTubePlayer: YouTubePlayer) {
                        item.id?.let { youTubePlayer.loadVideo(it, 0f) }
                    }

                    override fun onStateChange(
                        youTubePlayer: YouTubePlayer,
                        state: PlayerConstants.PlayerState
                    ) {
                        if (state == PlayerConstants.PlayerState.ENDED) {
                            youTubePlayer.seekTo(0f)
                            youTubePlayer.play()
                        }
                    }
                }
            )
        }

    }

    class ShortsUnknownViewHolder(
        private val binding: UnknownItemBinding,
    ): ShortsViewHolder(binding.root) {
        override fun onBind(item: ShortsItem) = Unit
    }

}
