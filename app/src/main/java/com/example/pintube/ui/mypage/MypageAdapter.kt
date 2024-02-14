package com.example.pintube.ui.mypage

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.fragment.FragmentNavigator
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.pintube.R
import com.example.pintube.databinding.ItemMypageHeaderBinding
import com.example.pintube.databinding.RecyclerviewPinnedGroupBinding
import com.example.pintube.databinding.RecyclerviewRecentVideosBinding
import com.example.pintube.ui.main.MainActivity

class MypageAdapter(
    private val mContext: Context,
    private val multiViewType: MutableList<MypageViewType>
) : RecyclerView.Adapter<ViewHolder>() {


//    var multiViewType = mutableListOf<MypageViewType>()

    companion object {
        private const val VIEW_TYPE_TITLE = 1
        private const val VIEW_TYPE_RECENT = 2
        private const val VIEW_TYPE_PINNED = 3
    }

    interface ItemClick {
        fun onClick(view: View, position: Int, id: String)
    }

    var itemClick: ItemClick? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {

            VIEW_TYPE_TITLE -> {
                val binding =
                    ItemMypageHeaderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                MypageHeaderViewHolder(binding)
            }

            VIEW_TYPE_RECENT -> {
                val binding = RecyclerviewRecentVideosBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                MypageRecentItemViewHolder(binding)
            }

            else -> {
                val binding = RecyclerviewPinnedGroupBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                MypagePinItemViewHolder(binding)
            }
        }
    }


    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (val items = multiViewType[position]) {

            is MypageViewType.Header -> {
                (holder as MypageHeaderViewHolder).onBind(items)
            }

            is MypageViewType.RecentItems -> {
                (holder as MypageRecentItemViewHolder).onBind(items)
            }

            is MypageViewType.PinItems -> {
                val adapter = items.pinAdapter
                (holder as MypagePinItemViewHolder).onBind(items)
            }
        }
    }

    override fun getItemCount(): Int {
        return multiViewType.size
    }

    override fun getItemViewType(position: Int): Int {
        return when (multiViewType[position]) {
            is MypageViewType.Header -> VIEW_TYPE_TITLE
            is MypageViewType.RecentItems -> VIEW_TYPE_RECENT
            is MypageViewType.PinItems -> VIEW_TYPE_PINNED
        }

    }

    inner class MypageHeaderViewHolder(private val binding: ItemMypageHeaderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(item: MypageViewType.Header) {
            binding.tvMyHeaderTitle.text = item.title
            binding.tvMyHeaderBtn.isVisible = item.isRecent
            binding.ivMyHeaderIcon.isVisible = !item.isRecent
            binding.tvMyHeaderBtn.setOnClickListener {
                val fragment = MoreRecentVideosFragment()
                (mContext as MainActivity).supportFragmentManager.beginTransaction()
                    .replace(R.id.mypage_fragment, fragment)
                    .addToBackStack("")
                    .commit()
            }
        }
    }

    inner class MypageRecentItemViewHolder(private val binding: RecyclerviewRecentVideosBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(item: MypageViewType.RecentItems) {
            val adapter = item.recentAdapter
            binding.rvRecyclerviewRecent.adapter = adapter
        }

    }

    inner class MypagePinItemViewHolder(private val binding: RecyclerviewPinnedGroupBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(item: MypageViewType.PinItems) {
            val adapter = item.pinAdapter
            binding.rvRecyclerviewPinned.adapter = adapter
        }

    }

}