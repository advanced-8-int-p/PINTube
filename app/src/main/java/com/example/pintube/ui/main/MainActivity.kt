package com.example.pintube.ui.main

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.pintube.R
import com.example.pintube.databinding.ActivityMainBinding
import com.example.pintube.ui.detailpage.DetailFragment
import com.example.pintube.ui.shorts.ShortsActivity
import com.example.pintube.utill.ShareLink
import com.example.pintube.utill.VideoDataInterface
import com.example.pintube.utill.dpToPx
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val navHostFragment by lazy {
        supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
    }

    private val navController by lazy {
        navHostFragment.navController
    }

    private val sharedViewModel: MainSharedViewModel by viewModels()

    private val viewModel: MainViewModel by viewModels()
    private val pinTagDialogSheet by lazy {
        layoutInflater.inflate(R.layout.dialog_select_pin_tag, null)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        initView()
    }

    private fun initView() {
        initBottomNav()
        detailState()
    }

    private var detail: Boolean = false
    fun initDetailFragment(videoId: String) = with(binding) {
        val fragment = DetailFragment()
        fragment.apply {
            arguments = Bundle().apply {
                putString("video_id", videoId)
            }
        }
        supportFragmentManager.beginTransaction()
            .replace(R.id.detail_fragment_activity_main, fragment)
            .commit()

    }


    private fun initBottomNav() = with(binding) {
        navView.setupWithNavController(navController)
        navView.background = null

        mainFabShare.setOnClickListener {
            val currentFragmentInstance =
                supportFragmentManager
                    .findFragmentById(R.id.nav_host_fragment_activity_main)?.childFragmentManager?.fragments?.first() as VideoDataInterface

            val videoUrl = currentFragmentInstance.getVideoUrl()
            ShareLink(this@MainActivity, videoUrl)
            mainMotion.transitionToStart()
        }

        mainFabPin.setOnClickListener {
            val currentFragmentInstance =
                supportFragmentManager
                    .findFragmentById(R.id.nav_host_fragment_activity_main)?.childFragmentManager?.fragments?.first() as VideoDataInterface

            val videoId = currentFragmentInstance.getVideoId()
            viewModel.onClickBookmark(videoId)
            currentFragmentInstance.initData()
            mainMotion.transitionToStart()
        }
    }

    private fun detailState() = with(sharedViewModel) {
        lifecycleScope.launch {
            viewState.collect {
                detail = it
                if (it) {
                    with(binding) {
                        mainFab.setOnClickListener {
                            if (mainMotion.currentState == mainMotion.startState) {
                                mainMotion.transitionToEnd()
                            } else {
                                mainMotion.transitionToStart()
                            }
                        }
                        mainFab.setImageResource(R.drawable.ic_main_fab_plus)
                        mainFab.backgroundTintList =
                            ColorStateList.valueOf(
                                ContextCompat.getColor(
                                    mainFab.context,
                                    R.color.main_color
                                )
                            )
                    }
                } else {
                    with(binding.mainFab) {
                        setImageResource(R.drawable.ic_nav_fab_shorts)
                        backgroundTintList =
                            ColorStateList.valueOf(
                                ContextCompat.getColor(
                                    context,
                                    R.color.white
                                )
                            )
                        setOnClickListener {
                            startActivity(
                                Intent(
                                    this@MainActivity,
                                    ShortsActivity::class.java
                                )
                            )
                        }
                    }
                }
            }
        }
    }

    override fun onBackPressed() {
        if (detail) {
            val detailFragment =
                supportFragmentManager.findFragmentById(R.id.detail_fragment_activity_main)
            detailFragment?.let {
                this.supportFragmentManager.beginTransaction()
                    .remove(it)
                    .commit()
            }
        } else {
            super.onBackPressed()
        }
    }

}