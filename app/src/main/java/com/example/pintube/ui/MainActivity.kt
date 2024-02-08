package com.example.pintube.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.pintube.R
import com.example.pintube.databinding.ActivityMainBinding
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        initView()
    }
    private fun initView() {
        initBottomNav()
    }

    private fun initBottomNav() = with(binding){
        navView.setupWithNavController(navController)
        navView.background = null

        var currentFragment = (R.id.navigation_home)

        mainFab.setOnClickListener {
            if (mainMotion.currentState == mainMotion.startState) {
                mainMotion.transitionToEnd()
            } else {
                mainMotion.transitionToStart()
            }
        }

        mainFabShorts.setOnClickListener {
            val navOptions = NavOptions.Builder()
                .setPopUpTo(currentFragment, true)
                .build()
            navController.navigate(
                resId = R.id.navigation_shorts,
                args = null,
                navOptions = navOptions,
                )
            mainMotion.transitionToStart()
        }

        mainFabPin.setOnClickListener {
            val navOptions = NavOptions.Builder()
                .setPopUpTo(currentFragment, true)
                .build()
            navController.navigate(
                R.id.navigation_detail,
                    args = null,
                navOptions = navOptions,
            )
            mainMotion.transitionToStart()
        }


        navController.addOnDestinationChangedListener { _, destination,_ ->

            when (destination.id) {
                R.id.navigation_home -> currentFragment = destination.id
                R.id.navigation_mypage -> currentFragment = destination.id
            }
        }
    }

}