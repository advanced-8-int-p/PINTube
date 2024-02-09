package com.example.pintube.ui.shorts

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.pintube.R
import com.example.pintube.databinding.ActivityShortsBinding
import com.example.pintube.ui.shorts.adapter.CommentAdapter
import com.example.pintube.ui.shorts.adapter.ShortsAdapter
import com.example.pintube.ui.shorts.model.ShortsViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ShortsActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityShortsBinding.inflate(layoutInflater)
    }

    private val viewModel: ShortsViewModel by viewModels()

    private val adapter: ShortsAdapter by lazy {
        ShortsAdapter(
            onBookmarkChecked = { item ->
                item
            },
            onSharedChecked = { item ->
                item
            },
            onCommentChecked = this::onCommentChecked
        )
    }

    private val commentAdapter: CommentAdapter by lazy {
        CommentAdapter()
    }

    private val commentSheetView by lazy {
        layoutInflater.inflate(R.layout.comments_bottom_sheet, null)
    }

    private val bottomSheetDialog by lazy {
        BottomSheetDialog(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initView()
        initViewModel()
    }

    private fun initView() {
        setPlayer()
        setCommentSheet()
    }

    private fun initViewModel() {
        viewModel.getShortsVideos()

        viewModel.videos.observe(this) {
            adapter.submitList(it)
        }

        viewModel.comments.observe(this) {
            commentAdapter.submitList(it)
        }
    }

    private fun setPlayer() = with(binding) {
        vpShortsViewpager.adapter = adapter
        vpShortsViewpager.orientation = ViewPager2.ORIENTATION_VERTICAL
    }

    private fun setCommentSheet() = with(binding){
        bottomSheetDialog.setContentView(commentSheetView)
        val commentsRv = commentSheetView.findViewById<RecyclerView>(R.id.rv_bottom_comment)
        commentsRv.adapter = commentAdapter
        commentsRv.layoutManager = LinearLayoutManager(this@ShortsActivity)
    }

    private fun onCommentChecked(id: String) {
        viewModel.getComments(id)
        bottomSheetDialog.show()
    }
}
