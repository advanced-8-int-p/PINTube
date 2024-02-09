package com.example.pintube.ui.shorts.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.pintube.databinding.ItemCommentBinding
import com.example.pintube.databinding.UnknownItemBinding
import com.example.pintube.ui.shorts.model.CommentsItem
import com.example.pintube.ui.shorts.model.CommentsViewType
import com.example.pintube.utill.convertViewCount
import org.jsoup.Jsoup

class CommentAdapter(
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
                binding = ItemCommentBinding.inflate(LayoutInflater.from(parent.context),parent,false)
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
    ) : CommentsViewHolder(binding.root) {
        override fun onBind(item: CommentsItem) = with(binding){
            if (item !is CommentsItem.Comments) {
                return@with
            }
            ivCommentUserprofile.load(item.userProfileImage)
            tvCommentUsername.text = item.userName
            tvCommentDesc.text = item.textOriginal
            tvCommentLikeCount.text = item.likeCount?.convertViewCount()

        }
    }

    class CommentsUnknownViewHolder(
        binding: UnknownItemBinding
    ) : CommentsViewHolder(binding.root) {
        override fun onBind(item: CommentsItem) = Unit
    }
}
