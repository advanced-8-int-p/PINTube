package com.example.pintube.ui.detailpage

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.pintube.R
import com.example.pintube.databinding.FragmentDetailBinding
import com.example.pintube.ui.detailpage.adapter.DetailCommentAdapter
import com.example.pintube.ui.main.MainActivity
import com.example.pintube.ui.main.MainSharedViewModel
import com.example.pintube.ui.main.MotionState
import com.example.pintube.ui.shorts.ShortsActivity
import com.example.pintube.utill.ShareLink
import com.example.pintube.utill.VideoDataInterface
import com.example.pintube.utill.convertToDaysAgo
import com.example.pintube.utill.convertViewCount
import com.example.pintube.utill.getUrlFromSrc
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint

class DetailFragment : Fragment(), VideoDataInterface {

    private var _binding: FragmentDetailBinding? = null

    private val binding get() = _binding!!

    private lateinit var mContext: Context

    private lateinit var playerSrc: String

    private lateinit var videoUrl: String

    private lateinit var videoId: String

    private var isPlaying = false

    private lateinit var viewModel: DetailViewModel

    private val sharedViewModel: MainSharedViewModel by viewModels()

    private val commentAdapter: DetailCommentAdapter by lazy {
        DetailCommentAdapter(
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
        BottomSheetDialog(requireContext())
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(this).get(DetailViewModel::class.java)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        videoUrl = "youtube.com"
        initView()
        initViewModel()

//        Log.d("viewModel", "player bf $videoUrl")
//        getUrlFromSrc()
//        Log.d("viewModel", "player af $videoUrl")
//        initPlayer()


    }

    override fun onDestroy() {
        (activity as MainActivity).detail = false
        sharedViewModel.updateViewState(false)

        super.onDestroy()
        _binding = null
    }

    private fun initView() {
        //뷰 초기화, 받아온 정보 배치
        setCommentSheet()
        setMotion()
        setMotionBtn()
        sharedViewModel.updateViewState(true)
    }

    @SuppressLint("SetTextI18n")
    private fun initViewModel() = viewModel.also { viewModel1 ->
        lateinit var count: String
        viewModel1.media.observe(viewLifecycleOwner, Observer {
            Log.d("viewModel", "init video data $it")
            playerSrc = it.player.toString()
            videoUrl = playerSrc.getUrlFromSrc()
            videoId = it.id.toString()
            Log.d("viewModel", "player af $videoUrl")
            initPlayer()
            with(binding) {
                ivDetailProfilePic.load(it.channelProfile)
                tvDetailChannelName.text = it.channelTitle.toString()
                tvDetailViewCount.text =
                    it.viewCount?.convertViewCount().toString() + " views"
                tvDetailFavCount.text = it.likeCount?.convertViewCount().toString()
                tvDetailTitle.text = it.title.toString()
                tvDetailContent.text = it.publishedAt?.convertToDaysAgo()
                    .toString() + "\n\n\n" + it.description.toString()
                count = it.commentCount?.convertViewCount() ?: "0"
                tvDetailCommentCount.text = "댓글 $count"
                ivPopularItemPin.isVisible = it.isPinned

            }
        })

        viewModel1.comments.observe(viewLifecycleOwner) { item ->
            with(binding) {
                ivDetailCommentFirstProfilePic.load(item.first().userProfileImage)
                tvDetailCommentFirstName.text = item.first().userName
                tvDetailCommentFirst.text = item.first().textOriginal
                tvDetailCommentFirstLikeCount.text =
                    " " + item.first().likeCount.toString().convertViewCount()
                clDetailComment.setOnClickListener {
                    setCommentSheet(count)
                    bottomSheetDialog.show()
                }
            }
            commentAdapter.submitList(item)
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initPlayer() {

        //왜안되지...........으악

        val webView = binding.playerDetail

        webView.settings.javaScriptEnabled = true
        webView.settings.loadWithOverviewMode = true
        webView.settings.useWideViewPort = true
        webView.settings.mediaPlaybackRequiresUserGesture = false
        webView.webChromeClient = WebChromeClient()

        Log.d("viewModel", "play link $videoUrl")
        webView.loadUrl("https://$videoUrl")


    }

    private fun setCommentSheet() = with(binding) {
        bottomSheetDialog.setContentView(commentSheetView)
        commentRecyclerView.adapter = commentAdapter
        commentRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun setCommentSheet(count: String) = with(commentSheetView) {
        findViewById<TextView>(R.id.tv_comment_count).text = count
        findViewById<ImageView>(R.id.iv_comment_close).setOnClickListener {
            bottomSheetDialog.hide()
        }
    }

    private fun onRepliesClick(comments: List<DetailCommentsItem.Comments?>?) = Unit

    @SuppressLint("ClickableViewAccessibility")
    private fun setMotion() = with(binding){
        var initialY = 0f

        playerDetail.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    sharedViewModel.updateMotionState(MotionState.START)
                    initialY = event.y
                    if (detailFragment.currentState == R.id.end) {
                        detailFragment.transitionToStart()
                    }
                }

                MotionEvent.ACTION_MOVE -> {
                    sharedViewModel.updateMotionState(MotionState.MOVE)
                    if (initialY < event.y && detailFragment.currentState == R.id.start) {
                        detailFragment.transitionToEnd()
                    } else if (initialY > event.y && detailFragment.currentState == R.id.end) {
                        detailFragment.transitionToStart()
                    }
                }

                MotionEvent.ACTION_UP -> {
                    sharedViewModel.updateMotionState(MotionState.END)
                    if (detailFragment.currentState == R.id.start) {
                        detailFragment.transitionToEnd()
                    }
                }
            }
            true
        }
    }

    private fun setMotionBtn() = with(binding){
        btnDetailMotionClose.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .remove(this@DetailFragment)
                .commit()
        }
    }

    override fun getVideoUrl(): String {
        return videoUrl
    }

    override fun getVideoId(): String = videoId
    override fun initData() {
        viewModel.initBookmark()
    }

    override fun onResume() {
        val mainFab: FloatingActionButton = activity?.findViewById(R.id.main_fab) ?: return
        val mainMotion: MotionLayout = activity?.findViewById(R.id.main_motion) ?: return
        val mainFabShare: FloatingActionButton = activity?.findViewById(R.id.main_fab_share) ?: return
        val mainFabPin: FloatingActionButton = activity?.findViewById(R.id.main_fab_pin) ?: return
        with(mainFab) {
            setImageResource(R.drawable.ic_main_fab_plus)
            backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.main_color))
            setOnClickListener {
                    if (mainMotion.currentState == mainMotion.startState) {
                        mainMotion.transitionToEnd()
                    } else {
                        mainMotion.transitionToStart()
                    }
                }
        }

        mainFabShare.setOnClickListener {
            ShareLink(requireActivity(), videoUrl)
            mainMotion.transitionToStart()
        }

        mainFabPin.setOnClickListener {
            viewModel.onClickBookmark(videoId)
            mainMotion.transitionToStart()
        }
        super.onResume()

    }

}
