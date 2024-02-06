package com.example.pintube.ui.home

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pintube.R

class HomeAdapter(private val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var datas = mutableListOf<MultiData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View?
        return when (viewType) {
            MULTI_POPULAR -> {
                view = LayoutInflater.from(parent.context).inflate(
                    R.layout.home_item_popular,
                    parent,
                    false
                )
                MultiViewHolderPopular(view)
            }

            else -> {
                view = LayoutInflater.from(parent.context).inflate(
                    R.layout.home_item_category,
                    parent,
                    false
                )
                MultiViewHolderCategory(view)
            }
        }
    }

    override fun getItemCount(): Int = datas.size

    override fun getItemViewType(position: Int): Int {
        return datas[position].type
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (datas[position].type) {
            MULTI_POPULAR -> {
                (holder as MultiViewHolderPopular).onBind(datas[position])
                holder.setIsRecyclable(false)
            }

            else -> {
                (holder as MultiViewHolderCategory).onBind(datas[position])
                holder.setIsRecyclable(false)
            }
        }
    }

    inner class MultiViewHolderPopular(private val view: View) :
        RecyclerView.ViewHolder(view) {

        fun onBind(item: MultiData) {
            Log.d("myTag:멀티인기뷰홀더 onBind", item.toString())  //ddd
            // TODO: 어댑터를 파라미터로 받아야하나
            val adapter = PopularAdapter { _, _ -> }
            view.findViewById<RecyclerView>(R.id.rv_popular_videos).adapter =
                adapter
            //ddd
            adapter.items.addAll(List(10) { VideoItemData() })
        }
    }

    inner class MultiViewHolderCategory(private val view: View) :
        RecyclerView.ViewHolder(view) {

        fun onBind(item: MultiData) {
            Log.d("myTag:멀티카테고리뷰홀더 onBind", item.toString())  //ddd
            // TODO: 어댑터를 파라미터로 받아야하나
            val categoryAdapter = CategoryAdapter { _, _ -> }
            view.findViewById<RecyclerView>(R.id.rv_category_categories).adapter =
                categoryAdapter
            //ddd
            categoryAdapter.items.addAll(List(10) { "카테고리$it" })

            val categoryVideoAdapter = CategoryVideoAdapter { _, _ -> }
            view.findViewById<RecyclerView>(R.id.rv_category_videos).adapter =
                categoryVideoAdapter
            //ddd
            categoryVideoAdapter.items.addAll(List(10) { VideoItemData() })
        }
    }

//    inner class MultiViewHolder3(view: View) : RecyclerView.ViewHolder(view) {
//
//        private val txtName: TextView = view.findViewById(R.id.tv_rv_name)
//        private val imgProfile: ImageView = view.findViewById(R.id.img_rv_photo)
//
//        fun bind(item: MultiData) {
//            txtName.text = item.name
//            Glide.with(itemView).load(item.image).into(imgProfile)
//
//        }
//    }
}
