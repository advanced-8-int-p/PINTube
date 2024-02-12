package com.example.pintube.ui.shorts

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.example.pintube.R
import com.example.pintube.databinding.ActivityShortsBinding
import com.example.pintube.ui.Search.SearchActivity
import com.example.pintube.ui.shorts.adapter.CommentAdapter
import com.example.pintube.ui.shorts.adapter.ShortsAdapter
import com.example.pintube.ui.shorts.model.CommentsItem
import com.example.pintube.ui.shorts.model.ShortsItem
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
            onCommentChecked = this::onCommentChecked,
            playerReady = { if (it) adapter.currentViewHolder?.playVideo() }
        )
    }

    private val commentAdapter: CommentAdapter by lazy {
        CommentAdapter(
            onRepliesClick = this::onRepliesClick
        )
    }

    private val commentSheetView by lazy {
        layoutInflater.inflate(R.layout.comments_bottom_sheet, null)
    }

    private val commentRecyclerView by lazy {
        commentSheetView.findViewById<RecyclerView>(R.id.rv_bottom_comment)
    }

    private val bottomSheetDialog by lazy {
        BottomSheetDialog(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        window.statusBarColor = ContextCompat.getColor(this, R.color.black)

        initView()
        initViewModel()
    }

    private fun initView() {
        setPlayer()
        setCommentSheet()
        setButton()
    }

    private fun setButton() = with(binding){
        ivShortsBack.setOnClickListener {
            finish()
        }

        ivShortsSearch.setOnClickListener { 
            startActivity(Intent(this@ShortsActivity,SearchActivity::class.java))
        }
    }

    private fun initViewModel() {
        viewModel.getShortsVideos()

        viewModel.videos.observe(this) {
            adapter.submitList(it)
        }

        viewModel.comments.observe(this) {
            if (it.isEmpty().not()) {
                commentAdapter.submitList(it)
            } else {
                commentAdapter.submitList(listOf(CommentsItem.NoComments))
            }
        }
    }

    private fun setPlayer() = with(binding) {
        vpShortsViewpager.adapter = adapter
        vpShortsViewpager.orientation = ViewPager2.ORIENTATION_VERTICAL

        vpShortsViewpager.offscreenPageLimit = 2
    }

    private fun setCommentSheet() = with(binding){
        bottomSheetDialog.setContentView(commentSheetView)
        commentRecyclerView.adapter = commentAdapter
        commentRecyclerView.layoutManager = LinearLayoutManager(this@ShortsActivity)
    }

    private fun setCommentSheet(count: String) = with(commentSheetView) {
        findViewById<TextView>(R.id.tv_comment_count).text = count
        findViewById<ImageView>(R.id.iv_comment_close).setOnClickListener {
            bottomSheetDialog.hide()
        }
    }

    private fun onCommentChecked(item: ShortsItem.Item) {
        item.id?.let { viewModel.getComments(it) }
        item.commentCount?.let { setCommentSheet(it) }
        bottomSheetDialog.show()
    }

    /*
    * Todo 대댓글 클릭시 화면 전환후 보여주기
    * */
    private fun onRepliesClick(parentId: String?) = Unit

    override fun onPause() {
        window.statusBarColor = ContextCompat.getColor(this, R.color.main_color)
        super.onPause()
    }
}
