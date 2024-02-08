package com.example.pintube.ui.Search

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pintube.R
import com.example.pintube.data.repository.ApiRepositoryImpl
import com.example.pintube.databinding.ActivitySearchBinding
import com.example.pintube.domain.entitiy.SearchEntity
import com.example.pintube.domain.repository.ApiRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import androidx.recyclerview.widget.RecyclerView.Adapter



class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding

    private val apiRepository: ApiRepository = ApiRepositoryImpl()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val recyclerView = binding.rvSearchList
        recyclerView.layoutManager = LinearLayoutManager(this)

        val searchHistoryList = SharedPrefManager.getSearchHistory(this).toMutableList()
        val adapter = SearchListAdapter(searchHistoryList)

        recyclerView.adapter = adapter


        binding.ivSearchFragmentSearch.setOnClickListener {
            val query = binding.etSearchFragmentBar.text.toString()
            if (query.isNotBlank()) {
                SharedPrefManager.saveSearchHistory(this, query)
                val searchHistorySet = SharedPrefManager.getSearchHistory(this)
                adapter.notifyDataSetChanged()
                Log.d("SearchActivity", "Search history: $searchHistorySet")
            } else {
                Toast.makeText(this, "검색어를 입력해주세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            searchVideo(query)
        }
        binding.ivSearchFragmentBackArrow.setOnClickListener {
            finish()
        }

        adapter.itemClick = object : SearchListAdapter.ItemClick {
            override fun onClick(view: View, position: Int) {

            }
            override fun onRemoveClick(position: Int) {
                val itemRemove = searchHistoryList[position]
                searchHistoryList.remove(itemRemove)


                adapter.notifyDataSetChanged()

            }
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