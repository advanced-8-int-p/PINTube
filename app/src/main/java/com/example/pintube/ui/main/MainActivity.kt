package com.example.pintube.ui.main

import android.app.ActionBar.LayoutParams
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.marginBottom
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.pintube.R
import com.example.pintube.databinding.ActivityMainBinding
import com.example.pintube.ui.mypage.MypageViewType
import com.example.pintube.ui.detailpage.DetailFragment
import com.example.pintube.ui.shorts.ShortsActivity
import com.example.pintube.utill.ShareLink
import com.example.pintube.utill.VideoDataInterface
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
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

    var recentItemsList = mutableListOf<String>()

    private val viewModel: MainViewModel by viewModels()

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

    var detail: Boolean = false
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
        sharedViewModel.motionState.observe(this@MainActivity) { stats ->
            when (stats) {
                MotionState.START -> {
                }

                MotionState.MOVE -> {
                    val params =
                        detailFragmentActivityMain.layoutParams as ViewGroup.MarginLayoutParams
                    params.bottomMargin += 64
                    detailFragmentActivityMain.layoutParams = params
                }

                MotionState.END -> {
                    val params = detailFragmentActivityMain.layoutParams
                    if (params.height != ViewGroup.LayoutParams.MATCH_PARENT) {
                        params.height = ViewGroup.LayoutParams.MATCH_PARENT
                        detailFragmentActivityMain.layoutParams = params
                    } else {
                        params.height = ViewGroup.LayoutParams.WRAP_CONTENT
                        detailFragmentActivityMain.layoutParams = params
                    }
                }
            }
        }
    }


    private fun initBottomNav() = with(binding) {
        navView.setupWithNavController(navController)
        navView.background = null

        var currentFragment = R.id.navigation_home

        mainFab.setOnClickListener {
            if (currentFragment == R.id.detail_fragment) {
                if (mainMotion.currentState == mainMotion.startState) {
                    mainMotion.transitionToEnd()
                } else {
                    mainMotion.transitionToStart()
                }
            } else {
                startActivity(
                    Intent(
                        this@MainActivity,
                        ShortsActivity::class.java
                    )
                )
            }
        }

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


        navController.addOnDestinationChangedListener { _, destination, _ ->
            currentFragment = destination.id
            when (destination.id) {
                R.id.navigation_home, R.id.navigation_mypage -> currentFragment = destination.id
            }
            when (destination.id) {
                else -> with(mainFab) {
                    setImageResource(R.drawable.ic_nav_fab_shorts)
                    backgroundTintList =
                        ColorStateList.valueOf(ContextCompat.getColor(context, R.color.white))
                }
            }
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
                    }
                } else {
                    binding.mainFab.setOnClickListener {
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