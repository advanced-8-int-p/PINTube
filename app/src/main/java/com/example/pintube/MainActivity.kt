package com.example.pintube

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
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

        mainFab.setOnClickListener {
            if (mainMotion.currentState == mainMotion.startState) {
                mainMotion.transitionToEnd()
            } else {
                mainMotion.transitionToStart()
            }
        }

        mainFabShorts.setOnClickListener {
            navController.navigate(R.id.navigation_shorts)
            mainMotion.transitionToStart()
        }

        mainFabPin.setOnClickListener {
            navController.navigate(R.id.navigation_detail)
            mainMotion.transitionToStart()
        }

    }
}