package com.example.pintube.ui.Search

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pintube.R
import com.example.pintube.databinding.ActivitySearchBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchActivity : AppCompatActivity() {
    private val binding by lazy { ActivitySearchBinding.inflate(layoutInflater) }
    private val viewModel: SearchViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val recyclerView = binding.rvSearchList
        recyclerView.layoutManager = LinearLayoutManager(this)

        val searchHistoryList = SharedPrefManager.getSearchHistory(this).toMutableList()
        val adapter = SearchListAdapter(searchHistoryList)

        recyclerView.adapter = adapter


        binding.ivSearchFragmentSearch.setOnClickListener {
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(window.decorView.applicationWindowToken, 0)
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
            viewModel.searchVideo(query)
            initViewModel()
        }
        binding.ivSearchFragmentBackArrow.setOnClickListener {
            finish()
        }

        binding.removeAll.setOnClickListener {
            if (searchHistoryList.isEmpty()) {
                Toast.makeText(this, "검색기록이 없습니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            //
            AlertDialog.Builder(this)
                .setIcon(R.drawable.pintube_logo)
                .setTitle("검색기록 전체삭제")
                .setMessage("모든 검색기록을 삭제 하시겠습니까? \n 삭제 시 다시 되돌릴 수 없습니다.")
                .setPositiveButton("삭제") { dialog, _ ->
                    searchHistoryList.clear()
                    adapter.notifyDataSetChanged()
                }
                .setNegativeButton("취소") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()

            SharedPrefManager.clearSearchHistory(this)

        }

        //검색창 텍스트 변화 감지
        binding.etSearchFragmentBar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            //텍스트 입력되면 x버튼 나옴.
            override fun afterTextChanged(s: Editable?) {
                if (s.isNullOrEmpty()) {
                    supportFragmentManager.popBackStack()
                }
                binding.tvNoSearch.visibility = View.GONE
                binding.rvSearchList.visibility = View.VISIBLE
                showXButton()

            }
        })


        //x버튼 누르면 검색창에 입력된 텍스트 없앰.
        binding.removeQuery.setOnClickListener {
            binding.etSearchFragmentBar.setText("")
        }

        adapter.itemClick = object : SearchListAdapter.ItemClick {
            override fun onClick(query: String) {

                viewModel.searchVideo(query)
                initViewModel()
                binding.etSearchFragmentBar.setText(query)

                Toast.makeText(this@SearchActivity, "$query 클릭", Toast.LENGTH_SHORT).show()

            }

            override fun onRemoveClick(position: Int) {
                val itemRemove = searchHistoryList[position]
                searchHistoryList.remove(itemRemove)

                adapter.notifyDataSetChanged()
            }
        }

    }

    private fun initViewModel() = viewModel.also { vm ->
        vm.searchDataList.observe(this) {
            if (it.isNullOrEmpty()) {
                binding.rvSearchList.visibility = View.GONE
                return@observe
            }
            val searchResultFragment = SearchResultFragment.newInstance(it)
            setFragment(searchResultFragment)
        }
    }

    //검색창에 텍스트 입력되면 x버튼 나옴. 지워지면 없어짐.
    private fun showXButton() {
        val text = binding.etSearchFragmentBar.text.toString()
        if (text.length >= 1) {
            binding.removeQuery.visibility = View.VISIBLE
        } else {
            binding.removeQuery.visibility = View.GONE
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