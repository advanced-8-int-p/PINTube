package com.example.pintube.ui.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.lifecycle.lifecycleScope
import com.example.pintube.R
import com.example.pintube.databinding.ActivitySplashBinding
import com.example.pintube.ui.home.HomeViewModel
import com.example.pintube.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class Splash : AppCompatActivity() {

    private val binding: ActivitySplashBinding by lazy {
        ActivitySplashBinding.inflate(layoutInflater)
    }

    private val viewModel: HomeViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

    }

    private fun init() {
        initViewModel()
    }

    private fun initViewModel() = with(viewModel){
        populars.observe(this@Splash) {
        }
    }

    override fun onResume() {
        super.onResume()
        binding.mlSplashLayout.post {
            setMotion()
        }
    }
    private fun setMotion() = with(binding.mlSplashLayout) {
        setTransitionListener(object : MotionLayout.TransitionListener {
            override fun onTransitionChange(motionLayout: MotionLayout, startId: Int, endId: Int, progress: Float) {}
            override fun onTransitionStarted(motionLayout: MotionLayout, startId: Int, endId: Int) {}
            override fun onTransitionTrigger(motionLayout: MotionLayout, triggerId: Int, positive: Boolean, progress: Float) {}
            override fun onTransitionCompleted(motionLayout: MotionLayout, currentId: Int) {
                when (currentId) {
                    R.id.end -> setTransitionAction(R.id.end, R.id.statusbar, 600)
                    R.id.statusbar -> setTransitionAction(R.id.statusbar, R.id.loading, 700)
                    R.id.loading -> navigateToMain()
                }
            }
        })
        setTransitionAction(R.id.start, R.id.end, 600)
    }

    private fun setTransitionAction(startId: Int, endId: Int, duration: Int) = with(binding.mlSplashLayout) {
        setTransition(startId, endId)
        setTransitionDuration(duration)
        transitionToEnd()
    }

    private fun navigateToMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
