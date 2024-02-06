package com.example.pintube.ui.Search

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.example.pintube.R
import com.example.pintube.data.model.VideoModel
import com.example.pintube.data.repository.ApiRepository
import com.example.pintube.data.repository.ApiRepositoryImpl
import com.example.pintube.databinding.ActivitySearchBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding
    private var searchItems: ArrayList<VideoModel> = arrayListOf()
    private val apiRepository: ApiRepository = ApiRepositoryImpl()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        binding.ivSearchFragmentSearch.setOnClickListener {
//            searchVideo()
//
//        }
//    }

//    private fun searchVideo() {
//        val query = binding.etSearchFragmentBar.text.toString().trim()
//        if (query.isNotEmpty()) {
//            GlobalScope.launch {
//                val video = apiRepository.searchYoutube(query)
//                setFragment()
//            }
//        }
//
//
//    }

    }


    private fun setFragment(frag: Fragment) {
        supportFragmentManager.commit {
            replace(R.id.search_frameLayout, frag)
            setReorderingAllowed(true)
            addToBackStack("")
        }
    }
}