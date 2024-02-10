package com.example.pintube.ui.detailpage

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.JavascriptInterface
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.pintube.data.remote.api.retrofit.YouTubeApi
import com.example.pintube.data.remote.dto.ApiResponse
import com.example.pintube.data.repository.ApiRepositoryImpl
import com.example.pintube.databinding.FragmentDetailBinding
import com.example.pintube.domain.repository.ApiRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint

class DetailFragment : Fragment() {

    private var _binding : FragmentDetailBinding? = null

    private val binding get() = _binding!!

    private var tempMediaId: String = "rkuE-ygaSgQ"
    private lateinit var mediaItemData: DetailItemModel

    private lateinit var mContext: Context
//    private lateinit var commentAdapter: CommentAdapter


    private var videoSample = "https://www.youtube.com/embed/IunP_b5FfhY"

    private var isPlaying = false

    private val viewModel: DetailViewModel by viewModels()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initPlayer()

        binding.ivDetailClose.setOnClickListener {
//            parentFragmentManager.popBackStack()
            findNavController().navigateUp()
        }
        binding.ivDetailShare.setOnClickListener {
            shareLink()
        }
        binding.ivDetailPin.setOnClickListener {
            //보관함 저장
        }
        binding.playerDetail.setOnClickListener {
            isPlaying = !isPlaying
            binding.clDetailTopBar.isVisible = isPlaying
        }

    }

    override fun onStart() {
        super.onStart()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun initView() {
        //뷰 초기화, 받아온 정보 배치
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initPlayer() {
//        val mediaItem = MediaItem.fromUri(videoSample)

        val webView = binding.playerDetail

        webView.settings.javaScriptEnabled = true
        webView.addJavascriptInterface(YoutubeInterface(), "Android")
        webView.loadUrl(videoSample)

//        val videoUrl = DefaultMediaSourceFactory(mContext)
//

    }

    private inner class YoutubeInterface() {

        @JavascriptInterface
        fun onStateChanged(state: Int) {
            when(state) {
                0 -> Log.d("YouTube", "Ended")
                1 -> Log.d("YouTube", "Playing")
                2 -> Log.d("YouTube", "Paused")
                3 -> Log.d("YouTube", "Buffering")
            }
        }
    }


    private fun getData(id: String?) = lifecycleScope.launch {
//        mediaId = //id값 받아와서 그 값으로 검색?해서 해당 영상 정보 가져오기 enquedhodksehlwl...
        YouTubeApi.youtubeNetwork.getContentDetails(ids = listOf(tempMediaId))
//        ApiRepositoryImpl().getContentDetails(listOf(tempMediaId))?.forEach {
//            videoSample = it.player.toString()
//        }
        val detailData = ApiRepository
        mediaItemData.id
        mediaItemData.player
        mediaItemData.channelProfile
        mediaItemData.channelTitle
        mediaItemData.viewCount
        mediaItemData.likeCount
        mediaItemData.title
        mediaItemData.publishedAt
        mediaItemData.description
        mediaItemData.commentCount
        mediaItemData.isPinned = false
    }

    private fun shareLink() {
        val intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, "link")
            type = "text/plain"
        }
        val shareChooser = Intent.createChooser(intent, null)
        startActivity(shareChooser)
    }

}