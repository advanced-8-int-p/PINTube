package com.example.pintube.ui.detailpage

import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import com.example.pintube.ui.MainActivity
import com.example.pintube.databinding.FragmentDetailBinding
import com.example.pintube.ui.home.HomeViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint

class DetailFragment : Fragment() {

    private val binding by lazy { FragmentDetailBinding.inflate(layoutInflater) }

    private lateinit var player: ExoPlayer
    private lateinit var mediaSession: MediaSession

    private val videoSample =
        "https://storage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"

    private val viewModel: HomeViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initPlayer()
        initSession()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
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
                    PendingIntent.FLAG_IMMUTABLE
                )
            )
            .build()
    }

}