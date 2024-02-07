package com.example.pintube.ui.Search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentResultListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pintube.data.repository.entitiy.SearchEntity
import com.example.pintube.databinding.FragmentSearchResultBinding


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

        if (searchResults != null) {
            items.addAll(searchResults)
            searchAdapter?.notifyDataSetChanged() // Notify adapter that data set has changed
        }

        searchAdapter = SearchResultAdapter(requireContext(), items)
        binding.rvResult.layoutManager = LinearLayoutManager(requireContext())
        binding.rvResult.adapter = searchAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}