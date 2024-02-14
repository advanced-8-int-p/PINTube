package com.example.pintube.ui.mypage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import coil.load
import com.example.pintube.data.local.entity.LocalVideoEntity
import com.example.pintube.data.repository.local.LocalFavoriteRepositoryImpl
import com.example.pintube.databinding.VideoItemBinding
import com.example.pintube.domain.repository.LocalFavoriteRepository
import com.example.pintube.domain.repository.LocalVideoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlin.coroutines.suspendCoroutine

class RecyclerviewPinnedGroupAdapter(
    private var idList: List<LocalVideoEntity>,
//    private var localVideoRepository: LocalVideoRepository
) : RecyclerView.Adapter<ViewHolder>() {

    private val mItems : MutableList<VideoItem> = mutableListOf()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = VideoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun getItemCount(): Int = mItems.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        (holder as ItemViewHolder).thumbnail.load(mItems[position].thumbnailUrl)
        holder.title.text = mItems[position].title
        holder.channelProfile.load(mItems[position].thumbnailUrl)
        holder.channelName.text = mItems[position].channelName
    }

//    private fun getData() = viewModelScope.launch(Dispatchers.IO) {
//        val result = videoId.let { localVideoRepository.findVideoDetail(it) }
//        result?.let { getComment(id = it.id) }
//
//        _media.postValue(result?.convertToDetailItem())
//    }

    private fun getFavoriteVideo(position: Int) {

        idList[position]
    }

    inner class ItemViewHolder(binding: VideoItemBinding): RecyclerView.ViewHolder(binding.root) {

        val thumbnail = binding.ivItemVideo
        val title = binding.tvItemTitle
        val channelProfile = binding.ivItemChannel
        val channelName = binding.tvItemName
    }


}
