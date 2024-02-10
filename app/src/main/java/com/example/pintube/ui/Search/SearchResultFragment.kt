package com.example.pintube.ui.Search

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.FragmentResultListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pintube.R
import com.example.pintube.data.local.entity.LocalSearchEntity
import com.example.pintube.databinding.FragmentSearchResultBinding
import com.example.pintube.domain.entitiy.SearchEntity
import com.example.pintube.domain.entitiy.VideoEntity


class SearchResultFragment : Fragment() {
    companion object {
        fun newInstance(searchResults: List<SearchEntity>?): SearchResultFragment {
            val fragment = SearchResultFragment()
            val args = Bundle().apply {
                putParcelableArrayList("searchResults", searchResults as ArrayList<SearchEntity>?)
            }
            fragment.arguments = args
            return fragment
        }
    }

    private var _binding: FragmentSearchResultBinding? = null
    private val binding get() = _binding!!
    private var searchAdapter: SearchResultAdapter? = null
    private val items = ArrayList<SearchEntity>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val searchResults = arguments?.getParcelableArrayList<SearchEntity>("searchResults")

        var viewSelect = resources.getStringArray(R.array.view)
        var viewAdapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_dropdown_item, viewSelect)
        binding.searchResultSppiner.adapter = viewAdapter
        binding.searchResultSppiner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedItem = viewSelect[position]

                when (selectedItem) {
                    "조회수 순" -> {
                    }
                    "업로드 순(오름차순)" -> {
                        searchAdapter?.sortByDescending()
                    }
                    "업로드 순(내림차순)" -> {
                        searchAdapter?.sortByAscending()
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }



        if (searchResults != null) {
            items.addAll(searchResults)
            searchAdapter?.notifyDataSetChanged() // Notify adapter that data set has changed
        }
        searchAdapter = SearchResultAdapter(items)
        binding.rvResult.layoutManager = LinearLayoutManager(requireContext())
        binding.rvResult.adapter = searchAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}