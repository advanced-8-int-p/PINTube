package com.example.pintube.ui.detailpage

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import coil.load
import com.example.pintube.databinding.FragmentDetailBinding
import com.example.pintube.utill.convertToDaysAgo
import com.example.pintube.utill.convertViewCount
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint

class DetailFragment : Fragment() {

    private var _binding : FragmentDetailBinding? = null

    private val binding get() = _binding!!

    private lateinit var mContext: Context

    private var tempMediaId: String = "rkuE-ygaSgQ"

    private lateinit var playerSrc: String

    private lateinit var videoUrl: String

    private var isPlaying = false

    private lateinit var viewModel: DetailViewModel

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

        initViewModel()

//        Log.d("viewModel", "player bf $videoUrl")
//        getUrlFromSrc()
//        Log.d("viewModel", "player af $videoUrl")
//        initPlayer()

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

    @SuppressLint("SetTextI18n")
    private fun initViewModel() = viewModel.also { viewModel1 ->
        viewModel1.getData(id = tempMediaId)
        viewModel1.media.observe(viewLifecycleOwner, Observer {
            Log.d("viewModel", "init video data $it")
            playerSrc = it.player.toString()
            videoUrl = getUrlFromSrc(playerSrc)
            Log.d("viewModel", "player af $videoUrl")
            initPlayer()
            binding.ivDetailProfilePic.load(it.channelProfile)
            binding.tvDetailChannelName.text = it.channelTitle.toString()
            binding.tvDetailViewCount.text = it.viewCount?.convertViewCount().toString() + " views"
            binding.tvDetailFavCount.text = it.likeCount?.convertViewCount().toString()
            binding.tvDetailTitle.text = it.title.toString()
            binding.tvDetailContent.text = it.publishedAt?.convertToDaysAgo().toString() + "\n\n\n" + it.description.toString()
            binding.tvDetailCommentCount.text = "댓글 " + it.commentCount.toString()
        })
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

    private fun getUrlFromSrc(src: String): String {
        val urlRegex = """src="//([^"]*)"""".toRegex()

        val matchResult = urlRegex.find(src)

        return matchResult?.groups?.get(1)?.value.toString()
    }



    private fun shareLink() {
        val intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, "https:$videoUrl")
            type = "text/plain"
        }
        val shareChooser = Intent.createChooser(intent, null)
        startActivity(shareChooser)
    }

}