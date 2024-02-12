package com.example.pintube.ui.detailpage.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.pintube.databinding.ItemCommentBinding
import com.example.pintube.databinding.ItemCommentEmptyBinding
import com.example.pintube.databinding.UnknownItemBinding
import com.example.pintube.ui.detailpage.DetailCommentsItem
import com.example.pintube.ui.shorts.model.CommentsViewType
import com.example.pintube.utill.convertViewCount

class DetailCommentAdapter(
    private val onRepliesClick: (replies: List<DetailCommentsItem.Comments?>?) -> Unit,
) : ListAdapter<DetailCommentsItem, DetailCommentAdapter.CommentsViewHolder>(

    object : DiffUtil.ItemCallback<DetailCommentsItem>() {
        override fun areItemsTheSame(
            oldItem: DetailCommentsItem,
            newItem: DetailCommentsItem
        ): Boolean = if (oldItem is DetailCommentsItem.Comments && newItem is DetailCommentsItem.Comments) {
            oldItem.id == newItem.id
        } else {
            oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: DetailCommentsItem,
            newItem: DetailCommentsItem
        ): Boolean = oldItem == newItem

    }
) {
    abstract class CommentsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        abstract fun onBind(item: DetailCommentsItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentsViewHolder = when(CommentsViewType.from(viewType)) {
            CommentsViewType.ITEM -> DetailCommentsItemViewHolder(
                binding = ItemCommentBinding.inflate(LayoutInflater.from(parent.context),parent,false),
                onRepliesClick = onRepliesClick
            )

            CommentsViewType.NO_ITEM -> NoCommentsViewHolder(
                binding = ItemCommentEmptyBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            )

            else -> CommentsUnknownViewHolder(
                binding = UnknownItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
        }


    override fun onBindViewHolder(holder: CommentsViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }

    class DetailCommentsItemViewHolder (
        private val binding: ItemCommentBinding,
        private val onRepliesClick: (replies: List<DetailCommentsItem.Comments?>?) -> Unit,
    ) : CommentsViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        override fun onBind(item: DetailCommentsItem) = with(binding){
            if (item !is DetailCommentsItem.Comments) {
                return@with
            }
            ivCommentUserprofile.load(item.userProfileImage){
                crossfade(true)
            }
            tvCommentUsername.text = item.userName
            tvCommentDaysAgo.text = " · " + item.publishedAt
            tvCommentDesc.text = item.textOriginal
            tvCommentLikeCount.text = item.likeCount?.toString()?.convertViewCount()
            if (item.likeCount == 0){
                tvCommentLikeCount.isVisible = false
            }

            if (item.totalReplyCount!! > 0) {
                tvCommentRepliesBtn.isVisible = true
                tvCommentReplies.text = "답글 " + item.totalReplyCount + " 개"
            }
            tvCommentRepliesBtn.setOnClickListener {
                onRepliesClick(item.replies)
            }
        }
    }

    class NoCommentsViewHolder (
        private val binding: ItemCommentEmptyBinding,
    ) : CommentsViewHolder(binding.root) {
        override fun onBind(item: DetailCommentsItem) = Unit
    }

    class CommentsUnknownViewHolder(
        binding: UnknownItemBinding
    ) : CommentsViewHolder(binding.root) {
        override fun onBind(item: DetailCommentsItem) = Unit
    }
}
