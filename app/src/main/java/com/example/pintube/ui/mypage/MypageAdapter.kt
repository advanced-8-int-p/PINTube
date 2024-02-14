package com.example.pintube.ui.mypage

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import coil.load
import com.example.pintube.R
import com.example.pintube.databinding.ItemHeaderBinding
import com.example.pintube.databinding.ItemMypageHeaderBinding
import com.example.pintube.databinding.ItemMypageProfileBinding
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
        private const val VIEW_TYPE_TOP_HEADER = 4
        private const val VIEW_TYPE_PROFILE = 5
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

            VIEW_TYPE_PINNED -> {
                val binding = RecyclerviewPinnedGroupBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                MypagePinItemViewHolder(binding)
            }

            VIEW_TYPE_TOP_HEADER -> TopHeaderViewHolder(
                ItemHeaderBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false)
            )

            VIEW_TYPE_PROFILE -> MypageProfileViewHolder(
                ItemMypageProfileBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false)
            )

            else -> error("jj-마이페이지어댑터 onCreateViewHolder - viewType error: $viewType")
        }
    }


    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (val item = multiViewType[position]) {

            is MypageViewType.Header -> {
                (holder as MypageHeaderViewHolder).onBind(item)
            }

            is MypageViewType.RecentItems -> {
                (holder as MypageRecentItemViewHolder).onBind(item)
            }

            is MypageViewType.PinItems -> {
                (holder as MypagePinItemViewHolder).onBind(item)
            }

            MypageViewType.TopHeader -> Unit

            is MypageViewType.MyPageProfile -> (holder as MypageProfileViewHolder).onBind(item)
        }
    }

    override fun getItemCount(): Int = multiViewType.size

    override fun getItemViewType(position: Int): Int {
        return when (multiViewType[position]) {
            is MypageViewType.Header -> VIEW_TYPE_TITLE
            is MypageViewType.RecentItems -> VIEW_TYPE_RECENT
            is MypageViewType.PinItems -> VIEW_TYPE_PINNED
            MypageViewType.TopHeader -> VIEW_TYPE_TOP_HEADER
            is MypageViewType.MyPageProfile -> VIEW_TYPE_PROFILE
        }
    }

    inner class TopHeaderViewHolder(binding: ItemHeaderBinding) :
        RecyclerView.ViewHolder(binding.root)

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

    inner class MypageProfileViewHolder(private val binding: ItemMypageProfileBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(item: MypageViewType.MyPageProfile) = binding.also { b ->
            b.ivMypageProfile.load(item.channelThumbnail)
            b.tvMypageChannelName.text = item.channelName
            b.tvMypageChannelId.text = item.channelId
            // TODO: 로그인 여부에 따라서 버튼을 로그인/로그아웃 변경..?
        }
    }

}
