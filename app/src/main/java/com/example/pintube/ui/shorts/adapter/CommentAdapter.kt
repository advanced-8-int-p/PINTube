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
import com.example.pintube.databinding.UnknownItemBinding
import com.example.pintube.ui.shorts.model.CommentsItem
import com.example.pintube.ui.shorts.model.CommentsViewType
import com.example.pintube.utill.convertToDaysAgo
import com.example.pintube.utill.convertViewCount
import org.jsoup.Jsoup

class CommentAdapter(
    private val onRepliesClick: (replies: List<CommentsItem.Comments?>?) -> Unit,
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentsViewHolder {
        return when(CommentsViewType.from(viewType)) {
            CommentsViewType.ITEM -> CommentsItemAdapter(
                binding = ItemCommentBinding.inflate(LayoutInflater.from(parent.context),parent,false),
                onRepliesClick = onRepliesClick
            )

            else -> CommentsUnknownViewHolder(
                binding = UnknownItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
        }
    }

    override fun onBindViewHolder(holder: CommentsViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }

    class CommentsItemAdapter (
        private val binding: ItemCommentBinding,
        private val onRepliesClick: (replies: List<CommentsItem.Comments?>?) -> Unit,
    ) : CommentsViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        override fun onBind(item: CommentsItem) = with(binding){
            if (item !is CommentsItem.Comments) {
                return@with
            }
            ivCommentUserprofile.load(item.userProfileImage)
            tvCommentUsername.text = item.userName
            tvCommentDaysAgo.text = " · " + item.publishedAt
            tvCommentDesc.text = item.textOriginal
            tvCommentLikeCount.text = item.likeCount?.toString()?.convertViewCount()
            if (item.likeCount == 0){
                tvCommentLikeCount.isVisible = false
            }

            if (item.totalReplyCount!! > 0) {
                tvCommentReplies.isVisible = true
                tvCommentReplies.text = "답글 " + item.totalReplyCount + " 개"
            }
            tvCommentReplies.setOnClickListener {
                onRepliesClick(item.replies)
            }
        }
    }

    class CommentsUnknownViewHolder(
        binding: UnknownItemBinding
    ) : CommentsViewHolder(binding.root) {
        override fun onBind(item: CommentsItem) = Unit
    }
}
