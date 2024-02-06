package com.example.pintube.ui.Search

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.example.pintube.R
import com.example.pintube.databinding.ActivityMainBinding
import com.example.pintube.databinding.ActivitySearchBinding
import kotlinx.coroutines.launch

class SearchActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySearchBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ivSearchFragmentSearch.setOnClickListener {

        }


    }

    private fun communicationNetwork(param : HashMap<String, String>) = lifecycleScope.launch {

    }
}