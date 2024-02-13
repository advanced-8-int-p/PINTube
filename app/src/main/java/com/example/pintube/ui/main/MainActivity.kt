package com.example.pintube.ui.main

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
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

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val navHostFragment by lazy {
        supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
    }

    private val navController by lazy {
        navHostFragment.navController
    }

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
    }

    private fun initBottomNav() = with(binding) {
        navView.setupWithNavController(navController)
        navView.background = null

        var currentFragment = R.id.navigation_home

        mainFab.setOnClickListener {
            if (currentFragment == R.id.navigation_detail) {
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
            mainMotion.transitionToStart()
        }


        navController.addOnDestinationChangedListener { _, destination, _ ->
            currentFragment = destination.id
            when (destination.id) {
                R.id.navigation_home, R.id.navigation_mypage -> currentFragment = destination.id
            }
            when (destination.id) {
                R.id.navigation_detail -> with(mainFab){
                    setImageResource(R.drawable.ic_main_fab_plus)
                    backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.main_color))
                }
                else -> with(mainFab){
                    setImageResource(R.drawable.ic_nav_fab_shorts)
                    backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.white))
                }
            }
        }
    }

    override fun onBackPressed() {
        val currentFragment = navHostFragment.childFragmentManager.fragments[0]

        if (currentFragment is DetailFragment) {
            navController.navigate(
                resId = R.id.action_navigation_detail_to_navigation_home,
            )
        } else {
            super.onBackPressed()
        }
    }

}