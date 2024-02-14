package com.example.pintube.ui.mypage

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import coil.load
import com.example.pintube.R
import com.example.pintube.databinding.VideoItemBinding
import com.example.pintube.ui.detailpage.DetailFragment

class MoreRecentVideoAdapter(private val mContext: Context, private val mItems: MutableList<RecentItem>): RecyclerView.Adapter<ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = VideoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun getItemCount(): Int = mItems.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        (holder as ItemViewHolder).thumbnail.load(mItems[position].thumbnailUrl)
        holder.thumbnail.setOnClickListener {
            val fragment = DetailFragment()
            (mContext as AppCompatActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment_activity_main, fragment)
                .addToBackStack("")
                .commit()
        }
    }

    inner class ItemViewHolder(binding: VideoItemBinding): RecyclerView.ViewHolder(binding.root) {
        val thumbnail = binding.ivItemVideo
    }

}
