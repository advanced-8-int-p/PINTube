package com.example.pintube.ui.Search

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.lifecycleScope
import com.example.pintube.R
import com.example.pintube.data.remote.retrofit.YouTubeApi
import com.example.pintube.data.repository.sever.ApiRepositoryImpl
import com.example.pintube.databinding.ActivitySearchBinding
import com.example.pintube.domain.repository.ApiRepository
import kotlinx.coroutines.launch


class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding

    private val apiRepository: ApiRepository = ApiRepositoryImpl(YouTubeApi.youtubeNetwork)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ivSearchFragmentSearch.setOnClickListener {
            val query = binding.etSearchFragmentBar.text.toString()
            if (query.isNotBlank()) {
                SharedPrefManager.saveSearchHistory(this, query)
            }
            searchVideo(query)
        }
        binding.ivSearchFragmentBackArrow.setOnClickListener {
            super.onBackPressed()
        }
    }

    private fun searchVideo(query : String) {
        lifecycleScope.launch {
            val searchResults = apiRepository.searchResult(query)
            val searchResultFragment = SearchResultFragment.newInstance(searchResults)
            setFragment(searchResultFragment)
        }
    }

    private fun setFragment(frag: Fragment) {
        supportFragmentManager.commit {
            replace(R.id.search_frameLayout, frag)
            setReorderingAllowed(true)
            addToBackStack("")
        }
    }
}