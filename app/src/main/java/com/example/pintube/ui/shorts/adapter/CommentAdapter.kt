package com.example.pintube.ui.shorts.adapter

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
import com.example.pintube.ui.shorts.model.CommentsItem
import com.example.pintube.ui.shorts.model.CommentsViewType
import com.example.pintube.utill.convertViewCount

class CommentAdapter(
    private val onRepliesClick: (parentId: String?) -> Unit,
) : ListAdapter<CommentsItem, CommentAdapter.CommentsViewHolder>(

    object : DiffUtil.ItemCallback<CommentsItem>() {
        override fun areItemsTheSame(
            oldItem: CommentsItem,
            newItem: CommentsItem
        ): Boolean = if (oldItem is CommentsItem.Comments && newItem is CommentsItem.Comments) {
            oldItem.id == newItem.id
        } else {
            oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: CommentsItem,
            newItem: CommentsItem
        ): Boolean = oldItem == newItem

    }
) {
    abstract class CommentsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        abstract fun onBind(item: CommentsItem)
    }

    override fun onViewRecycled(holder: CommentsViewHolder) {
        super.onViewRecycled(holder)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentsViewHolder = when(CommentsViewType.from(viewType)) {
            CommentsViewType.ITEM -> CommentsItemViewHolder(
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

    class CommentsItemViewHolder (
        private val binding: ItemCommentBinding,
        private val onRepliesClick: (parentId: String?) -> Unit,
    ) : CommentsViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        override fun onBind(item: CommentsItem) = with(binding){
            if (item !is CommentsItem.Comments) {
                return@with
            }
            ivCommentUserprofile.load(item.userProfileImage){
                crossfade(true)
            }
            tvCommentUsername.text = item.userName
            tvCommentDaysAgo.text = " · " + item.publishedAt
            tvCommentDesc.text = item.textOriginal
            tvCommentLikeCount.text = item.likeCount?.toString()?.convertViewCount()
            tvCommentLikeCount.isVisible = (item.likeCount ?: 0) > 0

            if (item.totalReplyCount!! > 0) {
                tvCommentRepliesBtn.isVisible = true
                tvCommentReplies.text = "답글 " + item.totalReplyCount + " 개"
            }
            tvCommentRepliesBtn.setOnClickListener {
                onRepliesClick(item.id)
            }
        }
    }

    class NoCommentsViewHolder (
        private val binding: ItemCommentEmptyBinding,
    ) : CommentsViewHolder(binding.root) {
        override fun onBind(item: CommentsItem) = Unit
    }

    class CommentsUnknownViewHolder(
        binding: UnknownItemBinding
    ) : CommentsViewHolder(binding.root) {
        override fun onBind(item: CommentsItem) = Unit
    }
}
