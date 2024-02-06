package com.example.pintube.ui.detailpage

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.pintube.MainActivity
import com.example.pintube.R
import com.example.pintube.databinding.FragmentDetailpageBinding
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import com.google.android.material.snackbar.Snackbar

class DetailpageFragment : Fragment() {

    private val binding by lazy { FragmentDetailpageBinding.inflate(layoutInflater) }

    private lateinit var player: ExoPlayer
    private lateinit var mediaSession: MediaSession

    private val videoSample =
        "https://storage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"

//    companion object {
//        fun newInstance() = DetailpageFragment()
//    }

//    private lateinit var viewModel: DetailpageViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_detailpage, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initPlayer()
        initSession()
//        viewModel = ViewModelProvider(this).get(DetailpageViewModel::class.java)
//        // TODO: Use the ViewModel
    }

    override fun onStart() {
        super.onStart()
        player.playWhenReady = true
    }

    override fun onStop() {
        super.onStop()
        player.playWhenReady = false
    }

    override fun onDestroy() {
        super.onDestroy()
        player.release()
    }

    private fun initPlayer() {
        val mediaItem = MediaItem.fromUri(videoSample)
        player = ExoPlayer.Builder(requireContext())
            .build()
            .also { exoPlayer ->
                binding.playerDetail.player = exoPlayer
            }
        player.setMediaItem(mediaItem)
        player.prepare()
        player.play()

        player.addListener(object : Player.Listener{
            override fun onPlayerError(error: PlaybackException) {
                Snackbar.make(binding.detailFragment, "error", Snackbar.LENGTH_SHORT).show()
                Log.d("player", "error = ${error}")
                super.onPlayerError(error)
            }
        })

    }

    private fun initSession() {
        mediaSession = MediaSession.Builder(requireContext(), player)
            .setSessionActivity(
                PendingIntent.getActivity(
                    requireContext(),
                    0,
                    Intent(requireContext(), MainActivity::class.java),
                    0
                )
            )
            .build()
    }

}


//class VideoPlayerService : Service() {
//    override fun onBind(intent: Intent?): IBinder? {
//        TODO("Not yet implemented")
//    }
//
//}